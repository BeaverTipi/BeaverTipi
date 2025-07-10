package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.NoticeVO;

@Mapper
public interface NoticeResidentMapper {

	 /** 공지사항 목록 조회 */
    public List<NoticeVO> selectNoticeList(PaginationInfo<NoticeVO> paging);

    /** 전체 공지사항 수 조회 */
    public int selectNoticeTotalCount(PaginationInfo<NoticeVO> paging);

    /** 공지사항 단건 조회 */
    public NoticeVO selectNoticeById(String noticeNo);

    /** 공지사항 등록 */
    public int insertNotice(NoticeVO noticeVO);

    /** 공지사항 수정 */
    public int updateNotice(NoticeVO noticeVO);

    /** 공지사항 조회수 증가 */
    public int updateNoticeViewCount(NoticeVO noticeVO);

    /** 공지사항 소프트 삭제 */
    public int softDeleteNotice(String noticeNo);
    /** 공지사항 추가 시 필요한 보드 추가 */
    public int insertBoard(NoticeVO noticeVO);
	
    public String selectLastNoticeNo();
    
    public String getNextNoticeNo();
    
//	public List<ResidentBoardVO> selectDeletedNoticeList(PaginationInfo paging);

//	public int selectDeletedTotalCount(PaginationInfo paging);
//	
//	public int restoreNotice(String rsdBrdId);
//	
//	public int permanentDeleteNotice(String rsdBrdId);
}
