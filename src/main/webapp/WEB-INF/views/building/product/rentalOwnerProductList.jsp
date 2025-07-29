<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>내 매물 관리</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

<div class="container mt-4">
  <div class="card">
    <div class="card-body">
      <h2 class="mb-4">내 매물 관리</h2>

      <form:form modelAttribute="searchForm" action="${pageContext.request.contextPath}/building/product/list" method="get" class="border p-3 rounded mb-4 bg-light">
        <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput" />
        <div class="form-row">
          <div class="form-group col-md-3">
            <label>건물명</label>
            <form:input path="searchBuildingName" class="form-control" placeholder="건물명 입력"/>
          </div>
          <div class="form-group col-md-2">
            <label>호수</label>
            <form:input path="searchRoomNum" class="form-control" placeholder="예: 101호"/>
          </div>
          <div class="form-group col-md-2">
            <label>상태</label>
            <form:select path="searchStatus" class="form-control">
              <form:option value="">전체</form:option>
              <c:forEach var="code" items="${statusCodeList}">
                <form:option value="${code.codeValue}">${code.codeName}</form:option>
              </c:forEach>
            </form:select>
          </div>
          <div class="form-group col-md-2">
            <label>거래유형</label>
            <form:select path="searchType" class="form-control">
              <form:option value="">전체</form:option>
              <c:forEach var="typeCode" items="${typeSaleCodeList}">
                <form:option value="${typeCode.codeValue}">${typeCode.codeName}</form:option>
              </c:forEach>
            </form:select>
          </div>
          <div class="form-group col-md-2">
            <label>보증금</label>
            <div class="d-flex">
              <form:input path="searchDepositMin" class="form-control mr-1" placeholder="최소" />
              <form:input path="searchDepositMax" class="form-control" placeholder="최대" />
            </div>
          </div>
          <div class="form-group col-md-2">
            <label>월세</label>
            <div class="d-flex">
              <form:input path="searchMonthlyMin" class="form-control mr-1" placeholder="최소" />
              <form:input path="searchMonthlyMax" class="form-control" placeholder="최대" />
            </div>
          </div>
          <div class="form-group col-md-2">
            <label>매매가</label>
            <div class="d-flex">
              <form:input path="searchSaleMin" class="form-control mr-1" placeholder="최소" />
              <form:input path="searchSaleMax" class="form-control" placeholder="최대" />
            </div>
          </div>
          <div class="form-group col-md-3 d-flex align-items-end">
            <button type="submit" class="btn btn-dark mr-2">검색</button>
            <button type="reset" class="btn btn-warning">초기화</button>
          </div>
        </div>
      </form:form>

      <div class="table-responsive">
        <table class="table table-bordered table-hover text-center">
          <thead class="thead-light">
            <tr>
              <th>순번</th>
              <th>건물명</th>
              <th>상태</th>
              <th>거래유형</th>
              <th>전세금</th>
              <th>보증금</th>
              <th>월세</th>
              <th>매매가</th>
            </tr>
          </thead>
          <tbody id="listingTableBody">
            <c:choose>
              <c:when test="${empty listingProductList}">
                <tr><td colspan="8">등록된 매물이 없습니다.</td></tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="listing" items="${listingProductList}" varStatus="status">
                  <tr class="listing-row" data-address="${listing.lstgAdd}">
                    <td>${status.index + 1}</td>
                    <td><a href="#" class="building-name-link font-weight-bold text-primary">${listing.lstgNm}</a></td>
                    <td>
                      <c:choose>
                        <c:when test="${listing.lstgProdStat == 1}">활성</c:when>
                        <c:when test="${listing.lstgProdStat == 2}">비활성</c:when>
                        <c:when test="${listing.lstgProdStat == 3}">숨기기</c:when>
                        <c:otherwise>미정</c:otherwise>
                      </c:choose>
                    </td>
                    <td>
                      <c:choose>
                        <c:when test="${listing.lstgTypeSale == 1}">전세</c:when>
                        <c:when test="${listing.lstgTypeSale == 2}">월세</c:when>
                        <c:when test="${listing.lstgTypeSale == 3}">매매</c:when>
                        <c:otherwise>기타</c:otherwise>
                      </c:choose>
                    </td>
                    <td>${empty listing.lstgLease ? '-' : listing.lstgLease}</td>
                    <td>${empty listing.lstgLeaseM ? '-' : listing.lstgLeaseM}</td>
                    <td>${empty listing.lstgLeaseAmt ? '-' : listing.lstgLeaseAmt}</td>
                    <td>-</td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>

      <div class="pagination-wrapper mt-4">
        <nav aria-label="Page navigation">
          <ul class="pagination justify-content-center">
            ${pagingHTML}
          </ul>
        </nav>
      </div>
    </div>
  </div>
</div>

<script>
const tableBody = document.getElementById('listingTableBody');

tableBody.addEventListener('click', async (e) => {
  if (!e.target.classList.contains('building-name-link')) return;
  e.preventDefault();

  const row = e.target.closest('tr');
  const address = row.dataset.address;
  const safeAddress = address.replace(/[^\w]/g, '').slice(0, 40); // ID로 안전하게

  const detailRowId = `detail-${safeAddress}`;
  const existingDetailRow = document.getElementById(detailRowId);

  if (existingDetailRow) {
    existingDetailRow.remove();
    return;
  }

  const detailRow = document.createElement('tr');
  detailRow.setAttribute('id', detailRowId);

  const td = document.createElement('td');
  td.setAttribute('colspan', '8');
  td.className = 'bg-light border-top border-bottom text-secondary';
  td.innerHTML = '<div class="py-2">불러오는 중...</div>';

  detailRow.appendChild(td);
  row.insertAdjacentElement('afterend', detailRow);

  try {
    const response = await axios.post(`/ajax/building/listing/rooms`, {
    	 address: address
    });
    const list = response.data;

    if (Array.isArray(list) && list.length > 0) {
      td.classList.remove('text-secondary');
      td.innerHTML = list.map(room => `
        <div class="mb-2 px-3 py-2 border rounded bg-white shadow-sm">
          <div><strong>호수:</strong> \${room.lstgRoomNum || '-'}</div>
          <div>
            <strong>보증금:</strong> \${room.lstgLease || '-'} &nbsp; 
            <strong>월세:</strong> \${room.lstgLeaseM || '-'} &nbsp; 
            <strong>매매가:</strong> \${room.lstgLeaseAmt || '-'}
          </div>
        </div>
      `).join('');
    } else {
      td.innerHTML = '<div class="py-2 text-muted">해당 주소의 다른 호수가 없습니다.</div>';
    }
  } catch (err) {
    console.error(err);
    td.innerHTML = '<div class="py-2 text-danger">호수 정보를 불러오는 중 오류가 발생했습니다.</div>';
  }
});
</script>


</body>
</html>