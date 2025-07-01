<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
/*  table 스타일  */
$tableBorderColor: #ddd;
$tableTextColor: #333;
$tableBgColorHeader: #f5f5f5;
$stripedTableColor: #fafafa;
$tableBgRowHover: #eee;

table {
	border-collapse: collapse;
}

table.table {
  border-radius: 3px;
  border-style: hidden;
  box-shadow: 0 0 0 1px #ddd;
  width: 100%;
  color: #333;
  background: #fff;
  text-align: center;
}

table.table thead {
  background: #f5f5f5;
}

table.table thead th {
  font-weight: 400;
  padding: 15px 20px;
  text-transform: uppercase;
  border: 1px solid #ddd;
  border-radius: 3px;
}

table.table tbody td {
  padding: 10px 20px;
  border: 1px solid #ddd;
  border-radius: 3px;
  vertical-align: middle;
}

table.table-striped tbody tr:nth-of-type(odd) {
  background-color: #fafafa;
}

table.table-custom {
  box-shadow: none;
}

table.table-custom thead {
  background: transparent;
  border-bottom: 2px solid #ddd;
}

table.table-custom thead th {
  border: 0;
  border-bottom: 1px solid #ddd;
}

table.table-custom tbody tr {
  border-bottom: 1px solid #ddd;
}

table.table-custom tbody tr td {
  border: 0;
}

table.table-custom tbody tr:hover {
  background: #ddd;
}

table.table-hover tbody tr:hover {
  background-color: #eee;
}

.table-responsive {
  padding: 1px;
}

/*  button 스타일  */
.btn {
  border-radius: 4px;
  transition: all 0.3s ease;
}

.button-group{
	margin-bottom: 10px;
}

/*  btn-primary(파랑) 스타일  */
.btn.btn-primary {
  background-color: #007bff;
  border: 1px solid #007bff;
}
.btn.btn-primary:focus,
.btn.btn-primary:hover {
  background-color: #0069d9; 
  border: 1px solid #0069d9;
}

/*  btn-danger(빨강) 스타일  */
.btn.btn-danger {
  color: #fff;
  background-color: #dc3545;
  border: 1px solid #dc3545;
}
.btn.btn-danger:focus,
.btn.btn-danger:hover {
  background-color: #c82333; 
  border: 1px solid #c82333;
}

/*  btn-warning(노랑) 스타일  */
.btn.btn-warning {
  color: #fff;
  background-color: #ffc107;
  border: 1px solid #ffc107;
}
.btn.btn-warning:focus,
.btn.btn-warning:hover {
  background-color: #e0a800; 
  border: 1px solid #e0a800;
}
.btn.btn-warning:active {
  color: #fff !important;
}

/*  FORM 스타일  */
label {
    color: #7f8fa4;
    padding-right: 1rem;
    font-weight: 400;
}

.form-control {
    outline: none;
    line-height: 1.5em;
    padding-left: .8rem;
    padding-right: .8rem;
    background: #fff;
    border: 1px solid #ccc; 
    box-shadow: inset 0 2px 0 0 #f2f5f8;
    border-radius: 4px; 
}
.input-group-addon {
    background-color: #f0f0f0; 
    border-color: #d9d9d9; 
}

.input-group-btn + .form-control {
    border-left: 0;
}

.has-success .control-label {
    color: #28a745; 
}
.has-success .form-control {
    border-color: #28a745;
}

.has-warning .control-label {
    color: #ffc107; 
}
.has-warning .form-control {
    border-color: #ffc107;
}

.has-error .control-label {
    color: #dc3545; 
}
.has-error .form-control {
    border-color: #dc3545;
}
</style>
</head>
<body>
<h1>공지사항</h1>
	<div class="button-group">
		<button class="btn btn-primary">추가</button>
		<button class="btn btn-danger">삭제</button>
	</div>
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<th>No</th>
				<th>제목</th>
				<th>날짜</th>
				<th>조회수</th>
				<th>선택</th>
			</tr>
		</thead>
		<tbody>
<%-- 			<c:choose> 
					<c:when test="${not empty }">
						<c:forEach> 
						<c:url value="" var="detailURL">
							<c:param name="" value=""></c:param>
						</c:url> --%>
					<tr>
						<td>dummy</td>
						<td>
							<a href="javascript:void(0);" onclick="showDetail(this)">제목dummy</a>
						</td>
						<td>dummy</td>
						<td>dummy</td>
						<td><input type="checkbox" name="chkBox"></td>
					</tr>
					<tr style="display: none;">
						<td colspan="5">
							<div>
								<p>이거 바꿀꺼임 ㅅㅇㅅ 이거 예시화면 이에요~~</p>
								<button class="btn btn-warning" onclick="editForm(this)">수정</button>
								<form style="display: none;">
									<div class="form-group">
										<label for="editTitle">제목</label>
										<input type="text" id="editTitle" class="form-control" value="제목dummy">		
									</div>
									<div class="form-group">
										<label for="editContent">내용</label>
										<textarea id="editContent" class="form-control">dummy 상세 내용입니다.</textarea>									
									</div>
									<button type="button" class="btn btn-primary">저장</button>
									<button type="button" class="btn btn-secondary" onclick="cancelEdit(this)">취소</button>
								</form>
								<%-- <form:form>
									<input type="text" value="제목dummy">
									<textarea>dummy 상세 내용입니다.</textarea>
									<button type="button" class="btn btn-primary">저장</button>
									<button type="button" class="btn btn-secondary" onclick="cancelEdit(this)">취소</button>
								</form:form> --%>
							</div>
						</td>
					</tr>
<%--				</c:forEach> --%>
			
<%--			<c:otherwise> --%>
<%--				<tr> --%>
<%-- 					<td colspan="5"> --%>
<%-- 						새로운 공지 사항 하세요! --%>
<%-- 					</td> --%>
<%-- 				</tr> --%>
<%--			</c:otherwise> --%>
<%-- 			</c:choose> --%>
		</tbody>
	</table>
	<script>
		function showDetail(anchor) {
		    let detailRow = anchor.closest('tr').nextElementSibling;
		    detailRow.style.display = (detailRow.style.display === 'none' || detailRow.style.display === '') ? 'table-row' : 'none';
		}
	
		function editForm(button) {
		    let form = button.nextElementSibling;
		    form.style.display = '';
		    button.style.display = 'none';
		}
	
		function cancelEdit(button) {
		    let form = button.closest('form');
		    form.style.display = 'none';
		    form.previousElementSibling.style.display = '';
		}
	</script>
</body>
</html>