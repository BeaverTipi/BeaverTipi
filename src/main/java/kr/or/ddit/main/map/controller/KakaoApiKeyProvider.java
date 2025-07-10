package kr.or.ddit.main.map.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KakaoApiKeyProvider {

    @Value("${custom.kakao.rest-api-key}")
    private String restApiKey;

    @Value("${custom.kakao.javascript-api-key}")
    private String jsApiKey;

    public String getRestApiKey() {
        return restApiKey;
    }

    public String getJsApiKey() {
        return jsApiKey;
    }
}