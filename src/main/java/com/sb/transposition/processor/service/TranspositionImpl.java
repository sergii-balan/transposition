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
	
	private static final byte L_OCTAVE = -3;
	private static final byte H_OCTAVE = 5;
	private static final byte D_BASE = 12;
	
	@Override
	public byte[][] process(@NonNull String data, @NonNull String transposition) throws Exception {
		return process(convert(data), Byte.valueOf(transposition));
	}

	@Override
	public byte[][] process(@NonNull byte[][] data, byte transposition) throws Exception {
		for (int i = 0; i < data.length; i++) {
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
		
		byte term1_b12 = (byte) (term1[0] * D_BASE + term1[1]);
		byte term2_b12 = (byte) (term2 < D_BASE ? term2 : term2 / D_BASE + term2 % D_BASE);
		byte result_b12 = (byte) (term1_b12 + term2_b12);
		
		byte[] result = new byte[2];
		result[0] = (byte) (result_b12 / D_BASE);
		result[1] = (byte) (result_b12 % D_BASE);
		
		if (result[1] == 0) {
			result[0] -= 1;
			result[1] = 12;
		}
		
		return result;
	}
	
	private static void checkValue(byte[] value) throws Exception {
		if (L_OCTAVE > value[0] || H_OCTAVE < value[0]) {
			throw new Exception(String.format("Octave number is '%d'.", value[0]));
		}
		
		if (value[1] < 1 || value[1] > D_BASE) {
			throw new Exception(String.format("Note value is out of range: '%d'.", value[1]));
		}
	}
}
