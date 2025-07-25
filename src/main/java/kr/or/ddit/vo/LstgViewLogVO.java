package kr.or.ddit.vo;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "lstgId", "mbrCd" })
public class LstgViewLogVO implements Serializable {
    private String lstgId;     // 매물 ID
    private String mbrCd;      // 회원 코드 or 비회원 IP
    private Timestamp viewDtm; // 조회 일시
}
