package com.example.oyotest.utility;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.entity.ScoreEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CalculatePlayerScoreTest {

    static ArrayList<ScoreEntity> scoreEntities;

    @BeforeAll
    static void before() throws ParseException {

        ScoreEntity entity1 = new ScoreEntity();
        entity1.setScore(10);
        ScoreEntity entity2 = new ScoreEntity();
        entity2.setScore(20);
        ScoreEntity entity3 = new ScoreEntity();
        entity3.setScore(30);

        scoreEntities = new ArrayList<ScoreEntity>();
        scoreEntities.add(entity1);
        scoreEntities.add(entity2);
        scoreEntities.add(entity3);
    }

    @Test
    public void getTopScore() {
        Integer result = CalculatePlayerScore.getTopScore(scoreEntities);
        assertEquals(30, result);
    }

    @Test
    public void getLowScore() {
        Integer result = CalculatePlayerScore.getLowScore(scoreEntities);
        assertEquals(10, result);
    }

    @Test
    public void getAverageScore() {
        Float result = CalculatePlayerScore.getAverageScore(scoreEntities);
        assertEquals(20l, result);
    }
}