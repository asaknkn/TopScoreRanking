package com.example.oyotest.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CreateScoreRequest {
    private String player;
    private Integer score;
    private Date time;
}
