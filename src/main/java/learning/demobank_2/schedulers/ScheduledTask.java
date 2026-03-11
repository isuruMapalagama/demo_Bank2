package learning.demobank_2.schedulers;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class ScheduledTask {

    @Scheduled(fixedRate = 1000)
    public void runEveryFiveSeconds() {
        System.out.println("Fixed Rate Task: " + new Date() );
    }

    @Scheduled(fixedDelay = 2000)
    public void runWithDelay() {
        System.out.println("Run with delay : " + new Date() );
    }

    @Scheduled(initialDelay = 3000, fixedRate = 1500)
    public void runWithInitialDelay() {
        System.out.println("Run with initial delay : " + new Date() );
    }

    @Scheduled(cron = "0 * * * * *")
    public void runWithCron() {
        System.out.println("Run with cron : " + new Date() );
    }
}
