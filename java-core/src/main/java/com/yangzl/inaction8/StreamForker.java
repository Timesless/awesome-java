package com.yangzl.inaction8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author yangzl
 * @date 2020/11/25 00:32
 * @desc 可同时获取多个结果的流
 */

public class StreamForker<T> {
	
	interface Results {
		<R> R get(Object key);
	}
	
	static class ForkingStreamConsumer<T> implements Consumer<T>, Results {
		
		static final Object END_OF_OBJECT = new Object();
		
		private final List<BlockingQueue<T>> queues;
		private final Map<Object, Future<?>> actions;
		
		public ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
			this.queues = queues;
			this.actions = actions;
		}

		@Override
		public <R> R get(Object key) {
			try {
				return ((Future<R>)actions.get(key)).get();
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}

		@Override
		public void accept(T t) {
			queues.forEach(bq -> bq.add(t));
		}
		
		void finish() {
			accept((T)END_OF_OBJECT);
		}
	}
	
	static class BlockingQueueSpliterator<T> implements Spliterator<T> {
		
		private final BlockingQueue<T> q;
		
		BlockingQueueSpliterator(BlockingQueue<T> q) {
			this.q = q;
		}
		
		@Override
		public boolean tryAdvance(Consumer<? super T> action) {
			T t;
			while (true) {
				try {
					t = q.take();
					break;
				} catch (InterruptedException e) {}
			}
			if (t != ForkingStreamConsumer.END_OF_OBJECT) {
				action.accept(t);
				return true;
			}
			return false;
		}

		@Override
		public Spliterator<T> trySplit() {
			return null;
		}

		@Override
		public long estimateSize() {
			return 0;
		}

		@Override
		public int characteristics() {
			return 0;
		}
	}
	
	private final Stream<T> stream;
	private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();
	
	public StreamForker(Stream<T> stream) {
		this.stream = stream;
	}
	
	public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
		forks.put(key, f);
		return this;
	}

	ForkingStreamConsumer<T> build() {
		List<BlockingQueue<T>> queues = new ArrayList<>();

		Map<Object, Future<?>> actions =
				forks.entrySet().stream().reduce(
						new HashMap<Object, Future<?>>(),
						(map, e) -> {
							map.put(e.getKey(), getOperationResult(queues, e.getValue()));
							return map;
						},
						(m1, m2) -> {
							m1.putAll(m2);
							return m1;
						}
				);
		return new ForkingStreamConsumer<>(queues, actions);
	}
	
	public Results getResults() {
		ForkingStreamConsumer<T> consumer = build();
		try {
			stream.sequential().forEach(consumer);
		} finally {
			consumer.finish();
		}
		return consumer;
	}
	
	private Future<?> getOperationResult(List<BlockingQueue<T>> queues,
										 Function<Stream<T>, ?> f) {
		BlockingQueue<T> queue = new LinkedBlockingDeque<>();
		queues.add(queue);
		Spliterator<T> spliterator = new BlockingQueueSpliterator<>(queue);
		Stream<T> source = StreamSupport.stream(spliterator, false);
		return CompletableFuture.supplyAsync(() -> f.apply(source));
	}


	public static void main(String[] args) {
		Stream<String> stream = Stream.of("hello", "stream", "blockingQueue");

		StreamForker.Results rs = new StreamForker<String>(stream)
				.fork("length", s -> s.map(String::length)
						.collect(Collectors.toList()))
				.fork("upper", s -> s.map(String::toUpperCase)
						.collect(Collectors.toList()))
				.getResults();

		System.out.println((List<?>) rs.get("length"));
		System.out.println((List<?>) rs.get("upper"));
	}

}
