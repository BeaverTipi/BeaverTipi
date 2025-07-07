<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- <%@ taglib uri="jakarta.tags.core" prefix="c" %> --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
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
</style>
</head>
<body>
<h1>신고관리</h1>
<table border="2" class="table table-striped table-hover">
		<thead>
			<tr>
				<th>No</th>
				<th>내용</th>
				<th>신고자</th>
				<th>신고일자</th>
				<th>처리일자</th>
				<th>상태</th>
				<th>선택</th>
			</tr>
		</thead>
		<tbody>
<%-- 			<c:choose> 
					<c:when test="${not empty  }">
						<c:forEach> 
						<c:url value="" var="detailURL">
							<c:param name="" value=""></c:param>
						</c:url> --%>
					<tr>
						<td>dummy</td>
						<td>
							<a href="#">내용dummy<a>
						</td>
						<td>신고자dummy</td>
						<td>신고일자dummy</td>
						<td>처리일지dummy</td>
						<td>상태dummy</td>
						<td><input type="checkbox" name="chkBox"></td>
					</tr>
<!-- 				</c:forEach> 
				</c:when>
			<c:otherwise> -->
<!-- 				<tr> -->
<!-- 					<td colspan="7"> -->
<!-- 						들어온 신고 내역이 없어요! -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 			</c:otherwise> -->
<!-- 			</c:choose> -->
		</tbody>
	</table>
</body>
</html>