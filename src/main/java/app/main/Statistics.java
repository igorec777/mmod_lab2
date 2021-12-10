package app.main;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static app.mathUtils.MathUtils.*;

@Getter
@AllArgsConstructor
public class Statistics {

    public static final int WORK_TIME = 4000;

    public static final int n = 5;
    public static final int m = 4;

    public static final double lambda = 0.05;
    public static final double mu = 0.01;
    public static final double v = 0.01;
    public static final double ro = lambda / mu;
    public static final double betta = v / mu;

    public static int appearedReqCount;
    public static int rejectedByPlaceReqCount;
    public static int rejectedByTimeCount;

    public static ConcurrentHashMap<String, Integer> servedByChannel = new ConcurrentHashMap<>(n);
    public static ConcurrentHashMap<Integer, Long> requestsInQueueTime = new ConcurrentHashMap<>(m);

    public static void ShowStatisticsValues() {
        System.out.println("Total requests generated: " + appearedReqCount);
        System.out.println("Rejected (wasn't enough space in queue): " + rejectedByPlaceReqCount);
        System.out.println("Rejected (didn't wait): " + rejectedByTimeCount);
        channelsInfo();
    }

    public static void CalculateTheoreticalValues() {
        double p0 = finalProbP0();
        double pn = finalProbPn(p0);
        double[] pk = listToArray(finalProbPkAll(p0));
        Double[] pni = finalProbWithQueuePni(pn);
        double probOfRejection = probOfRejection(pn);
        double probOfQueueAppeared = probOfQueueAppeared(pn);
        double averageRequestsInQueue = averageRequestsInQueue(pn);
        System.out.println("Final theory probability of states:");
        System.out.println("pk (prob that k flows are busy): " + Arrays.toString(pk));
        System.out.println("pn (prob that n requests in queue): " + Arrays.toString(pni));
        System.out.println("Probability of rejection: " + probOfRejection);
        System.out.println("Average number of requests in queue: " + averageRequestsInQueue);
        System.out.println("Average number of requests in system: " + averageRequestsInSystem());
        System.out.println("Probability of queue formation: " + probOfQueueAppeared);
        System.out.println("Average waiting time in queue: " +
                averageWaitingTimeInQueue(1 - probOfRejection) + " ms");
        System.out.println("Relative traffic: " + (1 - probOfRejection));
        System.out.println("Absolute traffic: " + lambda * (1 - probOfRejection));
    }

    private static void channelsInfo() {
        for(Map.Entry<String, Integer> entry : servedByChannel.entrySet()) {
            System.out.println("Channel " + entry.getKey() + " total served: " + entry.getValue());
        }
    }

    public static double finalProbP0() {
        double tempSum1 = sum(i -> Math.pow(ro, i) / factorial(i), 0, n);
        double tempSum2 = sum(i -> Math.pow(ro, i) / mul(l -> (n + l * betta), 1, i), 1, m);
        return Math.pow(tempSum2 * (Math.pow(ro, n) / factorial(n)) + tempSum1, -1);
    }

    public static double finalProbPn(double p0) {
        return Math.pow(ro, n) / factorial(n) * p0;
    }

    public static List<Double> finalProbPkAll(double p0) {
        List<Double> result = new ArrayList<>(n);
        result.add(p0);
        for (int i = 0; i < n; i++) {
            result.add(finalProbPk(p0, i+1));
        }
        return result;
    }

    public static double finalProbPk(double p0, int k) {
        return Math.pow(ro, k) / factorial(k) * p0;
    }

    private static double probOfRejection(double pn) {
        double tempMul = mul(i -> (n + i * betta), 1, m);
        return pn * Math.pow(ro, m) / tempMul;
    }

    private static double probOfQueueAppeared(double pn) {
        double tempSum = sum(i -> Math.pow(ro, i) / mul(l -> (n + l*betta), 1, i), 1, m - 1);
        return (1 + tempSum) * pn;
    }

    private static double averageRequestsInQueue(double pn) {
        double tempSum = sum(i -> i * Math.pow(ro, i) / mul(l -> (n + l*betta), 1, i), 1, m);
        return pn * tempSum;
    }
    private static double averageRequestsInSystem() {
        double tempSum1 = sum(k -> k * finalProbPk(finalProbP0(), k.intValue()), 1, n);
        double tempSum2 = sum(i -> n * finalProbPk(finalProbP0(), n + i.intValue()), 1, m);
        return tempSum1 + tempSum2;
    }

    private static double averageWaitingTimeInQueue(double relativeTraffic) {
        return relativeTraffic / mu + (1.0 / v);
    }

    public static Double[] finalProbWithQueuePni(double pn) {
        Double[] result = new Double[n];
        for (int i = 0; i < result.length; i++) {
            double tempMul = mul(l -> (l + n * betta), 1, i+1);
            result[i] = pn * Math.pow(ro, i+1) / tempMul;
        }
        return result;
    }

    public static Double[] finalTimeWithQueuePni() {
        Double[] result = new Double[requestsInQueueTime.size()];
        int i = 0;
        requestsInQueueTime.remove(1);
        for(Map.Entry<Integer, Long> entry : requestsInQueueTime.entrySet()) {
            result[i] = (double) (entry.getValue() / (float) WORK_TIME);
            i++;
        }
        return result;
    }
}
