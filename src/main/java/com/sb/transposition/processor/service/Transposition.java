package com.sb.transposition.processor.service;

public interface Transposition {
	byte[][] process(String data, String transposition) throws Exception;
	byte[][] process(byte[][] data, byte transposition) throws Exception;
}
