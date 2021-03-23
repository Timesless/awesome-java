package com.yangzl.yangzl;

import lombok.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author yangzl
 * @date 2021/3/12
 * @desc
 */
@Slf4j
public class CopyTest {
	@Data
	@AllArgsConstructor
	class School implements Cloneable {
		String name;
		@Override
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}
	@Data
	@AllArgsConstructor
	class Student implements Cloneable {
		private String name;
		private School school;
		@Override
		public Object clone() throws CloneNotSupportedException {
			Student clone = (Student) super.clone();
			clone.setSchool((School) clone.school.clone());
			return clone;
		}
	}

	@Test
	public void testCopy() throws CloneNotSupportedException {
		School tsinghua = new School("tsinghua university");
		Student mm = new Student("明明", tsinghua);
		Student me = (Student) mm.clone();
		me.setName("me");
		tsinghua.setName("野鸡大学");
		System.out.println(me);
		System.out.println(mm);

	}
}
