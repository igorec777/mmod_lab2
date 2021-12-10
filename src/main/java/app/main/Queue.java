package app.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
@AllArgsConstructor
@Getter
public class Queue {
    private int size;
    private BlockingQueue<Request> queue;
}
