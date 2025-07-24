package kr.or.ddit.util.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@EnableEncryptableProperties
@SpringBootTest
public class AES256UtilTest {
	
	@Autowired
	private AES256Util aes256Util;
	
	@Test
	@DisplayName("REACT-SPRING 암복호화 iv키 테스트")
	void testContractIdSecure() {
		
		String original = "CN250723002";
		String encrypted = "cai4gzV6J0gUApkMqvADeg==";
		String decrypted = aes256Util.decrypt(encrypted);
		log.info("결과야 결과 {}", decrypted);
		assertEquals(original, decrypted);
	}
	
	@Test
	@DisplayName("문자열 암복호화 테스트")
	void testEncryptDecrypt() {
		
		//테스트 문자열
		String original = "자네 잔나잘하 잖나";
		//암호화 수행
		String encrypted = aes256Util.encrypt(original);
		//복호화 수행
		String decrypted = aes256Util.decrypt(encrypted);
		
		log.info("encrypted -> decrypted: {} -> {}", encrypted, decrypted);
		assertThat(decrypted).isEqualTo(original);
	}
	
	@Test
	@DisplayName("빈 문자열 암복호화 테스트")
	void testEmptyString() {
		
		//테스트 문자열
		String original = "";
		//암호화 수행
		String encrypted = aes256Util.encrypt(original);
		//복호화 수행
		String decrypted = aes256Util.decrypt(encrypted);
		
		log.debug("encrypted -> decrypted: {} -> {}", encrypted, decrypted);
		assertThat(decrypted).isEmpty();
	}
	
	@Test
	@DisplayName("한글, 특수문자 포함된 문자열 암복호화")
	void testSpecialCharacters() {
		
		//테스트 문자열
		String original = "이름: 김민지 🐱💼@#%!";
		//암호화 수행
		String encrypted = aes256Util.encrypt(original);
		//복호화 수행
		String decrypted = aes256Util.decrypt(encrypted);
		
		log.warn("encrypted -> decrypted: {} -> {}", encrypted, decrypted);
		assertThat(decrypted).isEqualTo(original);
	}

}
