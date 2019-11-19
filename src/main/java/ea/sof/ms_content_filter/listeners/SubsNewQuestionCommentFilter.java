package ea.sof.ms_content_filter.listeners;

import com.google.gson.Gson;
import ea.sof.ms_content_filter.util.FilterBadWords;
import ea.sof.shared.entities.CommentQuestionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(SubsNewQuestionCommentFilter.class);
	private static final FilterBadWords filterBadWords = new FilterBadWords();

	@KafkaListener(topics = "${topicNewQuestionComment}", groupId = "${subsNewQuestionCommentFilter}")
	public void newQuestionComment(String message) {

		LOGGER.info("SubsNewQuestionCommentFilter: New message from topic: " + message);
		System.out.println("SubsNewQuestionCommentFilter: New message from topic: " + message);

		Gson gson = new Gson();
		CommentQuestionEntity questionComment =  gson.fromJson(message, CommentQuestionEntity.class);

		LOGGER.info("SubsNewQuestionCommentFilter: As object: " + questionComment);
		System.out.println("SubsNewQuestionCommentFilter: As object: " + questionComment);

		boolean banThisQuestionComment = filterBadWords.filterBadWords(questionComment.getBody());

		if (banThisQuestionComment){
			kafkaTemplate.send(env.getProperty("topicBanQuestionComment"), questionComment.getId());
			LOGGER.info("SubsNewQuestionCommentFilter: Banned QuestionCommentId=" + questionComment.getId());
		}
	}

}
