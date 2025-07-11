package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.ChargeBillVO;
@Mapper
public interface ChargeBillMapper {
	
	public List<ChargeBillVO> selectChargeBillForMonths(
	        @Param("unitId") String unitId,
	        @Param("previousMonth") String currentMonth,
	        @Param("beforeLastMonth") String previousMonth
	    );
}
