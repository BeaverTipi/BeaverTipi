package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerScheduleService;
import kr.or.ddit.vo.ScheduleVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/schedule")
@RequiredArgsConstructor
public class RestBrokerScheduleController {

    private final BrokerAuthUnpackingService authUnpack;
    private final BrokerScheduleService service;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /** 일정 목록 조회 */
    @PostMapping("list")
    public Map<String, String> scheduleList(Principal principal) {
        String username = principal.getName();
        String mbrCd = authUnpack.getMbrCd(username);
        List<ScheduleVO> scheduleList = service.readScheduleList(mbrCd);
        
        return BrokerCryptUtil.encryptResponsePayload(scheduleList);
    }

    /** 일정 상세 조회 (※ GET 불가하므로 POST로 대체) */
    @PostMapping("detail")
    public Map<String, String> scheduleDetail(
        Principal principal,
        @RequestBody Map<String, String> encryptedPayload
    ) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(encryptedPayload);
        String scdId = decrypted.get("scdId");

        String mbrCd = authUnpack.getMbrCd(principal.getName());
        ScheduleVO input = new ScheduleVO();
        input.setScdId(scdId);
        input.setMbrCd(mbrCd);

        ScheduleVO detail = service.readScheduleDetail(input);
        return BrokerCryptUtil.encryptResponsePayload(detail);
    }

    @PostMapping("add")
    public Map<String, String> scheduleAdd(
        Principal principal,
        @RequestBody Map<String, String> encryptedPayload
    ) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(encryptedPayload);
        String mbrCd = authUnpack.getMbrCd(principal.getName());

        ScheduleVO vo = ScheduleVO.builder()
            .mbrCd(mbrCd)
            .scdTitlNm(decrypted.get("title"))
            .scdStrDtm(LocalDateTime.parse(decrypted.get("start"), FORMATTER))
            .scdEndDtm(LocalDateTime.parse(decrypted.get("end"), FORMATTER))
            .scdCont(decrypted.get("content"))
            .scdRptSetCont(decrypted.get("rptSetCont"))
            .scdLevel(decrypted.get("calendarLevel"))
            .build();

        // insert
        ScheduleVO criteria = service.createSchedule(vo);


        return BrokerCryptUtil.encryptResponsePayload(Map.of(
            "id", Objects.toString(criteria.getScdId(), ""),
            "title", Objects.toString(criteria.getScdTitlNm(), ""),
            "start", criteria.getScdStrDtm().toString(),
            "end", criteria.getScdEndDtm().toString(),
            "calendarLevel", Objects.toString(criteria.getScdLevel(), ""),
            "rptSetCont", Objects.toString(criteria.getScdRptSetCont(), "")
        ));
    }



    /** 일정 수정 */
    @PostMapping("modify/{scdId}")
    public Map<String, String> scheduleModify(
        Principal principal,
        @PathVariable String scdId,
        @RequestBody Map<String, String> encryptedPayload
    ) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(encryptedPayload);
        String mbrCd = authUnpack.getMbrCd(principal.getName());

        ScheduleVO vo = ScheduleVO.builder()
					            .scdId(scdId)
					            .mbrCd(mbrCd)
					            .scdTitlNm(decrypted.get("title"))
					            .scdStrDtm(LocalDateTime.parse(decrypted.get("start"), FORMATTER))
					            .scdEndDtm(LocalDateTime.parse(decrypted.get("end"), FORMATTER))
					            .scdCont(decrypted.get("content"))
					            .scdRptSetCont(decrypted.get("rptSetCont"))
					            .scdLevel(decrypted.get("calendarLevel"))
					            .build();

        service.modifySchedule(vo);
        return BrokerCryptUtil.encryptResponsePayload(Map.of("updated", "1"));
    }

    @PostMapping("delete/{scdId}")
    public Map<String, String> scheduleDelete(
        Principal principal,
        @PathVariable String scdId
    ) {
        String mbrCd = authUnpack.getMbrCd(principal.getName());

        ScheduleVO vo = new ScheduleVO();
        vo.setScdId(scdId);
        vo.setMbrCd(mbrCd);

        service.removeSchedule(vo);
        return BrokerCryptUtil.encryptResponsePayload(Map.of("deleted", "1"));
    }

}
