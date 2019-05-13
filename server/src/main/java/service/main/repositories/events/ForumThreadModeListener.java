package service.main.repositories.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import service.main.entity.ForumThread;
import service.main.services.SequenceGeneratorService;

@Component
public class ForumThreadModeListener extends AbstractMongoEventListener<ForumThread> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public ForumThreadModeListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<ForumThread> comment) {
        if (comment.getSource().getId() < 1) {
            comment.getSource().setId(sequenceGenerator.generateSequence(ForumThread.SEQUENCE_NAME));
        }
    }
}