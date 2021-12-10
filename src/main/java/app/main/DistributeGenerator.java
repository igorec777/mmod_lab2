package app.main;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

import static app.main.Statistics.*;


@Component
public class DistributeGenerator {

    public long generateNextReqTime() {
        double time = exponentialDistribution(lambda);
        return (long) time;
    }

    public long generateProcessingTime() {
        double result = exponentialDistribution(mu);
        return (long) (result);
    }

    public long generateWaitingTime() {
        double result = exponentialDistribution(v);
        return (long) (result);
    }

    private double exponentialDistribution(double param) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        return Math.log(1 - rand.nextDouble())/(-param);
    }
}
