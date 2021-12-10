package app.main;

import app.empiric.QueueState;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static app.main.Statistics.requestsInQueueTime;


@Component
public class RequestGenerator extends Thread {

    @Autowired
    private Queue queue;

    @Autowired
    private DistributeGenerator distributeGenerator;

    @Autowired
    private QueueState queueState;

    @SneakyThrows
    @Override
    public void run() {

        int totalCount = 1;

        while (true) {
            String reqName = "R" + totalCount;
            try {
                Request req = new Request(queue, queueState, reqName, distributeGenerator.generateWaitingTime());
                Statistics.appearedReqCount++;
                long thisTime = System.currentTimeMillis();

                int sizeBefore = queue.getQueue().size();
                queue.getQueue().add(req);
                int sizeAfter = queue.getQueue().size();

                if (sizeAfter - sizeBefore == 1) {
                    synchronized (queueState) {
                        queueState.reqInQueue = sizeAfter;
                        requestsInQueueTime.merge(
                                sizeBefore,
                                thisTime - queueState.getStartTime(),
                                Long::sum);
                        queueState.setStartTime(thisTime);
                    }
                }
                System.out.println(reqName + " came");
            }
            catch (IllegalStateException ex) {
                System.out.println(reqName + " rejected (queue is full)");
                Statistics.rejectedByPlaceReqCount++;
            }
            try {
                sleep(distributeGenerator.generateNextReqTime());
            } catch (InterruptedException ex) {
                return;
            }
            totalCount++;
        }
    }
}
