package com.wiam.lms.service.custom.progression;

import com.wiam.lms.domain.Progression;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Service;

@Service
public class ProgressionCustomService {

    public String calculateProgressLabel(List<Progression> progressionList) {
        if (progressionList == null) return "NA";
        AtomicReference<String> label = new AtomicReference<>("");
        progressionList.forEach(progression -> {
            int sumScores = progression.getAdaeScore() + progression.getHifdScore() + progression.getTajweedScore();
            float avgScore = sumScores / 3;
            if (avgScore < 5) label.set("WEAK");
            if (avgScore < 8 && avgScore > 5) label.set("AVG");
            if (avgScore < 12 && avgScore > 8) label.set("GOOD");
            if (avgScore > 12) label.set("VERY_GOOD");
        });
        return label.get();
    }
}
