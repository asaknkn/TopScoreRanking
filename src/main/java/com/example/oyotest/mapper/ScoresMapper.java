package com.example.oyotest.mapper;

import com.example.oyotest.entity.ScoreEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface ScoresMapper {
    ScoreEntity findById(Integer id);
    void deleteById(Integer id);
}
