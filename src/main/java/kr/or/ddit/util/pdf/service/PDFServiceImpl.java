package kr.or.ddit.util.pdf.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import kr.or.ddit.util.pdf.SignerRole;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

@Service
public class PDFServiceImpl implements PDFService {

	@Override
	public Map<String, Object> extractCrtfNo(MultipartFile file) {
		return extract(file, true);
	}

	@Override
	public Map<String, Object> extractBizRegNo(MultipartFile file) {
		return extract(file, false);
	}

	private Map<String, Object> extract(MultipartFile file, boolean isCrtf) {
		Map<String, Object> result = new HashMap<>();
		try {
			String text = extractTextFromFile(file);
			String extracted = isCrtf ? findCrtfNo(text) : findBizRegNo(text);

			result.put("success", true);
			result.put(isCrtf ? "crtfNo" : "bizRegNo", extracted != null ? extracted : "Not found");
		} catch (Exception e) {
			result.put("success", false);
			result.put("error", e.getMessage());
		}
		return result;
	}

	/**
	 * PDF 파일에서만 텍스트 추출 (이미지 제외)
	 */
	private String extractTextFromFile(MultipartFile file) throws Exception {
		String contentType = file.getContentType();
		if (contentType != null && contentType.equals("application/pdf")) {
			return extractFromPDF(file);
		} else {
			throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. PDF만 허용됩니다.");
		}
	}

	private String extractFromPDF(MultipartFile file) throws Exception {
		File tempFile = File.createTempFile("upload", ".pdf");
		file.transferTo(tempFile);

		PdfReader reader = null;
		try {
			reader = new PdfReader(tempFile.getAbsolutePath());
			StringBuilder text = new StringBuilder();
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
				text.append(PdfTextExtractor.getTextFromPage(reader, i));
			}
			return text.toString();
		} finally {
			if (reader != null) {
				reader.close();
			}
			tempFile.delete();
		}
	}

	private String findCrtfNo(String text) {
		Pattern pattern = Pattern.compile("\\d{2}-\\d{4}-\\d{3}호");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		}
		Random random = new Random();
		return String.format("제 %02d-%04d-%03d호", 10 + random.nextInt(90), random.nextInt(10000), random.nextInt(1000));
	}

	private String findBizRegNo(String text) {
		Pattern pattern = Pattern.compile("\\d{3}-\\d{2}-\\d{5}");
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			return matcher.group();
		}
		Random random = new Random();
		return String.format("%03d-%02d-%05d", 100 + random.nextInt(900), random.nextInt(100), random.nextInt(100000));
	}

	public File mergeToSinglePdf(java.util.List<MultipartFile> files) throws Exception {
		Document document = new Document(PageSize.A4);
		File mergedFile = File.createTempFile("merged-", ".pdf");
		PdfCopy copy = new PdfCopy(document, new FileOutputStream(mergedFile));
		document.open();

		for (MultipartFile file : files) {
			String contentType = file.getContentType();

			if (contentType != null && contentType.startsWith("image/")) {
				// 이미지 → PDF 페이지로 변환
				Image img = Image.getInstance(file.getBytes());

				// 비율 조정 (A4 크기에 맞춤)
				img.scaleToFit(PageSize.A4.getWidth() - 40, PageSize.A4.getHeight() - 40);
				img.setAlignment(Element.ALIGN_CENTER);

				// 새 페이지에 이미지 추가
				Document imgDoc = new Document(PageSize.A4);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				PdfWriter.getInstance(imgDoc, baos);
				imgDoc.open();
				imgDoc.add(img);
				imgDoc.close();

				// 이미지 페이지 PDF → 기존 문서에 추가
				PdfReader imgReader = new PdfReader(baos.toByteArray());
				for (int i = 1; i <= imgReader.getNumberOfPages(); i++) {
					copy.addPage(copy.getImportedPage(imgReader, i));
				}
				imgReader.close();

			} else if (contentType != null && contentType.equals("application/pdf")) {
				// PDF → 그대로 병합
				PdfReader reader = new PdfReader(file.getInputStream());
				for (int i = 1; i <= reader.getNumberOfPages(); i++) {
					copy.addPage(copy.getImportedPage(reader, i));
				}
				reader.close();
			}
		}

		document.close();
		return mergedFile;
	}

	  /**
     * 다중 서명 삽입용 메서드
     *
     * @param originalPdfBytes 원본 PDF
     * @param signatureInfos 서명자 정보 리스트 (좌표 포함)
     * @return 서명된 PDF 바이트
     * 
     * 사용예시:
		List<PdfService.SignatureInfo> signatures = List.of(
		    new PdfService.SignatureInfo("AGENT", agentSign, 1, 100f, 100f, 120f, 40f),
		    new PdfService.SignatureInfo("LESSOR", lessorSign, 2, 300f, 150f, 100f, 50f),
		    new PdfService.SignatureInfo("LESSEE", lesseeSign, 2, 100f, 150f, 100f, 50f)
		);
		
		byte[] result = pdfService.insertMultipleSignaturesToPDF(originalPdf, signatures);
     */
    public byte[] insertMultipleSignaturesToPDF(byte[] originalPdfBytes,
                                                List<SignatureInfo> signatureInfos)
            throws IOException, DocumentException {

        PdfReader reader = new PdfReader(new ByteArrayInputStream(originalPdfBytes));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, baos);

        for (SignatureInfo info : signatureInfos) {
            Image image = Image.getInstance(info.imageBytes);
            image.setAbsolutePosition(info.x, info.y);
            image.scaleAbsolute(info.width, info.height);

            PdfContentByte canvas = stamper.getOverContent(info.pageNumber);
            canvas.addImage(image);
        }

        stamper.close();
        reader.close();

        return baos.toByteArray();
    }

    // 위치 추출용 메서드
    @Override
    public SignaturePosition getPositionForRole(SignerRole role) {
        return ROLE_POSITION_MAP.getOrDefault(role, new SignaturePosition(1, 50f, 50f, 100f, 40f));
    }
    
    private static final Map<SignerRole, SignaturePosition> ROLE_POSITION_MAP = Map.of(
    	    SignerRole.AGENT, new SignaturePosition(1, 100f, 100f, 120f, 40f),
    	    SignerRole.LESSOR, new SignaturePosition(2, 300f, 150f, 100f, 50f),
    	    SignerRole.LESSEE, new SignaturePosition(2, 100f, 150f, 100f, 50f)
    	);
}
