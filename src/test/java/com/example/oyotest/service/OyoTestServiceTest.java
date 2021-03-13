package com.example.oyotest.service;

import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.repository.OyoTestRepository;
import com.example.oyotest.response.GetScoreResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OyoTestServiceTest {
    @Autowired
    private OyoTestService oyoTestService;

    @MockBean
    private OyoTestRepository oyoTestRepository;

    @Test
    public void findScoreSuccess() throws ParseException {
        String inpDateStr = "1984-11-01 00:00:00";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateTime = sdformat.parse(inpDateStr);

        ScoreEntity entity = new ScoreEntity();
        entity.setPlayer("Goku");
        entity.setScore(10);
        entity.setPublishedDate(dateTime);

        doReturn(entity).when(oyoTestRepository).find(any());
        GetScoreResponse score = oyoTestService.findScore(1);
        assertEquals(score.getPlayer(), "Goku");
    }

}