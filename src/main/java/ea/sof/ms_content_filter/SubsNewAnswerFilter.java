package ea.sof.ms_content_filter;

import com.google.gson.Gson;
import ea.sof.shared.models.Answer;
import ea.sof.shared.models.Question;
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

	@KafkaListener(topics = "${topicNewAnswer}", groupId = "${subsNewAnswerFilter}")
	public void newAnswer(String message) {

		System.out.println("SubsNewAnswerFilter: New message from topic: " + message);

		Gson gson = new Gson();
		Answer answer =  gson.fromJson(message, Answer.class);

		System.out.println("SubsNewAnswerFilter: As object: " + answer);

		boolean banThisAnswer = false;

		if (banThisAnswer){
			kafkaTemplate.send(env.getProperty("topicBanAnswer"), answer.getId());
		}
	}

}
