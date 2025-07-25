package kr.or.ddit.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.web.multipart.MultipartFile;

public class ToMultipartFileUtil {

    public static MultipartFile convert(File file) throws IOException {
        byte[] content = Files.readAllBytes(file.toPath());
        String contentType = Files.probeContentType(file.toPath());

        return new Base64DecodedMultipartFile(
            content,
            "file",                      // name
            file.getName(),              // original filename
            contentType != null ? contentType : "application/octet-stream"
        );
    }
}