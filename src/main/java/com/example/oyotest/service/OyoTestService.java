package com.example.oyotest.service;

import com.example.oyotest.dto.*;
import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.repository.OyoTestRepository;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Service
public class OyoTestService {

    private final OyoTestRepository oyoTestRepository;
    @Autowired
    public OyoTestService(OyoTestRepository oyoTestRepository) { this.oyoTestRepository = oyoTestRepository;}

    public CreateScoreResponse createScore(CreateScoreRequest params) {
        ScoreEntity entity = new ScoreEntity();
        entity.setPlayer(params.getPlayer());
        entity.setScore(params.getScore());
        entity.setTime(params.getTime());

        Optional<ScoreEntity> existedEntity = Optional.ofNullable(oyoTestRepository.findByPlayerScoreTime(params));
        if(existedEntity.isPresent()) {
            return new CreateScoreResponse(existedEntity.get().getId());
        }

        entity = oyoTestRepository.create(entity);
        return new CreateScoreResponse(entity.getId());
    }

    public GetScoreResponse findScore(Integer id) {
        ScoreEntity entity = oyoTestRepository.findById(id);
        return new GetScoreResponse(entity.getId(),entity.getPlayer(),entity.getScore(),entity.getTime());
    }

    public DeleteScoreResponse deleteScore(Integer id) {
        oyoTestRepository.delete(id);

        LocalDateTime date = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(date, zone);
        Instant instant = zonedDateTime.toInstant();

        return new DeleteScoreResponse(id, Date.from(instant));
    }

    public Page<GetScoreResponse> listScore(ListScoreRequest param, Pageable pageable) {
        Integer total = oyoTestRepository.cntByEntities(param);

        ArrayList<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();
        if (total > 0) {
            RowBounds rowBounds = new RowBounds((int) pageable.getOffset(), pageable.getPageSize());
            ArrayList<ScoreEntity> entities = oyoTestRepository.findList(param, rowBounds);

            entities.forEach(entity -> {
                getScoreResponses.add(
                        new GetScoreResponse(entity.getId(),entity.getPlayer(),entity.getScore(),entity.getTime())
                );
            });
        }

        return new PageImpl<>(getScoreResponses, pageable, total);
    }
}
