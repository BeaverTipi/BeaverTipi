<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>게시글 상세</title>
  <style>
    .detail-container {
      width: 700px;
      margin: 40px auto;
      border: 1px solid #ccc;
      padding: 20px;
    }
    .detail-info p {
      margin: 6px 0;
    }
    .label {
      display: inline-block;
      width: 80px;
      font-weight: bold;
    }
	.btn-group {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px; /* 버튼 간 간격 */
}

	.btn-group a {
	  padding: 8px 16px;
	  background-color: #4a90e2;
	  color: white;
	  border: none;
	  border-radius: 4px;
	  text-decoration: none;
	  font-weight: bold;
	  transition: background-color 0.2s ease;
	}
	
	.btn-group a:hover {
	  background-color: #357ab8;
	}

    .detail-content {
      max-height: 400px;
      overflow-y: auto;        
      padding: 10px;
      border: 1px solid #ddd;
      box-sizing: border-box;
    }
    .detail-content p {
      white-space: pre-wrap;
      word-break: break-all;
      overflow-wrap: break-word;
      margin: 0;
    }
    .delete-btn {
	  padding: 8px 16px;
	  background-color: #e74c3c;
	  color: white;
	  border: none;
	  border-radius: 4px;
	  font-weight: bold;
	  cursor: pointer;
	  transition: background-color 0.2s ease;
	}
	
	.delete-btn:hover {
	  background-color: #c0392b;
	}
	    
    
    
  </style>
</head>
<body>

  <div class="detail-container">
    <h2>${board.rsdBrdTitl}</h2>

    <div class="detail-info">
      <p>
        <span class="label">작성자:</span>
        ${board.mbrNnm}
      </p>
      <p>
        <span class="label">작성일:</span>
        <fmt:formatDate
          value="${board.rsdBrdPblsDate}"
          pattern="yyyy-MM-dd HH:mm" />
      </p>
      <p>
        <span class="label">조회수:</span>
        ${board.rsdBrdCnt}
      </p>
    </div>

    <hr/>

    <div class="detail-content">
      <p>${board.rsdBrdCont}</p>
    </div>

    <div class="btn-group">
 		 <a href="<c:url value='/resident/board'>
           <c:param name='bldgIdParam' value='${selectedBldgId}'/>
         		</c:url>">
					목록으로
		 </a>
  		<!-- 수정 버튼: 여기서만 form 엔드포인트 호출 -->
  		  <a href="<c:url value='/resident/board/form'>
              <c:param name='rsdBrdId'   value='${board.rsdBrdId}'/>
              <c:param name='bldgIdParam' value='${selectedBldgId}'/>
            </c:url>">
    수정
  </a>
	 <form action="${pageContext.request.contextPath}/resident/board/delete"
	      method="post"
	      onsubmit="return confirm('정말 삭제하시겠습니까?');"
	      style="display:inline;">
	  <input type="hidden" name="rsdBrdId" value="${board.rsdBrdId}" />
	  <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
	  <button type="submit" class="delete-btn">삭제</button>
	</form>
</div>



</body>
</html>
