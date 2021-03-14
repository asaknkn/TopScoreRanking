package com.example.oyotest.dto;

import lombok.Data;

import java.util.Date;

@Data
public class PlayerScore {
    private Integer score;
    private Date time;

    public PlayerScore(){}
    public PlayerScore(Integer score, Date time) {
        this.score = score;
        this.time = time;
    }
}
