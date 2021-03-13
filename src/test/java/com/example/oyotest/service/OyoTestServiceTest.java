package com.example.oyotest.service;

import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.repository.OyoTestRepository;
import com.example.oyotest.response.DeleteScoreResponse;
import com.example.oyotest.response.GetScoreResponse;
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

    @Test
    public void findScoreSuccess() throws ParseException {
        String inpDateStr = "1984-11-01 00:00:00";
        Date dateTime = TestUtility.changeStrToDate(inpDateStr);

        ScoreEntity entity = new ScoreEntity();
        entity.setPlayer("Goku");
        entity.setScore(10);
        entity.setPublishedDate(dateTime);

        doReturn(entity).when(oyoTestRepository).find(any());
        GetScoreResponse result = oyoTestService.findScore(ID);
        assertEquals(result.getPlayer(), "Goku");
    }

    @Test
    public void deleteScoreSuccess() {
        doNothing().when(oyoTestRepository).delete(any());
        DeleteScoreResponse result = oyoTestService.deleteScore(ID);

        verify(oyoTestRepository).delete(ID);
        assertEquals(result.getId(), ID);
    }

}