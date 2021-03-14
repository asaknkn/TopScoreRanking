package com.example.oyotest.repository;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.dto.ListScoreRequest;
import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.mapper.ScoresMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class OyoTestRepository {
    private final ScoresMapper scoresMapper;
    @Autowired
    public OyoTestRepository(ScoresMapper scoresMapper) { this.scoresMapper = scoresMapper;}

    public ScoreEntity create(ScoreEntity entity) {
         scoresMapper.create(entity);
         return entity;
    }

    public ScoreEntity findByPlayerScoreTime(CreateScoreRequest param) {
        return scoresMapper.findByPlayerScoreTime(param);
    }

    public ScoreEntity findById(Integer id ) {
        return scoresMapper.findById(id);
    }

    public void delete(Integer id) {
        scoresMapper.deleteById(id);
    }

    public Integer cntByEntities(ListScoreRequest param) {
        return scoresMapper.cntByEntities(param);
    }

    public ArrayList<ScoreEntity> findList(ListScoreRequest param, RowBounds rowBounds) {
        return scoresMapper.findList(param, rowBounds);
    }

    public ArrayList<ScoreEntity> findByPlayer(String player) {
        return scoresMapper.findByPlayer(player);
    }
}
