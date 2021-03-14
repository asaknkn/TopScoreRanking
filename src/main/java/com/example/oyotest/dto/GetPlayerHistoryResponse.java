package com.example.oyotest.dto;


import lombok.Data;

import java.util.ArrayList;

@Data
public class GetPlayerHistoryResponse {
    private String name;
    private ArrayList<PlayerScore> scores;
    private Integer top_score;
    private Integer low_score;
    private Float average_score;

    public GetPlayerHistoryResponse(){}

}
