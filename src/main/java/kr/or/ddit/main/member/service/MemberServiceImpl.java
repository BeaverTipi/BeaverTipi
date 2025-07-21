package kr.or.ddit.main.member.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import kr.or.ddit.main.mapper.MemberMapper;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.validate.exception.FileIOException;
import kr.or.ddit.util.validate.exception.PKDuplicatedException;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberMapper mapper;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager; 
	
	private final FileService fileService;
	
	@Override
	@Transactional
	public void createMember(MemberVO member) {
		if(mapper.selectMemberByUsername(member.getMbrId())==null) {
			String encoded = passwordEncoder.encode(member.getMbrPw());
			member.setMbrPw(encoded);
			MultipartFile file = member.getMbrProfilImg();
			if(file!=null && !file.isEmpty() ) {
				FileVO newFile = this.fileUpload(file, member.getMbrCd());
				member.setMbrProfilImage(newFile.getFileId());
			}
			
			mapper.insertMember(member);
		}else {
			throw new PKDuplicatedException(member.getMbrId());
		}
	}

	private FileVO fileUpload(MultipartFile file, String mbrCd) {
		return	fileService.uploadAndSave(file, "public/profile", "MEMBER", mbrCd, file.getContentType());
	}
	private void fileUpdateUpload(String fileId,MultipartFile file) {
		try {
			fileService.updateFile(fileId, file);
		}catch(FileIOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MemberVO> readMemberList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberVO readMember(String username) {
		return mapper.selectMemberByUsername(username);
	}

	@Override
	public void modifyMember(MemberVO member) {
		
		MemberVO beforeMember = mapper.selectMember(member.getMbrCd());
		MultipartFile file = member.getMbrProfilImg();
		if(beforeMember.getMbrProfilImage()!=null && !beforeMember.getMbrProfilImage().isBlank()) {
			if(file!=null && !file.isEmpty() ) {
				this.fileUpdateUpload(beforeMember.getMbrProfilImage(),file);
			}
		}else {
			if(file!=null && !file.isEmpty() ) {
				FileVO newFile = this.fileUpload(file,member.getMbrCd());
				member.setMbrProfilImage(newFile.getFileId());
			}
		}
		
		mapper.updateMember(member);
		
//		기존 인증 객체 변경
		changeAuthentication(member);
	}

	@Override
	public void removeMember(String username, String password) {
		UsernamePasswordAuthenticationToken inputData =
				UsernamePasswordAuthenticationToken.unauthenticated(username, password); // 식별용으로 토큰이 필요하면 unauthenticated -> 검증용..
		
		authenticationManager.authenticate(inputData); // 인증을 위해 필요한 정보 줘라. 인증 실패면 exception뜬다.
		mapper.updateMemDelete(username);
	}

	private void changeAuthentication(MemberVO member) {
		UsernamePasswordAuthenticationToken inputData =
				UsernamePasswordAuthenticationToken.unauthenticated(member.getMbrId(), member.getMbrPw());
		SecurityContext context = SecurityContextHolder.getContext();
		
		UsernamePasswordAuthenticationToken before =
				(UsernamePasswordAuthenticationToken) context.getAuthentication(); // 기존 인증 객체
		Object datails = before.getDetails();
		
		UsernamePasswordAuthenticationToken newAuthentication =
				(UsernamePasswordAuthenticationToken)authenticationManager.authenticate(inputData);
		newAuthentication.setDetails(datails);
		
		context.setAuthentication(newAuthentication);
	}

	@Override
	public MemberVO readMemberByAll(String username) {
		// TODO Auto-generated method stub
		return mapper.selectMemberByUsername(username);
	}

	@Override
	public boolean checkedPassword(String username, String inputPassword) {
	    try {
	        UsernamePasswordAuthenticationToken inputData =
	            UsernamePasswordAuthenticationToken.unauthenticated(username, inputPassword);
	        authenticationManager.authenticate(inputData); // 실패 시 예외 발생
	        return true; // 성공
	    } catch (AuthenticationException e) {
	        return false; // 인증 실패
	    }
	}
	

}
