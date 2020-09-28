package com.yangzl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yangzl
 * @Date: 2020/8/24 15:14
 * @Desc: ..
 */
public class Consumer {
	
	public static final String brokerList = "localhost:9092";
	public static final String topic = "topic-demo";
	public static final String gorupId = "group.demo";

	public static void main(String[] args) {
		Map<String, String> config = new HashMap<>(4);
		config.put("bootstrap.server", brokerList);
		config.put("group.id", gorupId);
		KafkaConsumer<String, String> consumer =
				new KafkaConsumer(config, new StringDeserializer(), new StringDeserializer());
		consumer.subscribe(Collections.singletonList(topic));
		while (true) {
			ConsumerRecords<String, String> msgs = consumer.poll(Duration.ofMillis(2000));
			for (ConsumerRecord<String, String> msg : msgs) {
				System.out.printf("header is %s, msg is %s", msg.headers(), msg.value());
			}
		}
	}

}
