package kr.or.ddit.broker.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.FileVO;

@Mapper
public interface BrokerIntroCardMapper {
  
    FileVO selectIntroCardByMember(Map<String, Object> params);

    int insertIntroCardFile(FileVO file);

    int deleteIntroCardFile(Map<String, Object> params);
    
    public FileVO selectFile(String fileId);
    
    public int updateFile(FileVO file);

}
