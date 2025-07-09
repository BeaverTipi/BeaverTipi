package kr.or.ddit.admin.mapper;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.CommonCodeVO;
import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
class CommonCodeMapperTest {
	@Autowired
	CommonCodeMapper mapper;
	@Test // 테스트 완료
	void testSelectCommonCodeList() {
		String code = "PAY";
		List<CommonCodeVO> commonCodeList = mapper.selectCommonCodeList(code);
		assertNotNull(commonCodeList);
		commonCodeList.forEach(ccl ->
		log.info("code 종류 : {}",ccl)
				);
	}

}
