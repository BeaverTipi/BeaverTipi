package kr.or.ddit.vo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchVO implements Serializable {

    private String mbrCd; // 특정 회원 코드로 검색하는 경우를 대비하여 유지

    private String userRoleId; // 단일 선택으로 변경했음

    private String mbrId; // 회원 아이디 검색
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtFrom; // 가입일 시작일
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mbrFrstRegDtTo; // 가입일 종료일
    private String mbrStatusCode; // 회원 상태 코드
    private String mbrEmlAddr; // 이메일 주소 검색


}