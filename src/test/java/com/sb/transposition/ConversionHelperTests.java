package com.sb.transposition;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sb.transposition.utils.ConversionHelper;
import org.junit.jupiter.api.Test;

public class ConversionHelperTests {

	@Test
	void to10Test1() {
		assertArrayEquals(new byte[] {1, 10}, ConversionHelper.to10((byte)22));
	}
	
	@Test
	void to12Test1() {
		assertEquals(13, ConversionHelper.to12((byte)35));
	}
	
	@Test
	void to12Test2() {
		assertEquals(41, ConversionHelper.to12(new byte[] {3, 5}));
	}
	
}
