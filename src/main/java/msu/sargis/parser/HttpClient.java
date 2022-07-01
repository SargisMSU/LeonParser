package msu.sargis.parser;

import msu.sargis.exception.HttpException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient {
    private final RestTemplate restTemplate;
    private final HttpEntity<String> httpEntity;

    public HttpClient() {
        restTemplate = new RestTemplate();
        httpEntity = getEntity();
    }

    public String sendRequest(String url){
        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET, httpEntity, String.class);
        if (response.getStatusCodeValue() != 200)
            throw new HttpException(response.getStatusCode().getReasonPhrase() + " for GET request: \n" + url);
        return response.getBody();
    }

    private HttpEntity<String> getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.160 YaBrowser/22.5.4.904 Yowser/2.5 Safari/537.36");
        headers.set("Connection", "keep-alive");
        headers.set("Sec-Fetch-Dest", "document");
        headers.set("Host", "leon.bet");
        headers.set("Cache-Control", "max-age=0");
        headers.set("Accept-Language", "ru,en;q=0.9");
        headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        return new HttpEntity<>(headers);
    }

}

