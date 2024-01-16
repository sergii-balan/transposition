package com.sb.transposition.utils;

import lombok.NonNull;

public class ConversionHelper {
	private static final byte D_BASE = 12;

	public static byte to12(@NonNull byte[] b10) {
		if (b10[0] >= 0) {
			return (byte) (b10[0] * D_BASE + b10[1]);
		}
		
		byte b = (byte) ((b10[0] + 1) * D_BASE);
		byte r = (byte) (b10[1] - D_BASE - 1);
		return (byte) (b + r);
	}
	
	public static byte to12(byte b10) {
		return (byte) ( -D_BASE < b10 && b10 < D_BASE ? b10 : b10 / D_BASE * 10 + b10 % D_BASE);
	}
	
	public static byte[] to10(byte b12) {
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
}
