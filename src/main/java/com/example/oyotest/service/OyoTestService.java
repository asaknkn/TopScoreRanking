package com.example.oyotest.service;

import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.repository.OyoTestRepository;
import com.example.oyotest.response.DeleteScoreResponse;
import com.example.oyotest.response.GetScoreResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class OyoTestService {

    private final OyoTestRepository oyoTestRepository;
    @Autowired
    public OyoTestService(OyoTestRepository oyoTestRepository) { this.oyoTestRepository = oyoTestRepository;}

    public GetScoreResponse findScore(Integer id) {
        ScoreEntity entity = oyoTestRepository.find(id);
        return new GetScoreResponse(entity.getPlayer(),entity.getScore(),entity.getPublishedDate());
    }

    public DeleteScoreResponse deleteScore(Integer id) {
        oyoTestRepository.delete(id);

        LocalDateTime date = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, zone);
        Instant instant = zonedDateTime.toInstant();

        return new DeleteScoreResponse(id, Date.from(instant));
    }
}
