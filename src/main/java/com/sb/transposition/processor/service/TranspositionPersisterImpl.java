package com.sb.transposition.processor.service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.sb.transposition.config.TranspositionConfig;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranspositionPersisterImpl implements TranspositionPersister {
	
	private final TranspositionConfig config;

	@Override
	public boolean persist(byte[][] data) {
		try (Writer writer = new FileWriter(config.getFilePath())) {
			Gson gson = new Gson();
			gson.toJson(data, byte[][].class, writer);
		} catch (JsonIOException | IOException e) {
			log.error("File '{}' creation error: '{}'.", config.getFilePath(), e.getMessage());
			return false;
		}
		
		log.info("File '{}' created.", config.getFilePath());
		return true;
	}
	
}
