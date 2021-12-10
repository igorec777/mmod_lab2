package config;

import app.empiric.QueueState;
import app.main.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;

import static app.main.Statistics.m;


@Configuration
@ComponentScan({"app.main", "app.empiric"})
public class BeansConfig {
    @Bean
    public Queue queue() {
        return new Queue(m, new LinkedBlockingQueue<>(m));
    }

    @Bean
    public QueueState queueState() {
        return new QueueState(0L, 0);
    }
}
