package account.service;

import account.model.EventOutModel;
import account.model.mapper.SecurityEventMapper;
import account.repository.SecurityEventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityEventServiceImpl implements SecurityEventService {

    private final SecurityEventRepository eventRepository;

    private final SecurityEventMapper eventMapper;

    public SecurityEventServiceImpl(SecurityEventRepository eventRepository, SecurityEventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public List<EventOutModel> getEvents() {
        List<EventOutModel> result = new ArrayList<>();
        eventRepository.findAll().forEach(e-> result.add(eventMapper.toModel(e)));
        return result;
    }
}
