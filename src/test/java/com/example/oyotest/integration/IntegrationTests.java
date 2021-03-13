package com.example.oyotest.integration;

import com.example.oyotest.dto.CreateScoreRequest;
import com.example.oyotest.dto.CreateScoreResponse;
import com.example.oyotest.dto.DeleteScoreResponse;
import com.example.oyotest.dto.GetScoreResponse;
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
import java.util.Date;

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

        String url = "http://localhost:" + port + "/v1/scores";
        CreateScoreResponse result = this.restTemplate.postForObject(url, createScoreRequest, CreateScoreResponse.class);
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

        String url = "http://localhost:" + port + "/v1/scores";
        CreateScoreResponse result = this.restTemplate.postForObject(url, createScoreRequest, CreateScoreResponse.class);
        assertEquals(2, result.getId());
    }

    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void getScoreIntegration() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        String url = "http://localhost:" + port + "/v1/scores";
        GetScoreResponse getScoreResponse = new GetScoreResponse(ID,"Goku",20,dateTime);
        GetScoreResponse result = this.restTemplate
                .getForObject(url+"/{id}", GetScoreResponse.class, ID);

        assertEquals(getScoreResponse, result);
    }


    @Sql({ "classpath:schema.sql", "classpath:data.sql" })
    @Test
    public void deleteScoreIntegration() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);
        DeleteScoreResponse deleteScoreResponse = new DeleteScoreResponse(ID, dateTime);

        String url = "http://localhost:" + port + "/v1/scores";
        this.restTemplate
                .delete(url+"/{id}",ID);

        ResponseEntity<DeleteScoreResponse> result = this.restTemplate.exchange(url+"/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                DeleteScoreResponse.class,
                ID);

        assertEquals(deleteScoreResponse.getId(), result.getBody().getId());
    }


}
