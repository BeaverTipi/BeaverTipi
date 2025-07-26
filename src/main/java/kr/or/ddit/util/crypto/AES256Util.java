package kr.or.ddit.util.crypto;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AES256Util {

    @Value("${crypto.secret-key}")
    private String secretKey; // 32자

    //React에서는 동적으로 iv 생성 중.
    @Value("${crypto.iv}")
    private String iv; // 16자

    private SecretKeySpec keySpec;
    private IvParameterSpec ivSpec;
    
    @PostConstruct
    public void init() {
        if (secretKey.length() != 32 || iv.length() != 16) {
            throw new IllegalArgumentException("Secret Key must be 32 characters and IV must be 16 characters long.");
        }
        keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
        ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("[AES256] Encryption failed", e);
            throw new RuntimeException("암호화 실패");
        }
    }

    public String decrypt(String cipherText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("[AES256] Decryption failed", e);
            throw new RuntimeException("복호화 실패");
        }
    }
    
    public Map<String, String> encryptWithDynamicIV(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] randomIv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(randomIv);
            IvParameterSpec ivSpec = new IvParameterSpec(randomIv);

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            String base64Iv = Base64.getEncoder().encodeToString(randomIv);
            String base64Cipher = Base64.getEncoder().encodeToString(encryptedBytes);

            return Map.of("iv", base64Iv, "encrypted", base64Cipher);
        } catch (Exception e) {
            log.error("[AES256] 암호화 실패", e);
            throw new RuntimeException("응답 암호화 실패");
        }
    }
    
    public String decryptWithDynamicIV(String base64CipherText, String base64Iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            byte[] ivBytes = Base64.getDecoder().decode(base64Iv);
            IvParameterSpec dynamicIvSpec = new IvParameterSpec(ivBytes);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, dynamicIvSpec);
            byte[] cipherBytes = Base64.getDecoder().decode(base64CipherText);
            byte[] decrypted = cipher.doFinal(cipherBytes);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("[AES256] 동적 IV 복호화 실패", e);
            throw new RuntimeException("복호화 실패");
        }
    }
}