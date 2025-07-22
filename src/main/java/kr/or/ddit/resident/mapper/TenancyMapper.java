package kr.or.ddit.resident.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenancyMapper {

	String selectRentalPtyIdByMbrCd(String mbrCd);
}
