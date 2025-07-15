package kr.or.ddit.admin.business.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.admin.business.service.BusinessApproveService;
import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.util.renderer.DefaultPaginationRenderer;
import kr.or.ddit.util.validate.exception.ApprovedException;
import kr.or.ddit.util.validate.exception.FileIOException;
import kr.or.ddit.util.validate.exception.RejectedException;
import kr.or.ddit.vo.BusinessApproveSearchVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("")
@RequiredArgsConstructor
@Slf4j
public class BusinessApproveController {
	private final BusinessApproveService service;
	private final CommonCodeService commonService;
	private final String MODELNAME = "search";
	private final FileService fileService;
	
	private final ObjectMapper mapper;

	
	@ModelAttribute(MODELNAME)
	public BusinessApproveSearchVO search() {
		return new BusinessApproveSearchVO();
	}
	@GetMapping("/admin/business/approve")
	public String UI(
			Model model
			, @RequestParam(required = false, defaultValue = "1") int page
			, @ModelAttribute(MODELNAME) BusinessApproveSearchVO search
	) {
		log.info("=====> role정보 : {}",search.getRole());
		// 1. 페이징 객체 준비
				PaginationInfo<BusinessApproveSearchVO> paging = new PaginationInfo<>();
				paging.setCurrentPageNo(page);
				paging.setDetailSearch(search);
				
				// 2. 전체 개수 조회
				int totalRecord = service.readTotalRecord(paging);
				paging.setTotalRecordCount(totalRecord);

				// 3. 목록 조회
				List<MemberVO> approveList = service.readBusinessApproveList(paging);

				// 4. 페이징 HTML 생성
				String pagingHTML = new DefaultPaginationRenderer().renderPagination(paging, "fnPaging");

				List<CommonCodeVO> roleList = commonService.readCommonCodeList("SOL");
				List<CommonCodeVO> statusCodeList = commonService.readCommonCodeList("APST");
				List<CommonCodeVO> fileCodeList = commonService.readCommonCodeList("FILE");
				if (search.getHasFile() == null) {
				    search.setHasFile("");  // 기본값 '전체'
				}

				// 5. 모델 바인딩
				model.addAttribute("approveList", approveList);
				model.addAttribute("pagingHTML", pagingHTML);
				model.addAttribute("pagingInfo", paging);
				model.addAttribute("roleList", roleList);
				model.addAttribute("statusCodeList", statusCodeList);
				model.addAttribute("fileCodeList", fileCodeList);
				return "admin/business/businessApprove";
		}
	
    @PostMapping("/admin/business/approve/{userType}/{mbrCd}")
    public String formApprove(@PathVariable String mbrCd,@PathVariable String userType, RedirectAttributes redirectAttributes) {
        try {
        	        service.approveMember(mbrCd, userType);
        	        redirectAttributes.addFlashAttribute("message", "승인 처리되었습니다.");
        } catch (ApprovedException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
    	redirectAttributes.addFlashAttribute("message", "승인 처리 중 오류가 발생했습니다.");
    }
        return "redirect:/admin/business/approve"; // 목록 페이지로 리다이렉트
    }

    @PostMapping("/admin/business/reject/{userType}/{mbrCd}")
    public String formReject(@PathVariable String mbrCd, @PathVariable String userType,RedirectAttributes redirectAttributes) {
        try {
            service.rejectMember(mbrCd,userType);
            redirectAttributes.addFlashAttribute("message", "거절 처리되었습니다.");
        } catch (RejectedException e) {
            redirectAttributes.addFlashAttribute("message",  e.getMessage());
        }catch (Exception e) {
    	redirectAttributes.addFlashAttribute("message", "거절 처리 중 오류가 발생했습니다.");
    }
        return "redirect:/admin/business/approve";
    }



	@GetMapping("/admin/business/filePopup/{userType}/{mbrCd}")
	public String showFilePopup(@PathVariable String mbrCd,
	                            @PathVariable String userType,
	                            Model model) throws JsonProcessingException {

	    List<FileVO> fileList = fileService.readFileList(userType, mbrCd);

	    String fileListJson = mapper.writeValueAsString(fileList);

	    model.addAttribute("fileListJson", fileListJson);
	    model.addAttribute("mbrCd", mbrCd);
	    model.addAttribute("userType", userType);

	    return "admin/business/filePopup";
	}

	@PostMapping("/ajax/admin/business/bulkAction")
	public ResponseEntity<String> bulkAction(@RequestBody Map<String, Object> bulkRequest) {
	    try {
	        String action = (String) bulkRequest.get("action");
	        List<Map<String, String>> users = (List<Map<String, String>>) bulkRequest.get("users");

	        if ("approve".equalsIgnoreCase(action)) {
	            for (Map<String, String> user : users) {
	                String userType = user.get("userType");
	                String mbrCd = user.get("mbrCd");
	                service.approveMember(mbrCd, userType);
	            }
	        } else if ("reject".equalsIgnoreCase(action)) {
	            for (Map<String, String> user : users) {
	                String userType = user.get("userType");
	                String mbrCd = user.get("mbrCd");
	                service.rejectMember(mbrCd, userType);
	            }
	        } else {
	            return ResponseEntity.badRequest().body("알 수 없는 작업입니다.");
	        }

	        return ResponseEntity.ok("일괄 처리 완료");
	    } catch (RejectedException e) {
	        log.error("bulkAction 오류", e);
	        return ResponseEntity.status(500).body(e.getMessage());
	    } catch (ApprovedException e) {
		log.error("bulkAction 오류", e);
		return ResponseEntity.status(500).body(e.getMessage());
	}
	}

	
	@GetMapping("/admin/business/file/preview/{fileId}")
	public void previewFile(@PathVariable String fileId, HttpServletResponse response) {
	    FileVO file = fileService.readFile(fileId);
	    if (file == null) throw new FileIOException("파일이 존재하지 않습니다.");

	    response.setContentType(file.getFileMime());
	    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getFileOriginalname() + "\"");

	    try (InputStream is = fileService.getFileStream(fileId)) {
	        StreamUtils.copy(is, response.getOutputStream());
	    } catch (IOException e) {
	        throw new FileIOException("파일 미리보기 처리 중 오류 발생", e);
	    }
	}

    // ✅ Presigned URL 활용 (옵션)
    @GetMapping("/admin/business/presigned/{fileId}")
    public String generatePresignedUrl(@PathVariable String fileId,
                                       @RequestParam(defaultValue = "3") int expireMinutes) {
        return fileService.getPresignedUrl(fileId, expireMinutes); // 3분 유효한 URL
    }

}

