package kr.or.ddit.resident.residentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/resident")
public class RsdBoardController {
		@GetMapping("/board")
		public String noticeMyHousePage() {
			 return "resident/Board/Board"; 
		}
		
}
