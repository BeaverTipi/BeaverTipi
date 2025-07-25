package kr.or.ddit.broker.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import kr.or.ddit.util.crypto.AES256Util;

@Component
public class BrokerCryptUtil {
	private static ObjectMapper staticMapper;
	private static AES256Util staticAes256Util;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private AES256Util aes256Util;

	@PostConstruct
	private void initStatics() {
		staticMapper = mapper;
		staticAes256Util = aes256Util;
	}

	public static Map<String, String> decryptRequestPayload(Map<String, String> payload) {
	    String iv = payload.get("iv");
	    String encrypted = payload.get("encrypted");
	    if (encrypted == null) throw new IllegalArgumentException("암호화된 요청 없음");

	    String decryptedJson = staticAes256Util.decryptWithDynamicIV(encrypted, iv);
	    try {
	        return staticMapper.readValue(decryptedJson, new TypeReference<>() {});
	    } catch (Exception e) {
	        throw new RuntimeException("요청 JSON 파싱 실패", e);
	    }
	}

	public static Map<String, String> encryptResponsePayload(Object responseData) {
	    try {
	        String resultJson = staticMapper.writeValueAsString(responseData);
	        return staticAes256Util.encryptWithDynamicIV(resultJson);
	    } catch (Exception e) {
	        throw new RuntimeException("응답 암호화 실패", e);
	    }
	}
}

