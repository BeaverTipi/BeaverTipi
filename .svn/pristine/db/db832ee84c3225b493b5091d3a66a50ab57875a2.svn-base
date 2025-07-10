<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>거래 내역</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <style>
    body {
      font-family: Arial, sans-serif;
      padding: 30px;
    }

    h2 {
      margin-bottom: 20px;
    }

    .add-button {
      float: right;
      margin-bottom: 10px;
      background-color: #00aaff;
      color: white;
      padding: 8px 16px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
      margin-bottom: 30px;
    }

    th, td {
      border: 1px solid #ddd;
      padding: 10px;
      text-align: center;
    }

    thead {
      background-color: #f2f2f2;
    }

    .delete-btn {
      background-color: #e74c3c;
      color: white;
      border: none;
      padding: 6px 12px;
      border-radius: 4px;
      cursor: pointer;
    }

    .empty-message {
      text-align: center;
      padding: 30px;
      color: #888;
    }
	    .modal {
	  position: fixed;
	  top: 0;
	  left: 0;
	  z-index: 1000;
	  width: 100%;
	  height: 100%;
	  background-color: rgba(0,0,0,0.6);
	  display: flex;
	  justify-content: center;
	  align-items: center;
	}
	
	.modal-content {
	  background-color: white;
	  padding: 30px;
	  border-radius: 10px;
	  width: 400px;
	  position: relative;
	}
	
	.modal-content h3 {
	  margin-top: 0;
	  margin-bottom: 20px;
	  font-size: 1.2em;
	}
	
	.modal-content label {
	  display: block;
	  margin-top: 10px;
	}
	
	.modal-content input,
	.modal-content select {
	  width: 100%;
	  padding: 6px;
	  margin-top: 5px;
	  margin-bottom: 10px;
	}
	
	.modal-buttons {
	  margin-top: 20px;
	  text-align: right;
	}
	
	.close {
	  position: absolute;
	  top: 12px;
	  right: 15px;
	  font-size: 20px;
	  cursor: pointer;
	}
  </style>
</head>
<body>

  <h2>등록 계좌 내역</h2>


  <c:if test="${fn:length(accountList) == 0}">
    <div class="empty-message">등록된 계좌가 없습니다.</div>
  </c:if>

  <c:if test="${fn:length(accountList) > 0}">
    <table>
      <thead>
        <tr>
          <th>금융기관<br>(계좌구분)</th>
          <th>계좌번호</th>
          <th>예금주</th>
          <th>연동 건물</th>
          <th>계좌 등록일</th>
          <th>관리</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="acc" items="${accountList}">
          <tr>
            <td>${acc.accBank}</td>
            <td>${acc.accNum}</td>
            <td>${acc.accMaster}</td>
            <td>${acc.building.bldgNm}</td>
            <td>${acc.accRegDate}</td>
            <td>
              <form method="post" action="/building/account/delete">
                <input type="hidden" name="accNum" value="${acc.accNum}">
                <input type="hidden" name="bldgId" value="${acc.bldgId}">
                <input type="hidden" name="rentalPtyId" value="${acc.rentalPtyId}">
                <button class="delete-btn" type="submit">삭제</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:if>
<!-- 수납계좌 추가 버튼 -->
<button class="add-button" onclick="openModal()">수납계좌 추가</button>

<!-- 모달 구조 -->
	<div id="accountModal" class="modal" style="display: none;">
	  <div class="modal-content">
	    <span class="close" onclick="closeModal()">&times;</span>
	    <h3>수납계좌 등록</h3>
	    <form id="accountForm" method="post" action="${pageContext.request.contextPath }/building/account/add">
	      <label>계좌번호</label>
	      <input type="text" name="accNum" required maxlength="20" />
	
	      <label>금융기관명</label>
	      <input type="text" name="accBank" required maxlength="30" />
	
	      <label>예금주</label>
	      <input type="text" name="accMaster" required maxlength="10" />
	
			<label>연동 건물</label>
			<select name="bldgId" id="buildingSelect" required>
			  <option value="" disabled selected>건물 선택</option>
			</select>
	      <input type="hidden" name="rentalPtyId" value="${rentalPtyId }" />
	      
	
	      <div class="modal-buttons">
	        <button type="submit" class="add-button">등록</button>
	        <button type="button" class="delete-btn" onclick="closeModal()">취소</button>
	      </div>
	    </form>
	  </div>
	</div>

<script>
	function openModal() {
		  document.getElementById("accountModal").style.display = "flex";
		  loadBuildingList(); // 모달 열릴 때 건물 리스트도 로딩
		}
	function closeModal() {
	    const modal = document.getElementById("accountModal");
	    modal.style.display = "none";
	    document.getElementById("accountForm").reset();
	  }

	function loadBuildingList() {
	  fetch("/building/account/buildingList")
	    .then(res => res.json())
	    .then(data => {
	      const select = document.getElementById("buildingSelect");
	      select.innerHTML = ''; // 기존 옵션 초기화

	      const defaultOpt = document.createElement("option");
	      defaultOpt.text = "건물 선택";
	      defaultOpt.disabled = true;
	      defaultOpt.selected = true;
	      select.appendChild(defaultOpt);

	      if (!data || data.length === 0) {
	        const emptyOpt = document.createElement("option");
	        emptyOpt.text = "등록된 건물이 없습니다";
	        emptyOpt.disabled = true;
	        select.appendChild(emptyOpt);
	        return;
	      }

	      data.forEach(b => {
	        const opt = document.createElement("option");
	        opt.value = b.bldgId;
	        opt.textContent = b.bldgNm;
	        select.appendChild(opt);
	      });
	    })
	    .catch(err => {
	      console.error("건물 목록 불러오기 실패", err);
	    });
	}
</script>
</body>
</html>
