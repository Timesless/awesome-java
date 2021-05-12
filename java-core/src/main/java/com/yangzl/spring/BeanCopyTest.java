package com.yangzl.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

/**
 * @author yangzl
 * @date 2020/11/11 17:20
 */
@Data
class TestVO {
	private Long id;
	private String name;
}

@Data
@AllArgsConstructor
class TestPO {
	private String id;
	private String name;
}

public class BeanCopyTest {
	@Test
	public void testCopy() {
		TestPO po = new TestPO("12344567", "hhh");
		TestVO vo = new TestVO();
		BeanUtils.copyProperties(po, vo);
		System.out.println(vo);
	}
}
