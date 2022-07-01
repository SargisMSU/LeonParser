package msu.sargis.parser;

import lombok.SneakyThrows;
import msu.sargis.entity.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static msu.sargis.utils.UrlHelper.*;

@Component
public class Parser {
    @Value("${parser.threads}")
    private int threadsCount;
    private final HttpClient httpClient;
    private ExecutorService executorService;

    public Parser(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @PostConstruct
    public void init() {
        this.executorService = Executors.newFixedThreadPool(threadsCount);
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }

    @SneakyThrows
    public List<League> parseTopLeagues() {
        List<League> result = new ArrayList<>();
        String response = httpClient.sendRequest(getLeaguesUrl());
        JSONArray jsonArray = (JSONArray) new JSONParser().parse(response);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String name = (String) jsonObject.get("name");
            if (Arrays.stream(SportType.values()).anyMatch(sportType -> sportType.getName().equals(name))) {
                SportType sportType = SportType.getByName(name);
                JSONArray regions = (JSONArray) jsonObject.get("regions");
                for (int j = 0; j < regions.size(); j++) {
                    JSONObject region = (JSONObject) regions.get(j);
                    JSONArray leagues = (JSONArray) region.get("leagues");
                    for (int k = 0; k < leagues.size(); k++) {
                        JSONObject league = (JSONObject) leagues.get(k);
                        Boolean top = (Boolean) league.get("top");
                        if (top) {
                            String leagueName = (String) league.get("name");
                            Long leagueId = (Long) league.get("id");
                            String regionName = (String) region.get("name");
                            result.add(new League(sportType, leagueId, leagueName, regionName));
                        }
                    }
                }
            }
        }
        return result;
    }

    @SneakyThrows
    public List<Game> parseFirstGamesOfLeagues(List<League> leagues) {
        List<Game> games = new ArrayList<>(leagues.size());
        CountDownLatch countDownLatch = new CountDownLatch(leagues.size());
        for (int i = 0; i < leagues.size(); i++) {
            int finalI = i;
            executorService.execute(() -> {
                games.add(parseFirstGameOfLeague(leagues.get(finalI)));
                synchronized (this) {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        return games;
    }

    @SneakyThrows
    public Game parseFirstGameOfLeague(League league) {
        String leagueUrl = getLeagueUrl(league.getId());
        String response = httpClient.sendRequest(leagueUrl);
        JSONObject json = (JSONObject) new JSONParser().parse(response);
        JSONArray events = (JSONArray) json.get("events");
        JSONObject event = (JSONObject) events.get(0);
        Long id = (Long) event.get("id");
        Game game = parseGameById(id);
        game.setLeague(league);
        return game;
    }

    private Game parseGameById(Long id) throws ParseException {
        String url = getGameUrl(id);
        String response = httpClient.sendRequest(url);

        JSONObject json = (JSONObject) new JSONParser().parse(response);
        String name = (String) json.get("name");
        Long kickoff = (Long) json.get("kickoff");
        JSONArray markets = (JSONArray) json.get("markets");
        List<Market> marketList = new ArrayList<>(markets.size());
        for (int i = 0; i < markets.size(); i++) {
            JSONObject marketJson = (JSONObject) markets.get(i);
            String marketName = (String) marketJson.get("name");
            if (marketJson.containsKey("handicap")) {
                marketName += " " + marketJson.get("handicap");
            }
            JSONArray runners = (JSONArray) marketJson.get("runners");
            List<Runner> runnerList = new ArrayList<>(runners.size());
            for (int j = 0; j < runners.size(); j++) {
                JSONObject runner = (JSONObject) runners.get(j);
                Long runnerId = (Long) runner.get("id");
                Double price = (Double) runner.get("price");
                String tag = (String) runner.get("name");
                runnerList.add(new Runner(runnerId, tag, price));
            }
            marketList.add(new Market(marketName, runnerList));
        }
        return new Game(id, name, kickoff, marketList);
    }
}
