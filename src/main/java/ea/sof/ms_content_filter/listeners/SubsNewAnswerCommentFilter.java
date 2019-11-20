package ea.sof.ms_content_filter.listeners;

import com.google.gson.Gson;
import ea.sof.ms_content_filter.util.FilterBadWords;
import ea.sof.shared.entities.CommentAnswerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static final Logger LOGGER = LoggerFactory.getLogger(SubsNewAnswerCommentFilter.class);
	private static final FilterBadWords filterBadWords = new FilterBadWords();

	@KafkaListener(topics = "${topicNewAnswerComment}", groupId = "${subsNewAnswerCommentFilter}")
	public void newAnswerComment(String message) {

		LOGGER.info("SubsNewAnswerCommentFilter: New message from topic: " + message);

		Gson gson = new Gson();
		CommentAnswerEntity answerComment =  gson.fromJson(message, CommentAnswerEntity.class);

//		LOGGER.info("SubsNewAnswerCommentFilter: As object: " + answerComment);

		boolean banThisAnswerComment = filterBadWords.filterBadWords(answerComment.getBody());

		if (banThisAnswerComment){
			kafkaTemplate.send(env.getProperty("topicBanAnswerComment"), answerComment.getId());
			LOGGER.info("SubsNewAnswerCommentFilter: Banned answerCommentId=" + answerComment.getId());
		}
	}

}
