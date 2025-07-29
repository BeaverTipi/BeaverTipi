package kr.or.ddit.util.security.jwt;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kr.or.ddit.util.security.auth.MemberVOWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.RoleAchievedVO;

@Component
public class JwtProvider {

    @Value("${jwt.secrete-key}")
    private byte[] secreteKey;

    public static final long VALID_TERM = 1000 * 60 * 30; // 30분

    public String authenticationToToken(Authentication authentication) {
        try {
            JWSSigner signer = new MACSigner(secreteKey);

            MemberVOWrapper wrapper = (MemberVOWrapper) authentication.getPrincipal();
            MemberVO realUser = wrapper.getRealUser();

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(realUser.getMbrId())
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + VALID_TERM))
                .claim("mbrCd", realUser.getMbrCd())
                .claim("mbrStatus", realUser.getMbrStatusCode())
                .claim("scope", authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList())
                .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public Authentication tokenToAuthentication(String token) {
        SecretKey secretKey = new SecretKeySpec(secreteKey, JWSAlgorithm.HS256.getName());
        NimbusJwtDecoder decoder = NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();

        Jwt jwt = decoder.decode(token);

        String mbrId = jwt.getSubject();
        String mbrCd = jwt.getClaimAsString("mbrCd");
        String mbrStatus = jwt.getClaimAsString("mbrStatus");
        List<String> scope = jwt.getClaimAsStringList("scope");

        if (scope == null) {
            throw new IllegalStateException("JWT에 'scope' 클레임이 없습니다.");
        }

        // MemberVO 복원
        MemberVO member = new MemberVO();
        member.setMbrId(mbrId);
        member.setMbrCd(mbrCd);
        member.setMbrStatusCode(mbrStatus);
        member.setMbrPw(""); // 패스워드는 필요 없음
        member.setMemRoleList(
            scope.stream().map(role -> {
                RoleAchievedVO vo = new RoleAchievedVO();
                vo.setMbrCd(mbrCd);
                vo.setUserRoleId(role);
                return vo;
            }).toList()
        );

        // MemberVOWrapper로 감싸기
        MemberVOWrapper wrapper = new MemberVOWrapper(member, "DELETE");

        return new UsernamePasswordAuthenticationToken(wrapper, null, wrapper.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            SecretKey secretKey = new SecretKeySpec(secreteKey, JWSAlgorithm.HS256.getName());
            NimbusJwtDecoder decoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

            decoder.decode(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
