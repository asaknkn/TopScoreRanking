package com.example.oyotest.controller;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.dto.CreateScoreResponse;
import com.example.oyotest.dto.DeleteScoreResponse;
import com.example.oyotest.dto.GetScoreResponse;
import com.example.oyotest.service.OyoTestService;
import com.example.oyotest.utility.TestUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private static Pageable pageable;

    @BeforeAll
    static void before() {
        pageable = new Pageable() {
            @Override
            public int getPageNumber() {
                return 0;
            }
            @Override
            public int getPageSize() {
                return 0;
            }
            @Override
            public long getOffset() {
                return 0;
            }
            @Override
            public Sort getSort() {
                return null;
            }
            @Override
            public Pageable next() {
                return null;
            }
            @Override
            public Pageable previousOrFirst() {
                return null;
            }
            @Override
            public Pageable first() {
                return null;
            }
            @Override
            public boolean hasPrevious() {
                return false;
            }
        };
    }

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

    @Test
    public void listScoreSuccess() throws Exception {
        String inputStr = "2020-10-01T00:00:00+09:00";
        Date dateTime = TestUtility.changeStrToDate(inputStr);

        String player1 = "Goku";
        String player2 = "Gohan";
        String before = "2021-01-01 23:59:59";
        String after = "2020-01-01 00:00:00";

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();
        getScoreResponses.add(new GetScoreResponse(ID,"Goku",20,dateTime));
        getScoreResponses.add(new GetScoreResponse(ID,"Gohan",20,dateTime));

        Integer total = 1;

        Page<GetScoreResponse> getScoreResponsePage = new PageImpl<>(getScoreResponses, pageable, total);
        doReturn(getScoreResponsePage).when(oyoTestService).listScore(any(),any());

        MvcResult result = mockMvc.perform(
                get("/v1/scores/list")
                        .param("player", player1)
                        .param("player",player2)
                        .param("before", before)
                        .param("after", after))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        String response = result.getResponse().getContentAsString();

        for (int i = 0; i < getScoreResponses.size(); ++i){
            Integer id = JsonPath.parse(response).read("$["+i+"].id");
            String playerName = JsonPath.parse(response).read("$["+i+"].player");
            Integer score = JsonPath.parse(response).read("$["+i+"].score");
            String time = JsonPath.parse(response).read("$["+i+"].time");

            assertEquals(getScoreResponses.get(i).getId(), id);
            assertEquals(getScoreResponses.get(i).getPlayer(), playerName);
            assertEquals(getScoreResponses.get(i).getScore(), score);
            assertEquals(inputStr, time);
        }
    }

    @Test
    public void listScoreSuccessOnlyPlayer() throws Exception {
        String inputStr = "2020-10-01T00:00:00+09:00";
        Date dateTime = TestUtility.changeStrToDate(inputStr);

        String player1 = "Goku";
        String player2 = "Gohan";

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();
        getScoreResponses.add(new GetScoreResponse(ID,"Goku",20,dateTime));
        getScoreResponses.add(new GetScoreResponse(ID,"Gohan",20,dateTime));

        Integer total = 1;

        Page<GetScoreResponse> getScoreResponsePage = new PageImpl<>(getScoreResponses, pageable, total);
        doReturn(getScoreResponsePage).when(oyoTestService).listScore(any(),any());

        MvcResult result = mockMvc.perform(
                get("/v1/scores/list")
                        .param("player", player1)
                        .param("player",player2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        for (int i = 0; i < getScoreResponses.size(); ++i){
            Integer id = JsonPath.parse(response).read("$["+i+"].id");
            String playerName = JsonPath.parse(response).read("$["+i+"].player");
            Integer score = JsonPath.parse(response).read("$["+i+"].score");
            String time = JsonPath.parse(response).read("$["+i+"].time");

            assertEquals(getScoreResponses.get(i).getId(), id);
            assertEquals(getScoreResponses.get(i).getPlayer(), playerName);
            assertEquals(getScoreResponses.get(i).getScore(), score);
            assertEquals(inputStr, time);
        }
    }

    @Test
    public void listScoreSuccessOnlyBefore() throws Exception {
        String inputStr = "2020-10-01T00:00:00+09:00";
        Date dateTime = TestUtility.changeStrToDate(inputStr);

        String before = "2021-01-01 23:59:59";

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();
        getScoreResponses.add(new GetScoreResponse(ID,"Goku",20,dateTime));
        getScoreResponses.add(new GetScoreResponse(ID,"Gohan",20,dateTime));

        Integer total = 1;

        Page<GetScoreResponse> getScoreResponsePage = new PageImpl<>(getScoreResponses, pageable, total);
        doReturn(getScoreResponsePage).when(oyoTestService).listScore(any(),any());

        MvcResult result = mockMvc.perform(
                get("/v1/scores/list")
                        .param("before", before))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        String response = result.getResponse().getContentAsString();

        for (int i = 0; i < getScoreResponses.size(); ++i){
            Integer id = JsonPath.parse(response).read("$["+i+"].id");
            String playerName = JsonPath.parse(response).read("$["+i+"].player");
            Integer score = JsonPath.parse(response).read("$["+i+"].score");
            String time = JsonPath.parse(response).read("$["+i+"].time");

            assertEquals(getScoreResponses.get(i).getId(), id);
            assertEquals(getScoreResponses.get(i).getPlayer(), playerName);
            assertEquals(getScoreResponses.get(i).getScore(), score);
            assertEquals(inputStr, time);
        }
    }
}