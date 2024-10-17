package account.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventOutModel {
    private String action;
    private String subject;
    private String object;
    private String path;
}
