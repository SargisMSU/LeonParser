package msu.sargis.config;

import msu.sargis.parser.HttpClient;
import msu.sargis.parser.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:parser.properties")
public class ParserConfiguration {
    @Bean
    public Parser parser(){
        return new Parser(httpClient());
    }

    @Bean
    public HttpClient httpClient(){
        return new HttpClient();
    }
}

