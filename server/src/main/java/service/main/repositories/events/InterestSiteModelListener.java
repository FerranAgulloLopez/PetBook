package service.main.repositories.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import service.main.entity.InterestSite;
import service.main.services.SequenceGeneratorService;

@Component
public class InterestSiteModelListener extends AbstractMongoEventListener<InterestSite> {

    private SequenceGeneratorService sequenceGenerator;

    @Autowired
    public InterestSiteModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<InterestSite> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(InterestSite.SEQUENCE_NAME));
        }
    }
}
