package app.mathUtils;

import org.apache.commons.lang.ArrayUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class MathUtils {

    public static long factorial(long value) {
        if (value == 0) {
            return 1;
        }
        return LongStream.range(1, value + 1)
                .reduce((v1, v2) -> v1 * v2).getAsLong();
    }

    public static double sum(Function<Long, Double> function, long start, long end) {
        double result = 0;
        for (long i = start; i <= end; i++) {
            result += function.apply(i);
        }
        return result;
    }
    
    public static double mul(Function<Long, Double> function, long start, long end) {
        double result = 1;
        for (long i = start; i <= end; i++) {
            result *= function.apply(i);
        }
        return result;
    }

    public static double[] listToArray(List<Double> list) {
        return ArrayUtils.toPrimitive(list.toArray(Double[]::new));
    }
}
