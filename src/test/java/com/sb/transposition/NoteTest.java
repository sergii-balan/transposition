package com.sb.transposition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.sb.transposition.processor.data.Note;

public class NoteTest {
	
	@Test
	void ComparatorTest() {
		final Note n1 = new Note(1, 2);
		
		assertEquals(new Note(1, 2), new Note(1, 2));
		assertTrue(n1.isLessThan(new Note(1, 3)));
		assertTrue(n1.isLessThan(new Note(2, 1)));
		assertTrue(n1.isGreaterThan(new Note(0, 2)));
		assertTrue(n1.isGreaterThan(new Note(1, 1)));
	}

}
