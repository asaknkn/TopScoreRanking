package com.example.oyotest.mapper;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.dto.ListScoreRequest;
import com.example.oyotest.entity.ScoreEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Mapper
@Component
public interface ScoresMapper {
    void create(ScoreEntity entity);
    ScoreEntity findByPlayerScoreTime(CreateScoreRequest param);
    ScoreEntity findById(Integer id);
    void deleteById(Integer id);
    Integer cntByEntities(ListScoreRequest param);
    ArrayList<ScoreEntity> findList(ListScoreRequest param, RowBounds rowBounds);
}
