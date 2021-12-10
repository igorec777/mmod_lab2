package app.main;

import app.empiric.QueueState;
import app.mathUtils.Histogram;
import app.mathUtils.Properties;
import config.BeansConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static app.main.Statistics.*;

@Component
public class Main {

    @Autowired
    private QueueState queueState;


    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeansConfig.class);

        RequestGenerator requestGenerator = context.getBean(RequestGenerator.class);
        List<ServiceChannel> channels = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            channels.add(context.getBean(ServiceChannel.class));
            channels.get(i).start();
        }

        context.getBean(Main.class).initStates();
        requestGenerator.start();

        Thread.sleep(WORK_TIME);

        requestGenerator.interrupt();
        channels.forEach(Thread::interrupt);

        ShowStatisticsValues();
        CalculateTheoreticalValues();

        List<Double[]> resultData = new ArrayList<>();
        resultData.add(finalProbPkAll(finalProbP0()).toArray(Double[]::new));
        new Histogram(new Properties(0, "Probability that k flows are busy",
                new String[]{"Theoretical probabilities", "Empirical probabilities"}, resultData));

        resultData.clear();
        resultData.add(finalProbWithQueuePni(finalProbPn(finalProbP0())));
        resultData.add(finalTimeWithQueuePni());
        new Histogram(new Properties(1, "Probability that n requests in queue",
                new String[]{"Theoretical probabilities", "Empirical probabilities"}, resultData));
    }

    public void initStates() {
        queueState.setStartTime(System.currentTimeMillis());
    }
}