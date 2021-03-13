package com.example.oyotest.dto;


import lombok.Data;

@Data
public class CreateScoreResponse {
    private Integer id;

    public CreateScoreResponse(){}
    public CreateScoreResponse(Integer id) {
        this.id = id;
    }
}
