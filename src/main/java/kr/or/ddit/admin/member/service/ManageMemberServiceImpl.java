package kr.or.ddit.admin.member.service; // Corrected package to .service

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for @Transactional

import kr.or.ddit.admin.mapper.ManageMemberMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO; // Import MemberSearchVO for PaginationInfo generic type
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import Slf4j for logging

@Slf4j // Add Slf4j for logging
@Service
@RequiredArgsConstructor
public class ManageMemberServiceImpl implements ManageMemberService {

    private final ManageMemberMapper mapper;

    /**
     * 회원 목록을 페이징 및 검색 조건에 따라 조회합니다.
     * @param paging 페이징 정보 (현재 페이지, 페이지당 레코드 수, 검색 조건 포함)
     * @return 회원 목록
     */
    @Override
    // Changed PaginationInfo generic type to MemberSearchVO for clarity and type safety
    public List<MemberVO> getMemberList(PaginationInfo<MemberSearchVO> paging) {
        log.debug("Fetching member list with paging info: {}", paging);
        return mapper.selectMemberList(paging);
    }

    /**
     * 페이징 및 검색 조건에 해당하는 전체 회원 수를 조회합니다.
     * @param paging 페이징 정보 (검색 조건 포함)
     * @return 전체 회원 수
     */
    @Override
    // Changed PaginationInfo generic type to MemberSearchVO for clarity and type safety
    public int getTotalRecord(PaginationInfo<MemberSearchVO> paging) {
        log.debug("Counting total records with paging info: {}", paging);
        return mapper.selectTotalRecord(paging); // Corrected method name from selectTotalCount to selectTotalRecord as per previous context
    }

    /**
     * ⭐ Modified Method: 회원 상태를 업데이트 메서드 구현.
     * @param mbrCd 회원 코드
     * @param mbrStatusCode 변경할 회원 상태 코드
     * @return 업데이트 성공 여부 (true: 성공, false: 실패)
     */
    @Override
    @Transactional // Apply @Transactional for database update operations
    public boolean updateMemberStatus(String mbrCd, String mbrStatusCode) {
        log.info("Attempting to update member status for mbrCd: {} to {}", mbrCd, mbrStatusCode);
        // Directly pass parameters to mapper as per updated mapper interface (will be updated)
        int updatedRows = mapper.updateMemberStatus(mbrCd, mbrStatusCode); 
        if (updatedRows > 0) {
            log.info("Member status updated successfully for mbrCd: {}", mbrCd);
            return true;
        } else {
            log.warn("Member status update failed for mbrCd: {}. No rows affected.", mbrCd);
            return false;
        }
    }

    /**
     * ⭐ New Method: 특정 회원의 상세 정보를 조회합니다.
     * @param mbrCd 조회할 회원의 고유 코드
     * @return 조회된 MemberVO 객체 (회원 역할 목록 등 상세 정보 포함)
     */
    @Override
    public MemberVO getMemberDetailByMbrCd(String mbrCd) {
        log.info("Fetching detailed member information for mbrCd: {}", mbrCd);
        // Call the new mapper method to get full member details
        MemberVO member = mapper.selectMemberDetailByMbrCd(mbrCd);
        if (member != null) {
            log.debug("Member detail retrieved for mbrCd: {}. Member ID: {}", mbrCd, member.getMbrId());
        } else {
            log.warn("No member found for mbrCd: {}", mbrCd);
        }
        return member;
    }
}