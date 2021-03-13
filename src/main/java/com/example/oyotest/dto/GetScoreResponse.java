package com.example.oyotest.response;


import lombok.Data;
import java.util.Date;

@Data
public class GetScoreResponse {
    private Integer id;
    private String player;
    private Integer score;
    private Date time;

    public GetScoreResponse(Integer id, String player, Integer score, Date time) {
        this.id = id;
        this.player = player;
        this.score = score;
        this.time = time;
    }
}