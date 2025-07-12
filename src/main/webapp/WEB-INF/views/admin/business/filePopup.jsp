<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>첨부파일 미리보기</title>

  <!-- 
   * == 개정이력(Modification Information) ==
   *   
   *   수정일               수정자           수정내용
   *  ============      ============== =======================
   *  2025. 7. 11.           김아린           최초 생성
   -->

  <!-- ✅ 외부 CSS 로드 -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/business/filePopup.css">
  <!-- ✅ PDF.js (CDN은 예외로 허용) -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>
</head>
<body>
<div id="fileDataHolder" data-filelist='${fileListJson}'></div>
  <h3>첨부파일 목록</h3>
<h3><button id="toggleFileListBtn" type="button">첨부파일 목록 보기</button></h3>
<table border="1" id="fileTable">
  <thead>
    <tr><th>파일명</th><th>크기</th></tr>
  </thead>
  <tbody></tbody>
</table>
  <hr>
  <canvas id="pdfCanvas"></canvas>
  <div>
  페이지 <span id="fileIndex">0</span> / <span id="totalCount">0</span>
</div>
  <div id="pdf-controls">
    <button id="prevBtn">이전</button>
    <button id="nextBtn">다음</button>
  </div>

  <!-- ✅ 외부 JS 로드 -->
  <script src="${pageContext.request.contextPath}/app/js/admin/business/filePopup.js"></script>
</body>
</html>
