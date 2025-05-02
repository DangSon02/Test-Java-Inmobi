package burundi.ilucky.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FreePlayScheduler {

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduleDailyBonusPlays() {
        userService.addDailyBonusPlays();
    }
}
