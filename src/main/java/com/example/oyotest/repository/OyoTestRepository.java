package com.example.oyotest.repository;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.mapper.ScoresMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OyoTestRepository {
    private final ScoresMapper scoresMapper;
    @Autowired
    public OyoTestRepository(ScoresMapper scoresMapper) { this.scoresMapper = scoresMapper;}

    public ScoreEntity create(ScoreEntity entity) {
         scoresMapper.create(entity);
         return entity;
    }

    public ScoreEntity findByPlayer(String player) {
        return scoresMapper.findByPlayer(player);
    }

    public ScoreEntity findById(Integer id ) {
        return scoresMapper.findById(id);
    }

    public void delete(Integer id) {
        scoresMapper.deleteById(id);
    }
}
