package app.empiric;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Getter
@Setter
@Component
public class QueueState {
    private Long startTime;
    public int reqInQueue;
}
