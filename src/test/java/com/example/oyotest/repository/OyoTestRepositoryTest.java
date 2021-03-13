package com.example.oyotest.repository;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.mapper.ScoresMapper;
import com.example.oyotest.utility.TestUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OyoTestRepositoryTest {
    @Autowired
    private OyoTestRepository oyoTestRepository;

    @MockBean
    private ScoresMapper scoresMapper;

    private static final Integer ID = 1;
    private static final String INPUTDATESTR = "1984-11-01T00:00:00+09:00";
    static ScoreEntity entity;
    static CreateScoreRequest param;

    @BeforeAll
    static void before() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);
        entity = new ScoreEntity();
        entity.setId(ID);
        entity.setPlayer("Goku");
        entity.setScore(10);
        entity.setTime(dateTime);

        param = new CreateScoreRequest();
        param.setPlayer("Goku");
        param.setScore(20);
        param.setTime(dateTime);
    }

    @Test void createSuccess() throws ParseException {

        doNothing().when(scoresMapper).create(any());

        ScoreEntity result = oyoTestRepository.create(entity);

        verify(scoresMapper).create(entity);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getPlayer(), result.getPlayer());
        assertEquals(entity.getScore(), result.getScore());
        assertEquals(entity.getTime(), result.getTime());
    }

    @Test void findByPlayerScoreTime() throws ParseException {

        doReturn(entity).when(scoresMapper).findByPlayerScoreTime(any());

        ScoreEntity result = oyoTestRepository.findByPlayerScoreTime(param);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getPlayer(), result.getPlayer());
        assertEquals(entity.getScore(), result.getScore());
        assertEquals(entity.getTime(), result.getTime());
    }

    @Test
    public void findByIdSuccess() throws ParseException {

        doReturn(entity).when(scoresMapper).findById(any());

        ScoreEntity result = oyoTestRepository.findById(ID);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getPlayer(), result.getPlayer());
        assertEquals(entity.getScore(), result.getScore());
        assertEquals(entity.getTime(), result.getTime());
    }

    @Test
    public void deleteSuccess() {
        doNothing().when(scoresMapper).deleteById(any());

        oyoTestRepository.delete(ID);

        verify(scoresMapper).deleteById(ID);
    }



}