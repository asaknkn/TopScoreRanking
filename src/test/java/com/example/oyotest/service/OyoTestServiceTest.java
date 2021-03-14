package com.example.oyotest.service;

import com.example.oyotest.dto.*;
import com.example.oyotest.entity.ScoreEntity;
import com.example.oyotest.repository.OyoTestRepository;
import com.example.oyotest.utility.CalculatePlayerScore;
import com.example.oyotest.utility.TestUtility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.MockedStatic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class OyoTestServiceTest {
    @Autowired
    private OyoTestService oyoTestService;

    @MockBean
    private OyoTestRepository oyoTestRepository;

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
    public void createExistedScoreSuccess() throws ParseException{
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        CreateScoreRequest param = new CreateScoreRequest();
        param.setPlayer("Goku");
        param.setScore(20);
        param.setTime(dateTime);

        ScoreEntity entity = new ScoreEntity();
        entity.setId(ID);

        doReturn(entity).when(oyoTestRepository).findByPlayerScoreTime(any());
        CreateScoreResponse result = oyoTestService.createScore(param);

        assertEquals(entity.getId(), result.getId());
    }

    @Test
    public void createNewScoreSuccess() throws ParseException{
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        CreateScoreRequest param = new CreateScoreRequest();
        param.setPlayer("Goku");
        param.setScore(20);
        param.setTime(dateTime);

        ScoreEntity entity = new ScoreEntity();
        entity.setId(ID);

        doReturn(null).when(oyoTestRepository).findByPlayerScoreTime(any());
        doReturn(entity).when(oyoTestRepository).create(any());
        CreateScoreResponse result = oyoTestService.createScore(param);

        assertEquals(entity.getId(), result.getId());
    }

    @Test
    public void findScoreSuccess() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        ScoreEntity entity = new ScoreEntity();
        entity.setPlayer("Goku");
        entity.setScore(10);
        entity.setTime(dateTime);

        doReturn(entity).when(oyoTestRepository).findById(any());
        GetScoreResponse result = oyoTestService.findScore(ID);

        assertEquals(entity.getPlayer(), result.getPlayer());
        assertEquals(entity.getScore(), result.getScore());
        assertEquals(entity.getTime(), result.getTime());
    }

    @Test
    public void deleteScoreSuccess() {
        doNothing().when(oyoTestRepository).delete(any());
        DeleteScoreResponse result = oyoTestService.deleteScore(ID);

        verify(oyoTestRepository).delete(ID);
        assertEquals(ID, result.getId());
    }

    @Test
    public void listScoreSuccess() throws ParseException {
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);

        ScoreEntity entity = new ScoreEntity();
        entity.setId(ID);
        entity.setPlayer("Goku");
        entity.setScore(10);
        entity.setTime(dateTime);

        ArrayList<ScoreEntity> scoreEntities = new ArrayList<ScoreEntity>();
        scoreEntities.add(entity);

        doReturn(1).when(oyoTestRepository).cntByEntities(any());
        doReturn(scoreEntities).when(oyoTestRepository).findList(any(),any());

        ListScoreRequest listScoreRequest = new ListScoreRequest();
        listScoreRequest.addList("Goku");
        listScoreRequest.setBefore(dateTime);
        listScoreRequest.setAfter(dateTime);

        Page<GetScoreResponse> result = oyoTestService.listScore(listScoreRequest,pageable);


        GetScoreResponse expectedRes = new GetScoreResponse();
        expectedRes.setId(scoreEntities.get(0).getId());
        expectedRes.setPlayer(scoreEntities.get(0).getPlayer());
        expectedRes.setScore(scoreEntities.get(0).getScore());
        expectedRes.setTime(scoreEntities.get(0).getTime());
        assertEquals(expectedRes, result.getContent().get(0));
    }

    @Test
    public void listScoreSuccessTotalZero() throws ParseException {
        doReturn(0).when(oyoTestRepository).cntByEntities(any());

        ScoreEntity entity = new ScoreEntity();
        entity.setId(ID);
        entity.setPlayer("Goku");
        entity.setScore(10);
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);
        entity.setTime(dateTime);

        ArrayList<ScoreEntity> scoreEntities = new ArrayList<ScoreEntity>();
        scoreEntities.add(entity);

        ListScoreRequest listScoreRequest = new ListScoreRequest();
        listScoreRequest.addList("Goku");
        listScoreRequest.setBefore(dateTime);
        listScoreRequest.setAfter(dateTime);

        Page<GetScoreResponse> result = oyoTestService.listScore(listScoreRequest,pageable);


        List<GetScoreResponse> expectedRes = new ArrayList<GetScoreResponse>();
        assertEquals(expectedRes, result.getContent());
    }

    @Test
    public void findPlayerHistorySuccess() throws ParseException {
        ScoreEntity entity = new ScoreEntity();
        entity.setPlayer("Goku");
        entity.setScore(10);
        Date dateTime = TestUtility.changeStrToDate(INPUTDATESTR);
        entity.setTime(dateTime);

        ArrayList<ScoreEntity> entities = new ArrayList<ScoreEntity>();
        entities.add(entity);

        doReturn(entities).when(oyoTestRepository).findByPlayer(any());

        MockedStatic mocked = mockStatic(CalculatePlayerScore.class);
        mocked.when(() -> CalculatePlayerScore.getTopScore(any())).thenReturn(20);
        mocked.when(() -> CalculatePlayerScore.getLowScore(any())).thenReturn(20);
        mocked.when(() -> CalculatePlayerScore.getAverageScore(any())).thenReturn((float) 20);

        GetPlayerHistoryResponse expected = new GetPlayerHistoryResponse();
        ArrayList<PlayerScore> playerScores = new ArrayList<PlayerScore>();
        PlayerScore playerScore = new PlayerScore(10, dateTime);

        expected.setName("Goku");
        playerScores.add(playerScore);
        expected.setScores(playerScores);
        expected.setTop_score(20);
        expected.setLow_score(20);
        expected.setAverage_score((float) 20);

        GetPlayerHistoryResponse result = oyoTestService.findPlayerHistory("Goku");
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getScores().get(0).getScore(), result.getScores().get(0).getScore());
        assertEquals(expected.getScores().get(0).getTime(), result.getScores().get(0).getTime());
        assertEquals(expected.getLow_score(), result.getLow_score());
        assertEquals(expected.getTop_score(), result.getTop_score());
        assertEquals(expected.getAverage_score(), result.getAverage_score());

        mocked.close();
    }

}