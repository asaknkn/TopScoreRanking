package com.example.oyotest.controller;

import com.example.oyotest.response.DeleteScoreResponse;
import com.example.oyotest.response.GetScoreResponse;
import com.example.oyotest.service.OyoTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/scores")
public class OyoTestController {

    private final OyoTestService oyoTestService;

    @Autowired
    public  OyoTestController(OyoTestService oyoTestService) {this.oyoTestService = oyoTestService;}

    @GetMapping("/find/{id}")
    public GetScoreResponse getScore(@PathVariable("id") Integer id) {

        GetScoreResponse res = oyoTestService.findScore(id);
        return res;
    }

    @DeleteMapping("delete/{id}")
    public DeleteScoreResponse deleteScore(@PathVariable("id") Integer id) {
        DeleteScoreResponse res = oyoTestService.deleteScore(id);
        return  res;
    }
}
