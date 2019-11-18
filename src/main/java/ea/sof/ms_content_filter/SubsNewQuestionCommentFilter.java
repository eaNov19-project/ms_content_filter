package ea.sof.ms_content_filter;

import com.google.gson.Gson;
import ea.sof.shared.models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class SubsNewQuestionCommentFilter {
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	private Environment env;

	@KafkaListener(topics = "${topicNewQuestionComment}", groupId = "${subsNewQuestionCommentFilter}")
	public void newQuestionComment(String message) {

		System.out.println("SubsNewQuestionCommentFilter: New message from topic: " + message);

		Gson gson = new Gson();
		QuestionComment questionComment =  gson.fromJson(message, QuestionComment.class);

		System.out.println("SubsNewQuestionCommentFilter: As object: " + questionComment);

		boolean banThisQuestionComment = false;

		if (banThisQuestionComment){
			kafkaTemplate.send(env.getProperty("topicBanQuestionComment"), questionComment.getId());
		}
	}

}
