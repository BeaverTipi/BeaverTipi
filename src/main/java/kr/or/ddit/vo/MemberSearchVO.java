package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; // ⭐ DateTimeFormatter 임포트 유지
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchVO implements Serializable {

    private String mbrCd;
    private String userRoleId;
    private String mbrId; // 회원 아이디 검색

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtFrom; // 가입일 시작일
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtTo; // 가입일 종료일

    private String mbrStatusCode; // 회원 상태 코드
    private String mbrEmlAddr; // 이메일 주소 검색
    private String mbrNm;    // 회원 이름 검색 필드
    private String mbrNnm;  // 회원 닉네임 검색 필드

    // ⭐⭐ 기존 getMbrFrstRegDtToPlusOne() 제거 ⭐⭐

    /**
     * mbrFrstRegDtFrom 필드를 'YYYY-MM-DD' 형식의 문자열로 반환
     */
    public String getMbrFrstRegDtFromString() {
        if (this.mbrFrstRegDtFrom != null) {
            return this.mbrFrstRegDtFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }

    /**
     * mbrFrstRegDtTo 필드에 하루를 더한 후 'YYYY-MM-DD' 형식의 문자열로 반환
     */
    public String getMbrFrstRegDtToPlusOneString() {
        if (this.mbrFrstRegDtTo != null) {
            return this.mbrFrstRegDtTo.plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return null;
    }
}