package com.example.oyotest.service;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.dto.CreateScoreResponse;
import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.repository.OyoTestRepository;
import com.example.oyotest.dto.DeleteScoreResponse;
import com.example.oyotest.dto.GetScoreResponse;
import com.example.oyotest.utility.TestUtility;
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
class OyoTestServiceTest {
    @Autowired
    private OyoTestService oyoTestService;

    @MockBean
    private OyoTestRepository oyoTestRepository;

    private static final Integer ID = 1;
    private static final String INPUTDATESTR = "1984-11-01T00:00:00+09:00";

    @Test
    public void createExistedScoreSuccess() throws ParseException{
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        CreateScoreRequest param = new CreateScoreRequest();
        param.setPlayer("Goku");
        param.setScore(20);
        param.setTime(dateTime);

        ScoreEntity entity = new ScoreEntity();
        entity.setId(ID);

        doReturn(entity).when(oyoTestRepository).findByPlayerScoreTime(any());
        CreateScoreResponse result = oyoTestService.createScore(param);

        assertEquals(entity.getId(), result.getId());
    }

    @Test
    public void createNewScoreSuccess() throws ParseException{
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        CreateScoreRequest param = new CreateScoreRequest();
        param.setPlayer("Goku");
        param.setScore(20);
        param.setTime(dateTime);

        ScoreEntity entity = new ScoreEntity();
        entity.setId(ID);

        doReturn(null).when(oyoTestRepository).findByPlayerScoreTime(any());
        doReturn(entity).when(oyoTestRepository).create(any());
        CreateScoreResponse result = oyoTestService.createScore(param);

        assertEquals(entity.getId(), result.getId());
    }

    @Test
    public void findScoreSuccess() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        ScoreEntity entity = new ScoreEntity();
        entity.setPlayer("Goku");
        entity.setScore(10);
        entity.setTime(dateTime);

        doReturn(entity).when(oyoTestRepository).findById(any());
        GetScoreResponse result = oyoTestService.findScore(ID);

        assertEquals(entity.getPlayer(), result.getPlayer());
        assertEquals(entity.getScore(), result.getScore());
        assertEquals(entity.getTime(), result.getTime());
    }

    @Test
    public void deleteScoreSuccess() {
        doNothing().when(oyoTestRepository).delete(any());
        DeleteScoreResponse result = oyoTestService.deleteScore(ID);

        verify(oyoTestRepository).delete(ID);
        assertEquals(ID, result.getId());
    }

}