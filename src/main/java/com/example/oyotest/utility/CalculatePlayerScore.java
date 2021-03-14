package com.example.oyotest.utility;

import com.example.oyotest.entity.ScoreEntity;

import java.util.ArrayList;

public class CalculatePlayerScore {
    public static Integer getTopScore(ArrayList<ScoreEntity> scoreEntities) {
        Integer topScore = scoreEntities.get(0).getScore();

        for(ScoreEntity entity : scoreEntities){
            topScore = Math.max(topScore, entity.getScore());
        }

        return topScore;
    }

    public static Integer getLowScore(ArrayList<ScoreEntity> scoreEntities) {
        Integer lowScore = scoreEntities.get(0).getScore();

        for(ScoreEntity entity : scoreEntities){
            lowScore = Math.min(lowScore, entity.getScore());
        }

        return lowScore;
    }

    public static Float getAverageScore(ArrayList<ScoreEntity> scoreEntities) {
        Integer sum = 0;

        for(ScoreEntity entity : scoreEntities){
            sum += entity.getScore();
        }

        Float aveScore;
        if (scoreEntities.size() == 0) {
            aveScore = 0f;
        } else {
            aveScore = Float.valueOf(sum / scoreEntities.size());
        }

        return aveScore;
    }
}
