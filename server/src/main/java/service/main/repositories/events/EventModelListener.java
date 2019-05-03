package service.main.repositories.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import service.main.entity.Event;
import service.main.services.SequenceGeneratorService;

@Component
public class EventModelListener extends AbstractMongoEventListener<Event> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public EventModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Event> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Event.SEQUENCE_NAME));
        }
    }
}
