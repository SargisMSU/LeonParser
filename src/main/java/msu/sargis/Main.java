package msu.sargis;

import msu.sargis.config.ParserConfiguration;
import msu.sargis.entity.Game;
import msu.sargis.entity.League;
import msu.sargis.parser.Parser;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args){
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ParserConfiguration.class);

        Parser parser = context.getBean("parser", Parser.class);

        List<League> leagues = parser.parseTopLeagues();
        List<Game> games = parser.parseFirstGamesOfLeagues(leagues);
        games.forEach(System.out::println);

        context.close();
    }
}
