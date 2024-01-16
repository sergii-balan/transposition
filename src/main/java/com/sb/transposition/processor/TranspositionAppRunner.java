package com.sb.transposition.processor;

import com.sb.transposition.processor.service.Transposition;
import com.sb.transposition.processor.service.TranspositionPersister;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranspositionAppRunner implements ApplicationRunner {
	
	private final Transposition transposition;
	private final TranspositionPersister persister;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (args.getSourceArgs().length == 0) {
			log.error("ARGS is EMPTY!");
		}
		
		if (args.getSourceArgs().length != 2) {
			log.error("ARGS count is '{}'.", args.getSourceArgs().length);
		}
		
		log.info(Arrays.toString(args.getSourceArgs()));
		
		byte[][] t = transposition.process(args.getSourceArgs()[1], args.getSourceArgs()[0]);
		persister.persist(t);
	}

}
