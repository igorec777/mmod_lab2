package app.mathUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Properties {
    private final String applicationTitle = "";
    private final String chartTitle = "Final probabilities states difference";
    private final int index;
    private final String category;
    private final String[] rowKey;
    private final List<Double[]> resultData;
}
