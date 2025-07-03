<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="container">
		<div class="header">
			<h1>비동기요청 보내기</h1>
		</div>
		<div class="footer-buttons">
			<span id="lstgListHandler">중개인이 자신의 매물을 조회하고 싶대</span>
		</div>
		<div class="content" id="lstgListHandlerResult">
		
		</div>
		<script type="text/javascript">
			const lstgListHandler = window.lstgListHandler;
			window.lstgListHandler.addEventListener("click", event => {
				axios({
					method: 'get'
					, url: 'http://localhost:80/rest/broker/myoffice/lstg/list/M2507000110'
					, headers: {
						"Accept": "application/json"
						, withCredentials: true
					}
				}).then(resp => {
					let lstgList = resp.data;
					console.log(lstgList);
					let str = "";
					lstgList.forEach(lstg => {
						str += `
						lstgId: \${lstg.lstgId}
						, mbrCd: \${lstg.mbrCd}
						, rentalPtyId: \${lstg.rentalPtyId}
						, lstgType: \${lstg.lstgType}
						, lstgRoom: \${lstg.lstgRoom}
						, lstgCmar: \${lstg.lstgCmar}
						, \${lstg.tenancyInfo.rentalPtyId}
						`;
					});
					window.lstgListHandlerResult.innerHTML = str;
				})
			});
		</script>
	</div>
	
</body>
</html>