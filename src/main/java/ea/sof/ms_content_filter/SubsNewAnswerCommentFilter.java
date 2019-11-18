package ea.sof.ms_content_filter;

import com.google.gson.Gson;
import ea.sof.shared.models.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubsNewAnswerCommentFilter {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private Environment env;

	@KafkaListener(topics = "${topicNewAnswerComment}", groupId = "${subsNewAnswerCommentFilter}")
	public void newAnswerComment(String message) {

		System.out.println("SubsNewAnswerCommentFilter: New message from topic: " + message);

		Gson gson = new Gson();
		AnswerComment answerComment =  gson.fromJson(message, AnswerComment.class);

		System.out.println("SubsNewAnswerCommentFilter: As object: " + answerComment);

		boolean banThisAnswerComment = false;

		if (banThisAnswerComment){
			kafkaTemplate.send(env.getProperty("topicBanAnswerComment"), answerComment.getId());
		}
	}

}
