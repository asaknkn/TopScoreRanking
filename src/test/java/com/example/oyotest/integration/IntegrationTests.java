package com.example.oyotest.integration;

import com.example.oyotest.dto.*;
import com.example.oyotest.utility.TestUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    private static final Integer ID = 1;
    private static final String INPUTDATESTR = "1984-11-01T00:00:00+09:00";

    @Sql({ "classpath:schema.sql"})
    @Test
    public void createNewScoreIntegration() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        CreateScoreRequest createScoreRequest = new CreateScoreRequest();
        createScoreRequest.setPlayer("Yamcha");
        createScoreRequest.setScore(1000);
        createScoreRequest.setTime(dateTime);

        String URL = "http://localhost:" + port + "/v1/scores";
        CreateScoreResponse result = this.restTemplate.postForObject(URL, createScoreRequest, CreateScoreResponse.class);
        assertEquals(ID, result.getId());
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void createSameScoreIntegration() throws ParseException {
        String inputDateStr = "1989-04-01T00:00:00+09:00";
        Date dateTime = TestUtility.changeStrToDate(inputDateStr);

        CreateScoreRequest createScoreRequest = new CreateScoreRequest();
        createScoreRequest.setPlayer("Gohan");
        createScoreRequest.setScore(50);
        createScoreRequest.setTime(dateTime);

        String URL = "http://localhost:" + port + "/v1/scores";
        CreateScoreResponse result = this.restTemplate.postForObject(URL, createScoreRequest, CreateScoreResponse.class);
        assertEquals(2, result.getId());
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void getScoreIntegration() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        String URL = "http://localhost:" + port + "/v1/scores";
        GetScoreResponse getScoreResponse = new GetScoreResponse(ID,"Goku",20,dateTime);
        GetScoreResponse result = this.restTemplate
                .getForObject(URL+"/{id}", GetScoreResponse.class, ID);

        assertEquals(getScoreResponse, result);
    }


    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void deleteScoreIntegration() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);
        DeleteScoreResponse deleteScoreResponse = new DeleteScoreResponse(ID, dateTime);

        String URL = "http://localhost:" + port + "/v1/scores";
        this.restTemplate
                .delete(URL+"/{id}",ID);

        ResponseEntity<DeleteScoreResponse> result = this.restTemplate.exchange(URL+"/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                DeleteScoreResponse.class,
                ID);

        assertEquals(deleteScoreResponse.getId(), result.getBody().getId());
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreIntegration() throws ParseException {
        String player1 = "Goku";
        String player2 = "Gohan";
        String before = "2021-01-01 23:59:59";
        String after = "2020-01-01 00:00:00";

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list?player={player1}&player={player2}&before={before}&after={after}",
                        GetScoreResponse[].class, player1, player2, before, after));

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();
        String strDate = "2020-10-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(4,"Goku",30, date));

        strDate = "2020-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(5,"Goku",40, date));

        strDate = "2021-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(6,"Goku",50, date));

        strDate = "2020-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(7,"Gohan",20, date));

        strDate = "2021-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(8,"Gohan",20, date));

        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreTotalZeroIntegration() throws ParseException {
        String player1 = "Gokuuuu";
        String player2 = "Gohannnn";
        String before = "2021-01-01 23:59:59";
        String after = "2020-01-01 00:00:00";

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list?player={player1}&player={player2}&before={before}&after={after}",
                        GetScoreResponse[].class, player1, player2, before, after));

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();
        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreOnlyNameIntegration() throws ParseException {
        String player1 = "Goku";
        String player2 = "Gohan";

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list?player={player1}&player={player2}",
                        GetScoreResponse[].class, player1, player2));



        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();

        String strDate = "1984-11-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(1,"Goku",20, date));

        strDate = "1989-04-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(2,"Gohan",50, date));

        strDate = "2020-10-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(4,"Goku",30, date));

        strDate = "2020-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(5,"Goku",40, date));

        strDate = "2021-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(6,"Goku",50, date));

        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreOnlyTimeIntegration() throws ParseException {
        String before = "2021-01-01 23:59:59";
        String after = "2020-01-01 00:00:00";

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list?before={before}&after={after}",
                        GetScoreResponse[].class, before, after));

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();

        String strDate = "2020-10-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(4,"Goku",30, date));

        strDate = "2020-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(5,"Goku",40, date));

        strDate = "2021-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(6,"Goku",50, date));

        strDate = "2020-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(7,"Gohan",20, date));

        strDate = "2021-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(8,"Gohan",20, date));

        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreOnlyBeforeIntegration() throws ParseException {
        String before = "2021-01-01 23:59:59";

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list?before={before}",
                        GetScoreResponse[].class, before));

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();

        String strDate = "1984-11-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(1,"Goku",20, date));

        strDate = "1989-04-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(2,"Gohan",50, date));

        strDate = "1996-02-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(3,"Piccolo",40, date));

        strDate = "2020-10-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(4,"Goku",30, date));

        strDate = "2020-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(5,"Goku",40, date));

        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreOnlyAfterIntegration() throws ParseException {
        String after = "2020-01-01 00:00:00";

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list?after={after}",
                        GetScoreResponse[].class, after));

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();

        String strDate = "2020-10-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(4,"Goku",30, date));

        strDate = "2020-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(5,"Goku",40, date));

        strDate = "2021-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(6,"Goku",50, date));

        strDate = "2020-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(7,"Gohan",20, date));

        strDate = "2021-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(8,"Gohan",20, date));

        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreNoParamIntegration() throws ParseException {

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list",
                        GetScoreResponse[].class));

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();

        String strDate = "1984-11-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(1,"Goku",20, date));

        strDate = "1989-04-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(2,"Gohan",50, date));

        strDate = "1996-02-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(3,"Piccolo",40, date));

        strDate = "2020-10-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(4,"Goku",30, date));

        strDate = "2020-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(5,"Goku",40, date));

        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void listScoreOnlyOffsetIntegration() throws ParseException {
        String offset = "1";

        String URL = "http://localhost:" + port + "/v1/scores";
        List<GetScoreResponse> result = Arrays.asList(this.restTemplate
                .getForObject(URL+"/list?offset={offset}",
                        GetScoreResponse[].class, offset));

        List<GetScoreResponse> getScoreResponses = new ArrayList<GetScoreResponse>();

        String strDate = "2021-01-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(6,"Goku",50, date));

        strDate = "2020-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(7,"Gohan",20, date));

        strDate = "2021-01-01T23:59:59+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(8,"Gohan",20, date));

        strDate = "2020-11-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        getScoreResponses.add(new GetScoreResponse(9,"Piccolo",40, date));

        assertEquals(getScoreResponses, result);
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void getPlayerHistoryIntegration() throws ParseException {
        String player = "Goku";
        String URL = "http://localhost:" + port + "/v1/scores";
        GetPlayerHistoryResponse result = this.restTemplate
                .getForObject(URL+"/history?player={player}",
                        GetPlayerHistoryResponse.class, player);

        GetPlayerHistoryResponse getPlayerHistoryResponse = new GetPlayerHistoryResponse();
        getPlayerHistoryResponse.setName("Goku");
        getPlayerHistoryResponse.setTop_score(50);
        getPlayerHistoryResponse.setLow_score(20);
        getPlayerHistoryResponse.setAverage_score((float) 35.0);

        ArrayList<PlayerScore> playerScores = new ArrayList<PlayerScore>();
        String strDate = "1984-11-01T00:00:00+09:00";
        Date date = TestUtility.changeStrToDate(strDate);
        playerScores.add(new PlayerScore(20, date));

        strDate = "2020-10-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        playerScores.add(new PlayerScore(30, date));

        strDate = "2020-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        playerScores.add(new PlayerScore(40, date));

        strDate = "2021-01-01T00:00:00+09:00";
        date = TestUtility.changeStrToDate(strDate);
        playerScores.add(new PlayerScore(50, date));

        getPlayerHistoryResponse.setScores(playerScores);

        assertEquals(getPlayerHistoryResponse, result);
    }
}
