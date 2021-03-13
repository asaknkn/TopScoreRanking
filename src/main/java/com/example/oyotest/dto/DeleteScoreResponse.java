package com.example.oyotest.dto;

import lombok.Data;

import java.util.Date;

@Data
public class DeleteScoreResponse {
    private Integer id;
    private Date deleted_date;

    public DeleteScoreResponse(Integer id, Date deleted_date) {
        this.id = id;
        this.deleted_date = deleted_date;
    }
}
