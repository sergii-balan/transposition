package com.sb.transposition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sb.transposition.processor.service.Transposition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(args = {"0", "[[0,1],[0,1]]"})
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
class TranspositionApplicationTests {
	
	@Autowired
	private Transposition transposition;

	@Test
	void transpositionProcess1() throws Exception {
		byte[][] expected = new byte[][] {{1,7},{3,9}};
		byte[][] result = transposition.process("[[1,2],[3,4]]", "5");
		assertThat(result).isEqualTo(expected);
	}

	@Test
	void transpositionProcess2() throws Exception {
		byte[][] expected = new byte[][] {{0,9},{2,11}};
		byte[][] result = transposition.process("[[1,2],[3,4]]", "-5");
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	void transpositionProcess3() throws Exception {
		byte[][] expected = new byte[][] {{0,9},{0,12},{1,2}};
		byte[][] result = transposition.process("[[1,2],[1,5],[1,7]]", "-5");
		assertThat(result).isEqualTo(expected);
	}
	
	@Test
	void transpositionProcess4() throws Exception {
		Exception exception = assertThrows(Exception.class, () -> {
			transposition.process("[[1,2]]", "-100");
	    });
		
		assertTrue("Octave number is '-7'.".equals(exception.getMessage()));
	}
}
