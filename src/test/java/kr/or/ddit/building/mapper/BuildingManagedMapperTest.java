package kr.or.ddit.building.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.BuildingVO;
import lombok.extern.slf4j.Slf4j;
@SpringBootTest
@Slf4j
class BuildingManagedMapperTest {
	@Autowired
	BuildingManagedMapper mapper;
	@Test
	void testInsertBuilding() {
		fail("Not yet implemented");
	}

	@Test
	void testSelectBuildingListByBldgId() {
		List<BuildingVO> list = mapper.selectBuildingListByBldgId("2025070078");
		assertNotNull(list);
		list.forEach(l -> log.info(" 빌딩 정보 : {}",l));
	}

	@Test
	void testSelectBuildingById() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdateBuilding() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteBuilding() {
		fail("Not yet implemented");
	}

}
