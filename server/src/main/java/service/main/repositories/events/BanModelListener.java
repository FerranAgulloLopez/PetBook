package service.main.repositories.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import service.main.entity.Ban;
import service.main.services.SequenceGeneratorService;

@Component
public class BanModelListener extends AbstractMongoEventListener<Ban> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public BanModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Ban> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Ban.SEQUENCE_NAME));
        }
    }
}
