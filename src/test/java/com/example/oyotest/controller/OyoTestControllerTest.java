package com.example.oyotest.controller;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.dto.CreateScoreResponse;
import com.example.oyotest.dto.DeleteScoreResponse;
import com.example.oyotest.dto.GetScoreResponse;
import com.example.oyotest.service.OyoTestService;
import com.example.oyotest.utility.TestUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = OyoTestController.class)
class OyoTestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OyoTestService oyoTestService;

    private static final Integer ID = 1;
    private static final String INPUTDATESTR = "1984-11-01T00:00:00+09:00";

    @Test
    public void createScoreSuccess() throws Exception {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        CreateScoreRequest param = new CreateScoreRequest();
        param.setPlayer("Goku");
        param.setScore(20);
        param.setTime(dateTime);

        ObjectMapper mapper = new ObjectMapper();
        String jsonParam = mapper.writeValueAsString(param);

        CreateScoreResponse createScoreResponse = new CreateScoreResponse(ID);

        doReturn(createScoreResponse).when(oyoTestService).createScore(any());

        MvcResult result = mockMvc.perform(
                post("/v1/scores/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParam))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");

        assertEquals(createScoreResponse.getId(), id);
    }

    @Test
    public void getScoreSuccess() throws Exception {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        GetScoreResponse getScoreResponse = new GetScoreResponse(ID,"Goku",20,dateTime);
        doReturn(getScoreResponse).when(oyoTestService).findScore(any());
        MvcResult result = mockMvc.perform(
                get("/v1/scores/"+ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");
        String player = JsonPath.parse(response).read("$.player");
        Integer score = JsonPath.parse(response).read("$.score");
        String time = JsonPath.parse(response).read("$.time");

        assertEquals(getScoreResponse.getId(), id);
        assertEquals(getScoreResponse.getPlayer(), player);
        assertEquals(getScoreResponse.getScore(), score);
        assertEquals(INPUTDATESTR, time);
    }

    @Test
    public void deleteScoreSuccess() throws Exception {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        DeleteScoreResponse deleteScoreResponse = new DeleteScoreResponse(ID,dateTime);
        doReturn(deleteScoreResponse).when(oyoTestService).deleteScore(any());
        MvcResult result = mockMvc.perform(
                delete("/v1/scores/"+ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");
        String deletedDate = JsonPath.parse(response).read("$.deleted_date");

        assertEquals(id, deleteScoreResponse.getId());
        assertEquals(INPUTDATESTR, deletedDate);
    }
}