package account.controller;

import account.model.EventOutModel;
import account.service.SecurityEventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    private final SecurityEventService securityEventService;

    public SecurityController(SecurityEventService securityEventService) {
        this.securityEventService = securityEventService;
    }

    @GetMapping("/events/")
    List<EventOutModel> getEvents() {
        return securityEventService.getEvents();
    }
}
