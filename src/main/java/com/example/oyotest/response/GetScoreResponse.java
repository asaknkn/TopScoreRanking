package com.example.oyotest.response;


import lombok.Data;
import java.util.Date;

@Data
public class GetScoreResponse {
    private String player;
    private Integer score;
    private Date published_date;

    public GetScoreResponse(String player, Integer score, Date published_date) {
        this.player = player;
        this.score = score;
        this.published_date = published_date;
    }
}
