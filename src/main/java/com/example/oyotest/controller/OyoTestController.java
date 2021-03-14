package com.example.oyotest.controller;

import com.example.oyotest.dto.*;
import com.example.oyotest.service.OyoTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/v1/scores")
public class OyoTestController {

    private final OyoTestService oyoTestService;
    private static final Integer PAGESIZE = 5;

    @Autowired
    public  OyoTestController(OyoTestService oyoTestService) {
        this.oyoTestService = oyoTestService;
    }


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

    @GetMapping("/list")
    public List<GetScoreResponse> listScore(@RequestParam(required = false) MultiValueMap<String,String> player,
                                            @RequestParam(required = false) Optional<String> before,
                                            @RequestParam(required = false) Optional<String> after,
                                            @RequestParam(required = false, defaultValue = "0") Integer offset,
                                            @PageableDefault() Pageable pageable) throws ParseException {

        ListScoreRequest param = new ListScoreRequest();
        pageable = PageRequest.of(offset, PAGESIZE);

        if (player.containsKey("player")) {
            for (String name : player.get("player")) {
                param.addList(name);
            }
        }

        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        before.ifPresent(beforeDate -> {
            try {
                param.setBefore(sdformat.parse(beforeDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        after.ifPresent(afterDate -> {
            try {
                param.setAfter(sdformat.parse(afterDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });

        return oyoTestService.listScore(param, pageable).getContent();
    }

    @GetMapping("/history")
    public GetPlayerHistoryResponse getPlayerHistory(@RequestParam String player){
        return oyoTestService.findPlayerHistory(player);
    }

}
