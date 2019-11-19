package ea.sof.ms_content_filter.listeners;

import com.google.gson.Gson;
import ea.sof.ms_content_filter.util.FilterBadWords;
import ea.sof.shared.queue_models.QuestionQueueModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubsNewQuestionFilter {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private Environment env;
	private static final Logger LOGGER = LoggerFactory.getLogger(SubsNewQuestionFilter.class);
	private static final FilterBadWords filterBadWords = new FilterBadWords();

	@KafkaListener(topics = "${topicNewQuestion}", groupId = "${subsNewQuestionFilter}")
	public void newQuestion(String message) {

		LOGGER.info("SubsNewQuestionFilter: New message from topic: " + message);
		System.out.println("SubsNewQuestionFilter: New message from topic: " + message);

		Gson gson = new Gson();
		QuestionQueueModel question =  gson.fromJson(message, QuestionQueueModel.class);

		LOGGER.info("SubsNewQuestionFilter: As object: " + question);
		System.out.println("SubsNewQuestionFilter: As object: " + question);

		boolean banThisQuestion = false;

		if (filterBadWords.filterBadWords(question.getTitle())) banThisQuestion = true;
		if (filterBadWords.filterBadWords(question.getBody()))  banThisQuestion = true;

		if (banThisQuestion){
			kafkaTemplate.send(env.getProperty("topicBanQuestion"), question.getId());
			LOGGER.info("SubsNewQuestionFilter: Banned the question id=" + question.getId());
		}
	}

}
