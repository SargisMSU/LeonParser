package msu.sargis.entity;

import lombok.*;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Game {
    private long id;
    private League league;
    private String name;
    private long kickoff;
    private List<Market> markets;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public Game(long id, String name, long kickoff, List<Market> markets) {
        this.id = id;
        this.name = name;
        this.kickoff = kickoff;
        this.markets = markets;
    }

    @Override
    public String toString() {
        StringJoiner marketsJoiner = new StringJoiner("\n\u0009\u0009");
        for (Market market : markets) {
            StringJoiner runnersJoiner = new StringJoiner("\n\u0009\u0009\u0009");
            for (Runner runner : market.getRunners()) {
                runnersJoiner.add(runner.getTag() + ", " +
                        runner.getPrice() + ", " +
                        runner.getId());
            }
            marketsJoiner.add(market.getName() + "\n\u0009\u0009\u0009" + runnersJoiner);
        }
        return league.getSportType().getName() + ", " + league.getRegion() + " " + league.getName() +
                "\n\u0009" +
                name + ", " + simpleDateFormat.format(new Date(kickoff)) + ", " + id +
                "\n\u0009\u0009" +
                marketsJoiner;
    }
}
