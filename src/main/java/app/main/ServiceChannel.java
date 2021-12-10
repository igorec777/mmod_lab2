package app.main;

import app.empiric.QueueState;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static app.main.Statistics.*;

@Component
@Scope("prototype")
public class ServiceChannel extends Thread {

    @Autowired
    private Queue queue;

    @Autowired
    private QueueState queueState;

    @Autowired
    private DistributeGenerator distributeGenerator;

    @SneakyThrows
    @Override
    public void run() {

        Request request;
        int oldSize;

        while (true) {
            synchronized (queue) {
                request = queue.getQueue().poll();
                oldSize = queue.getQueue().size() + 1;
            }
            if (request != null) {
                System.out.println(Thread.currentThread().getName() + " take " + request.getReqName());
                long thisTime = System.currentTimeMillis();
                synchronized (queueState) {
                    queueState.reqInQueue = oldSize - 1;
                    requestsInQueueTime.merge(
                            oldSize,
                            thisTime - queueState.getStartTime(),
                            Long::sum);
                    queueState.setStartTime(thisTime);
                }
                try {
                    sleep(distributeGenerator.generateProcessingTime());
                } catch (InterruptedException ex) {
                    this.interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " processed " + request.getReqName());
                servedByChannel.merge(Thread.currentThread().getName(), 1, Integer::sum);
            }
        }
    }
}
