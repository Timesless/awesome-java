package com.yangzl;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.util.Properties;
import java.util.stream.IntStream;

/**
 * @Author: yangzl
 * @Date: 2020/8/24 15:13
 * @Desc: ..
 */
public class Producer {

	public static final String brokerList = "localhost:9092";
	public static final String topic = "topic-demo";

	/**
	 * 2020/9/17 基本测试 
	 * @param 
	 * @return 
	 */
	@Test
	public void test1() {
		Properties props = new Properties();
		// key序列化器，未指定分区发送时，在经过分区器时通过key使用MurmurHash2计算
		props.put("key.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer",
				"org.apache.kafka.common.serialization.StringSerializer");
		props.put("bootstrap.server", brokerList);
		
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello, kafka");
		IntStream.rangeClosed(0, 10).forEach(i -> producer.send(record));
		producer.close();
	}
	
	/**
	 * 2020/9/17 ProducerConfig与ConsumerConfig
	 * @param 
	 * @return 
	 */
	@Test
	public void test2() {
		Properties props = new Properties();
		String stringSerializerName = StringSerializer.class.getName();
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, stringSerializerName);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, stringSerializerName);
	}
	
	/**
	 * 2020/9/17 生产者一些重要参数说明 
	 * @param 
	 * @return 
	 */
	@Test
	public void test3() {
		/*
		 * acks
		 * -1 / all：所有ISR副本确认之后才发送acks，当ISR只有leader副本时退化为acks = "1"的情况
		 * 0：消息发送无需确认机制
		 * 1：leader副本确认之后发送acks
		 */
		String acks = ProducerConfig.ACKS_CONFIG;
		
		
	}
}
