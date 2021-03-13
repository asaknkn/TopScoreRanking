package com.example.oyotest.controller;

import com.example.oyotest.response.GetScoreResponse;
import com.example.oyotest.service.OyoTestService;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = OyoTestController.class)
class OyoTestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OyoTestService oyoTestService;

    @Test
    public void getScoreSuccess() throws Exception {
        String inpDateStr = "1984-11-01 00:00:00";
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateTime = sdformat.parse(inpDateStr);

        GetScoreResponse getScoreResponse = new GetScoreResponse("Goku",20,dateTime);
        doReturn(getScoreResponse).when(oyoTestService).findScore(any());
        MvcResult result = mockMvc.perform(
                get("/scores/find/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String player = JsonPath.parse(response).read("$.player");
        Integer score = JsonPath.parse(response).read("$.score");
        String publishedDate = JsonPath.parse(response).read("$.published_date");

        assertEquals(player, "Goku");
        assertEquals(score, 20);
        assertEquals(publishedDate, "1984-11-01T00:00:00+09:00");
    }
}