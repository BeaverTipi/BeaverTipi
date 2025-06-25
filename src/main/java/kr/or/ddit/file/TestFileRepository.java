package kr.or.ddit.file;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestFileRepository {
	public int save(TestFileEntity testFileEntity);
}
