package com.sb.transposition.processor.service;

import com.google.gson.Gson;
import com.sb.transposition.processor.data.Note;
import java.util.Arrays;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;


@Slf4j
@Primary
@Component
public class TranspositionNoteImpl implements Transposition {
	private static final byte[][] EMPTY_BARR = new byte[0][0];
	private static final int ARG_MIN_LEN = 7; //[[1,2]]
	private static final Note MIN_NOTE = new Note(-3,10);
	private static final Note MAX_NOTE = new Note( 5, 1);

	@Override
	public byte[][] process(@NonNull String data, @NonNull String transposition) throws Exception {
		return process(convert(data), Byte.valueOf(transposition));
	}

	@Override
	public byte[][] process(@NonNull byte[][] data, byte transposition) throws Exception {
		log.info("processing...");
		return Arrays.stream(data)
				.map(Note::from)
				.map(note -> checkValue(note))
				.map(note -> checkValue(note.transpose(transposition)))
				.map(Note::asByteArray)
				.toArray(byte[][]::new);
	}
	
	private static byte[][] convert(@NonNull String data) {
		if (data.isBlank() || data.trim().length() < ARG_MIN_LEN) {
			return EMPTY_BARR;
		}
		
		return new Gson().fromJson(data, byte[][].class);
	}
	
	private static Note checkValue(Note note) throws RuntimeException {
		if (MIN_NOTE.isGreaterThan(note) || MAX_NOTE.isLessThan(note)) {
			throw new RuntimeException(String.format("Note is '%s'.", note));
		}
		
		return note;
	}

}
