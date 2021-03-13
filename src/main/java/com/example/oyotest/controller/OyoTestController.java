package com.example.oyotest.controller;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.dto.CreateScoreResponse;
import com.example.oyotest.dto.DeleteScoreResponse;
import com.example.oyotest.dto.GetScoreResponse;
import com.example.oyotest.service.OyoTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/v1/scores")
public class OyoTestController {

    private final OyoTestService oyoTestService;

    @Autowired
    public  OyoTestController(OyoTestService oyoTestService) {this.oyoTestService = oyoTestService;}


    @PostMapping("")
    public CreateScoreResponse createScore(@RequestBody CreateScoreRequest params) {
        return oyoTestService.createScore(params);
    }

    @GetMapping("/{id}")
    public GetScoreResponse getScore(@PathVariable("id") Integer id) {
        return oyoTestService.findScore(id);
    }

    @DeleteMapping("/{id}")
    public DeleteScoreResponse deleteScore(@PathVariable("id") Integer id) {
        return oyoTestService.deleteScore(id);
    }
}
