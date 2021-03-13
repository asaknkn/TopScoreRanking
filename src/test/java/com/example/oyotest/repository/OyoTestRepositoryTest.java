package com.example.oyotest.repository;

import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.mapper.ScoresMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Test
    public void findSuccess() throws ParseException {
        Integer id = 1;

        String inpDateStr = "1984-11-01 00:00:00";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateTime = sdformat.parse(inpDateStr);

        ScoreEntity entity = new ScoreEntity();
        entity.setPlayer("Goku");
        entity.setScore(10);
        entity.setPublishedDate(dateTime);

        doReturn(entity).when(scoresMapper).findById(any());
        ScoreEntity result = oyoTestRepository.find(id);

        verify(scoresMapper).findById(id);
        assertEquals(result.getPlayer(),entity.getPlayer());
        assertEquals(result.getScore(),entity.getScore());
        assertEquals(result.getPublishedDate(),entity.getPublishedDate());
    }



}