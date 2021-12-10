package app.main;

import app.empiric.QueueState;
import lombok.Getter;
import lombok.SneakyThrows;

import static app.main.Statistics.requestsInQueueTime;


@Getter
public class Request extends Thread {

    private final String reqName;
    private final long waitTime;
    private final Queue queue;
    private final QueueState queueState;

    public Request(Queue queue, QueueState queueState, String reqName, long waitTime) {
        this.queue = queue;
        this.queueState = queueState;
        this.reqName = reqName;
        this.waitTime = waitTime;

        this.start();
    }

    @SneakyThrows
    @Override
    public void run() {
        sleep(waitTime);
        int oldSize = queue.getQueue().size();
        long thisTime;
        if (queue.getQueue().remove(this)) {
            thisTime = System.currentTimeMillis();
            synchronized (queueState) {
                queueState.reqInQueue = oldSize - 1;
                requestsInQueueTime.merge(
                        oldSize,
                        thisTime - queueState.getStartTime(),
                        Long::sum);
                queueState.setStartTime(thisTime);
            }
            System.out.println(this.reqName + " left (time is out)");
            Statistics.rejectedByTimeCount++;
        }
    }
    @Override
    public String toString() {
        return "Request{" +
                "name='" + reqName + '\'' +
                '}';
    }
}
