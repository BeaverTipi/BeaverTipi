package kr.or.ddit.main.mapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class MemberMapperTest {
	// 테스트 완료
    @Autowired
    MemberMapper mapper;

    @Test
    void testSelectMemberByUsername() {
        String testUsername = "manual_user_08"; // 임대인
//        String testUsername = "youaremysodapop"; // 중개인
//        String testUsername = "youaremysodapop"; // 중개인
    	
        MemberVO member = mapper.selectMemberByUsername(testUsername);

        log.info("조회된 회원 정보: {}", member);
        assertNotNull(member, "회원 정보가 null입니다.");

        if (member.getTenancy() != null) {
            log.info("▶ 임대/임차 정보: {}", member.getTenancy());
        }
        if (member.getBroker() != null) {
            log.info("▶ 공인중개사 정보: {}", member.getBroker());
        }
        if (member.getMemRoleList() != null) {
            log.info("▶ 보유 권한 수: {}", member.getMemRoleList().size());
        }
        if (member.getResidentList() != null) {
            log.info("▶ 입주 정보 수: {}", member.getResidentList().size());
        }
    }

    @Test
    void testSelectMemberByMail() {
        String testMail = "test@example.com"; // 테스트에 사용할 실제 이메일 필요
        MemberVO member = mapper.selectMemberByMail(testMail);

        log.info("이메일로 조회된 회원 정보: {}", member);
        assertNotNull(member, "이메일로 조회된 회원이 null입니다.");

        if (member.getTenancy() != null) {
            log.info("▶ 임대/임차 정보: {}", member.getTenancy());
        }
        if (member.getBroker() != null) {
            log.info("▶ 공인중개사 정보: {}", member.getBroker());
        }
        if (member.getMemRoleList() != null) {
            log.info("▶ 보유 권한 수: {}", member.getMemRoleList().size());
        }
        if (member.getResidentList() != null) {
            log.info("▶ 입주 정보 수: {}", member.getResidentList().size());
        }
    }




	@Test
	void testInsertMember() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateMemDelete() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateMember() {
		fail("Not yet implemented");
	}

}
