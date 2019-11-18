package ea.sof.ms_content_filter;

import com.google.gson.Gson;
import ea.sof.shared.models.Question;
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

	@KafkaListener(topics = "${topicNewQuestion}", groupId = "${subsNewQuestionFilter}")
	public void newQuestion(String message) {

		System.out.println("SubsNewQuestionFilter: New message from topic: " + message);

		Gson gson = new Gson();
		Question question =  gson.fromJson(message, Question.class);

		System.out.println("SubsNewQuestionFilter: As object: " + question);

		boolean banThisQuestion = false;

		if (banThisQuestion){
			kafkaTemplate.send(env.getProperty("topicBanQuestion"), question.getId());
		}
	}

}
