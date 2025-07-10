package kr.or.ddit.util.file.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.FileVO;

@Mapper
public interface FileMapper {
	public int insertFile(FileVO file);
	public int selectMaxAttachSeq(FileVO file);
	public List<FileVO> selectFilesOlderThanFiveYears();
	public int deleteFileById(String fileId);
	public FileVO selectFile(String fileId);
	public List<FileVO> selectFileList(FileVO file);
	public int updateFile(FileVO file);
	
}
