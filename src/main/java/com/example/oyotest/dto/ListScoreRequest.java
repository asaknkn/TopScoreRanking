package com.example.oyotest.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class ListScoreRequest {
    private ArrayList<String> players;
    private Date before;
    private Date after;

    public ListScoreRequest() {
        players = new ArrayList<String>();
    }
    public void addList(String player) {
        players.add(player);
    }
}
