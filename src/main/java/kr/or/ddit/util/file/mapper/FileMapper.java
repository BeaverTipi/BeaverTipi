package kr.or.ddit.util.file.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.FileVO;

@Mapper
public interface FileMapper {
	public int insertFile(FileVO file);
	public Integer selectMaxAttachSeq(FileVO file);
	public List<FileVO> selectFilesOlderThanFiveYears();
	public int deleteFileById(String fileId);
	public FileVO selectFile(String fileId);
	public String selectFileSoureOne(String fileId);
	public List<FileVO> selectFileList(FileVO file);
	public int updateFile(FileVO file);
	public FileVO selectContractFile(@Param("contId")String contId,@Param("fileAttachSeq") Integer fileAttachSeq);
	public FileVO selectTempContractFile(@Param("contId") String contId, @Param("fileAttachSeq") Integer fileAttachSeq);
	public int updateFileUrl(String fileId, String newUrl); // ^0^
	public int selectTempContrMaxAttachSeq(String contId); // ^0^
}
