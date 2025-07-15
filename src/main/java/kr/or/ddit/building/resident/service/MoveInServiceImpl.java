package kr.or.ddit.building.resident.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.MoveInMapper;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;

@Service
public class MoveInServiceImpl implements MoveInService {

    @Autowired
    private MoveInMapper mapper;

    @Override
    public int registerResident(UnitResidentVO vo) {
        return mapper.insertResident(vo);
    }

    @Override
    public int updateResident(UnitResidentVO vo) {
        return mapper.updateResident(vo);
    }

    @Override
    public int deleteResident(UnitResidentVO vo) {
        return mapper.deleteResident(vo);
    }

    @Override
    public List<UnitResidentVO> getResidentsByBldgId(String bldgId) {
        return mapper.selectResidentsByBldgId(bldgId);
    }

    @Override
    public List<MemberVO> searchMember(String keyword) {
        return mapper.searchMemberByKeyword(keyword);
    }
}
