package com.example.oyotest.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ScoreEntity {
    private String player;
    private Integer score;
    private Date publishedDate;
}
