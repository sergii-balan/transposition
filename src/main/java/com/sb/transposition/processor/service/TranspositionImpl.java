package com.sb.transposition.processor.service;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TranspositionImpl implements Transposition {
	
	private static final byte[][] EMPTY_BARR = new byte[0][0];
	private static final int ARG_MIN_LEN = 7; //[[1,2]]
	private static final String BR_S = "[";
	private static final String BR_SS = BR_S + BR_S;
	private static final String BR_E = "]";
	private static final String BR_EE = BR_E + BR_E;
	
	private static final byte[] L_OCTAVE = new byte[] {-3, 10};
	private static final byte[] H_OCTAVE = new byte[] { 5,  1};
	private static final byte D_BASE = 12;
	
	@Override
	public byte[][] process(@NonNull String data, @NonNull String transposition) throws Exception {
		return process(convert(data), Byte.valueOf(transposition));
	}

	@Override
	public byte[][] process(@NonNull byte[][] data, byte transposition) throws Exception {
		for (int i = 0; i < data.length; i++) {
			checkValue(data[i]);
			data[i] = add(data[i], transposition);
			checkValue(data[i]);
		}
		return data;
	}
	
	private static byte[][] convert(@NonNull String data) {
		if (data.isBlank() || data.trim().length() < ARG_MIN_LEN) {
			return EMPTY_BARR;
		}
		
		if (!data.trim().startsWith(BR_SS) && !data.trim().endsWith(BR_EE)) {
			return EMPTY_BARR;
		}
		
		return new Gson().fromJson(data, byte[][].class);
	}
	
	private static byte[] add(@NonNull byte[] term1, byte term2) {
		if (term1.length != 2) {
			log.error("term1 length is '{}'.", term1.length);
			return new byte[0];
		}
		
		byte b12 = (byte) (to12(term1) + to12(term2));
		return to10(b12);
	}
	
	private static byte to12(@NonNull byte[] b10) {
		if (b10[0] >= 0) {
			return (byte) (b10[0] * D_BASE + b10[1]);
		}
		
		byte b = (byte) ((b10[0] + 1) * D_BASE);
		byte r = (byte) (b10[1] - D_BASE - 1);
		return (byte) (b + r);
	}
	
	private static byte to12(byte b10) {
		return (byte) (b10 < D_BASE ? b10 : b10 / D_BASE + b10 % D_BASE);
	}
	
	private static byte[] to10(byte b12) {
		if (b12 >= 0) {
			return positiveTo10(b12);
		}
		
		return negativeTo10(b12);
	}
	
	private static byte[] positiveTo10(byte b12) {
		byte[] b10 = new byte[2];
		b10[0] = (byte) (b12 / D_BASE);
		b10[1] = (byte) (b12 % D_BASE);
		
		if (b10[1] == 0) {
			b10[0] -= 1;
			b10[1] = 12;
		}
		
		return b10;
	}
	
	private static byte[] negativeTo10(byte b12) {
		byte[] b10 = new byte[2];
		b10[0] = (byte) (b12 / D_BASE - 1);
		b10[1] = (byte) (b12 % D_BASE + D_BASE + 1);
		return b10;
	}
	
	private static void checkValue(byte[] value) throws Exception {
		if (L_OCTAVE[0] > value[0] || H_OCTAVE[0] < value[0]) {
			throw new Exception(String.format("Octave number is '%d'.", value[0]));
		}
		
		if ((L_OCTAVE[0] == value[0] && L_OCTAVE[1] > value[1]) 
				|| (H_OCTAVE[0] == value[0] && H_OCTAVE[1] < value[1])) {
			throw new Exception(String.format("Value is '[%d,%d]'.", value[0], value[1]));
		}
		
		if (value[1] < 1 || value[1] > D_BASE) {
			throw new Exception(String.format("Note value is out of range: '%d'.", value[1]));
		}
	}
}
