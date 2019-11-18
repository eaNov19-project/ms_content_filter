package ea.sof.ms_content_filter.listeners;

import com.google.gson.Gson;
import ea.sof.ms_content_filter.util.FilterBadWords;
import ea.sof.shared.queue_models.AnswerQueueModel;
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
	private FilterBadWords filterBadWords;

	@KafkaListener(topics = "${topicNewAnswer}", groupId = "${subsNewAnswerFilter}")
	public void newAnswer(String message) {

		System.out.println("SubsNewAnswerFilter: New message from topic: " + message);

		Gson gson = new Gson();
		AnswerQueueModel answer =  gson.fromJson(message, AnswerQueueModel.class);

		System.out.println("SubsNewAnswerFilter: As object: " + answer);

		boolean banThisAnswer = filterBadWords.filterBadWords(answer.getBody());

		if (banThisAnswer){
			kafkaTemplate.send(env.getProperty("topicBanAnswer"), answer.getId());
		}
	}

}
