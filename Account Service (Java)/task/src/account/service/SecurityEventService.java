package account.service;

import account.model.EventOutModel;

import java.util.List;

public interface SecurityEventService {
    List<EventOutModel> getEvents();
}
