package ea.sof.ms_content_filter.listeners;

import com.google.gson.Gson;
import ea.sof.ms_content_filter.util.FilterBadWords;
import ea.sof.shared.entities.CommentAnswerEntity;
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
	private FilterBadWords filterBadWords;

	@KafkaListener(topics = "${topicNewAnswerComment}", groupId = "${subsNewAnswerCommentFilter}")
	public void newAnswerComment(String message) {

		System.out.println("SubsNewAnswerCommentFilter: New message from topic: " + message);

		Gson gson = new Gson();
		CommentAnswerEntity answerComment =  gson.fromJson(message, CommentAnswerEntity.class);

		System.out.println("SubsNewAnswerCommentFilter: As object: " + answerComment);

		boolean banThisAnswerComment = filterBadWords.filterBadWords(answerComment.getBody());

		if (banThisAnswerComment){
			kafkaTemplate.send(env.getProperty("topicBanAnswerComment"), answerComment.getId());
		}
	}

}
