package ea.sof.ms_content_filter.listeners;

import com.google.gson.Gson;
import ea.sof.ms_content_filter.util.FilterBadWords;
import ea.sof.shared.queue_models.AnswerQueueModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubsNewAnswerFilter {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private Environment env;
	private static final Logger LOGGER = LoggerFactory.getLogger(SubsNewAnswerFilter.class);
	private static final FilterBadWords filterBadWords = new FilterBadWords();

	@KafkaListener(topics = "${topicNewAnswer}", groupId = "${subsNewAnswerFilter}")
	public void newAnswer(String message) {

		LOGGER.info("SubsNewAnswerFilter: New message from topic: " + message);

		Gson gson = new Gson();
		AnswerQueueModel answer =  gson.fromJson(message, AnswerQueueModel.class);

//		LOGGER.info("SubsNewAnswerFilter: As object: " + answer);

		boolean banThisAnswer = filterBadWords.filterBadWords(answer.getBody());

		if (banThisAnswer){

			kafkaTemplate.send(env.getProperty("topicBanAnswer"), answer.getId());
			LOGGER.info("SubsNewAnswerFilter: Banned answerId=" + answer.getId());
		}
	}

}
