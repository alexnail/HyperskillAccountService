package account.model.mapper;

import account.entity.SecurityEvent;
import account.model.EventOutModel;
import org.springframework.stereotype.Component;

@Component
public class SecurityEventMapper {


    public EventOutModel toModel(SecurityEvent entity) {
        return EventOutModel.builder()
                .action(entity.getAction().name())
                .subject(entity.getSubject() == null ? "Anonymous" : entity.getSubject())
                .object(entity.getObject())
                .path(entity.getPath())
                .build();
    }
}
