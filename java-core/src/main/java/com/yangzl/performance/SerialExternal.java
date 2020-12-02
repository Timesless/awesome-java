package com.yangzl.performance;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author yangzl
 * @date 2020/11/29 21:41
 * @desc 序列化与反序列化 调优
 */

public class SerialExternal {

	/**
	 * 2020/11/29 压缩序列化数据
	 */
	static class StockPriceHistroy implements Serializable {
		
		public StockPriceHistroy(SortedMap<Date, BigDecimal> prices) {
			this.prices = prices;
		}

		private byte[] zipOop;
		/*
		 * 标记为不序列化，而是在writeObject, readObject中自己处理，效率会提升一小部分
		 */
		private transient SortedMap<Date, BigDecimal> prices;

		/**
		 * 2020/11/29 序列化
		 *
		 * @param out 对象输出流
		 * @return void
		 */
		private void writeObject(ObjectOutputStream out) throws IOException {
			if (zipOop == null) {
				zipPrice();
			}
			out.defaultWriteObject();
		}

		/**
		 * 2020/11/29 反序列化
		 *
		 * @param in 对象输入流
		 * @return void
		 */
		private void readObject(ObjectInputStream in) throws Exception {
			in.defaultReadObject();
			unzipPrice();
		}

		/**
		 * 2020/11/29 压缩序列化后的对象
		 * <p>
		 * 节点流：
		 * ByteArrayInputStream / ByteArrayOutputStream
		 * FileInputStream, FileReader / FileOutputStream, FileWriter
		 * PipedInputStream / PipedOutputStream
		 * StringReader / StringWriter
		 * <p>
		 * <p>
		 * 处理流：
		 * 缓冲流一般用于提升性能
		 * BufferedInputStream, BufferedRader / BufferedOutputStream, BufferedWriter
		 *
		 * @param () v
		 * @return void
		 */
		private void zipPrice() {

			try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				 GZIPOutputStream zipOut = new GZIPOutputStream(bos);
				 ObjectOutputStream oop = new ObjectOutputStream(new BufferedOutputStream(zipOut))) {
				oop.writeObject(prices);
				zipOop = bos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 2020/11/29 解压后反序列化
		 *
		 * @param () v
		 * @return void
		 */
		private void unzipPrice() {
			
			try (ByteArrayInputStream bis = new ByteArrayInputStream(zipOop);
				 GZIPInputStream zipIn = new GZIPInputStream(bis);
				 ObjectInputStream oop = new ObjectInputStream(new BufferedInputStream(zipIn))){
				final Object o = oop.readObject();
				this.prices = (SortedMap<Date, BigDecimal>) o;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 序列化测试
	private static void serial() {
		long t1 = System.currentTimeMillis();
		try (FileOutputStream out = new FileOutputStream("d:/oop.out");
			 ObjectOutputStream oopOut = new ObjectOutputStream(out)) {
			for (int i = 0; i < 20000; ++i) {
				TreeMap<Date, BigDecimal> map = new TreeMap<>();
				map.put(new Date(), BigDecimal.ZERO);
				map.put(new Date(), BigDecimal.ONE);
				map.put(new Date(), BigDecimal.TEN);
				StockPriceHistroy client = new StockPriceHistroy(map);
				oopOut.writeObject(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.printf("序列化30000 耗时：%d \n", System.currentTimeMillis() - t1);
	}

	// unserial
	private static Object unserial() {
		try (FileInputStream in = new FileInputStream("d:/oop.out"); 
			 final ObjectInputStream oopIn = new ObjectInputStream(in)){
			return oopIn.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		// 序列化到文件
		try (FileOutputStream out = new FileOutputStream("d:/oop.out");
			 ObjectOutputStream oopOut = new ObjectOutputStream(out)) {
				TreeMap<Date, BigDecimal> map = new TreeMap<>();
				map.put(new Date(), BigDecimal.ZERO);
				map.put(new Date(new Date().getTime() - 10000L), BigDecimal.ONE);
				StockPriceHistroy client = new StockPriceHistroy(map);
				oopOut.writeObject(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 反序列化
		StockPriceHistroy stock = (StockPriceHistroy) unserial();
		System.out.println("stock: " + stock);
		System.out.println(stock.prices);

	}

	
}
