package kr.or.ddit.file;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileTestService {
	private final S3Uploader s3Uploader;
	private final TestFileRepository testFileRepository;
	public void saveFile(MultipartFile imgFile, String imgText) throws IOException {
        if (!imgFile.isEmpty()) {
            String storedFileName = s3Uploader.upload(imgFile, "images"); // s3 버킷에 images 디렉토리에 업로드
            TestFileEntity testFileEntity = new TestFileEntity();
            testFileEntity.setImgText(imgText);
            testFileEntity.setImgUrl(storedFileName);
            testFileRepository.save(testFileEntity);
        }
    }
}
