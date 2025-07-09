<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="card border-0 shadow mb-4">
  <div class="card-body">
    <h2 class="h4 mb-3 fw-bold text-dark">${notice.brdTitlNm}</h2>

    <div class="d-flex flex-wrap justify-content-between text-muted small mb-3">
      <div>
        📌 <span class="me-3">${notice.noticeTypeCode.codeName}</span>
        ✍️ <span class="me-3">${notice.member.mbrNnm}</span>
      </div>
      <div>
        🗓️ ${notice.formattedBrdPblsDtm} |
        👁️ ${notice.brdVwCnt}회 조회
      </div>
    </div>

    <hr>

    <div class="mt-4" style="white-space: pre-wrap; line-height: 1.7;">
      ${notice.brdCont}
    </div>

    <div class="mt-4 text-end">
      <a href="/resident/notice?bldgIdParam=${selectedBldgId}" class="btn btn-sm btn-primary">
        ← 목록으로
      </a>
    </div>
  </div>
</div>