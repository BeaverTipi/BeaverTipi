<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>내 매물 정보</title>
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      padding: 2rem;
      background-color: #f9f9f9;
    }

    h1.page-title {
      font-size: 24px;
      font-weight: bold;
      margin-bottom: 20px;
    }

    .tab-menu {
      display: flex;
      gap: 8px;
      margin-bottom: 20px;
    }

    .tab-button {
      font-size: 14px;
      padding: 6px 14px;
      border: 1px solid #ccc;
      border-radius: 4px;
      background: #fff;
      cursor: pointer;
    }

    .tab-button.active {
      background-color: #007bff;
      color: white;
      border-color: #007bff;
    }

    .listing-container {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;
    }

    .listing-card {
      width: 23%;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 6px;
      overflow: hidden;
      box-shadow: 0 1px 3px rgba(0,0,0,0.08);
      display: flex;
      flex-direction: column;
      position: relative;
    }

    .listing-card.inactive {
      opacity: 0.45;
      pointer-events: none;
    }

    .listing-card.inactive::after {
      content: attr(data-status-label);
      position: absolute;
      top: 45%;
      left: 0;
      right: 0;
      text-align: center;
      font-weight: bold;
      background-color: rgba(255,255,255,0.85);
      padding: 10px;
      color: #333;
      font-size: 14px;
    }

    .listing-img {
      width: 100%;
      height: 150px;
      object-fit: cover;
    }

    .listing-content {
      padding: 10px;
      font-size: 13px;
      flex-grow: 1;
    }

    .listing-title {
      font-weight: bold;
      font-size: 14px;
      margin-bottom: 4px;
    }

    .listing-price {
      color: #000;
      margin-bottom: 4px;
    }

    .listing-desc {
      font-size: 12px;
      color: #555;
      height: 32px;
      overflow: hidden;
      text-overflow: ellipsis;
    }

    .card-actions {
      display: flex;
      justify-content: flex-end;
      gap: 4px;
      padding: 0 10px 10px;
    }

    .card-actions button {
      font-size: 11px;
      padding: 4px 8px;
      border: 1px solid #ccc;
      background: #f5f5f5;
      border-radius: 3px;
      cursor: pointer;
    }

    .card-actions button:hover {
      background: #e0e0e0;
    }

    .summary-text {
      margin-bottom: 1rem;
      font-size: 14px;
    }
	    <style>
	  .filter-menu {
	    display: flex;
	    gap: 8px;
	    margin-bottom: 20px;
	  }
	
	  .filter-button {
	    font-size: 13px;
	    padding: 5px 12px;
	    border: 1px solid #ccc;
	    border-radius: 4px;
	    background: #fff;
	    cursor: pointer;
	  }
	
	  .filter-button.active {
	    background-color: #28a745;
	    color: white;
	    border-color: #28a745;
	  }
</style>
  </style>
</head>
<body>

  <h1 class="page-title">내 매물 정보</h1>

  <div class="tab-menu">
    <button class="tab-button active" onclick="showTab('active', event)">현재 매물</button>
    <button class="tab-button" onclick="showTab('inactive', event)">지난 매물</button>
    <button class="tab-button" onclick="showTab('all', event)">전체 보기</button>
  </div>
	<div class="filter-menu">
	  <button class="filter-button active" onclick="filterType('all', event)">전체</button>
	  <button class="filter-button" onclick="filterType('1', event)">전세</button>
	  <button class="filter-button" onclick="filterType('2', event)">월세</button>
	  <button class="filter-button" onclick="filterType('3', event)">매매</button>
	</div>
  <c:choose>
    <c:when test="${empty listingProductList}">
      <p>등록된 매물이 없습니다.</p>
    </c:when>
    <c:otherwise>
      <div class="summary-text">
        총 <strong>${fn:length(listingProductList)}</strong>개의 매물이 있습니다.
      </div>

      <div class="listing-container">
        <c:forEach var="listing" items="${listingProductList}">
          <c:set var="statusLabel">
            <c:choose>
              <c:when test="${listing.lstgProdStat == 2}">삭제됨</c:when>
              <c:when test="${listing.lstgProdStat == 3}">비공개</c:when>
              <c:otherwise></c:otherwise>
            </c:choose>
          </c:set>

          <div class="listing-card
            <c:if test='${listing.lstgProdStat != 1}'>inactive</c:if>"
            data-status="${listing.lstgProdStat}"
            data-status-label="${statusLabel}">

            <img class="listing-img"
                 src="/images/no-image.jpg"
                 alt="${listing.lstgNm}" />

            <div class="listing-content">
              <div class="listing-title">${listing.lstgNm}</div>

              <div class="listing-price">
                <c:choose>
                  <c:when test="${listing.lstgTypeSale == 1}">전세</c:when>
                  <c:when test="${listing.lstgTypeSale == 2}">월세</c:when>
                  <c:when test="${listing.lstgTypeSale == 3}">매매</c:when>
                  <c:otherwise>기타</c:otherwise>
                </c:choose>
                ${listing.lstgLease}/${listing.lstgLeaseM}
              </div>

              <div class="listing-desc">
                <c:choose>
                  <c:when test="${fn:length(listing.lstgDst) > 60}">
                    ${fn:substring(listing.lstgDst, 0, 60)}...
                  </c:when>
                  <c:otherwise>
                    ${listing.lstgDst}
                  </c:otherwise>
                </c:choose>
              </div>
            </div>

            <div class="card-actions">
              <button onclick="location.href='/building/product/detail?lstgId=${listing.lstgId}'">상세</button>
              <button onclick="location.href='/building/product/update?lstgId=${listing.lstgId}'">수정</button>
              <button onclick="location.href='/building/product/delete?lstgId=${listing.lstgId}'">삭제</button>
            </div>
          </div>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>

  <script>
    function showTab(type, event) {
      // 버튼 스타일 초기화
      document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
      event.target.classList.add('active');

      const rows = document.querySelectorAll('.listing-card');
      rows.forEach(card => {
        const status = card.dataset.status;
        if (type === 'all') {
          card.style.display = 'flex';
        } else if (type === 'active' && status == '1') {
          card.style.display = 'flex';
        } else if (type === 'inactive' && status != '1') {
          card.style.display = 'flex';
        } else {
          card.style.display = 'none';
        }
      });
    }
    let currentTab = 'active';
    let currentType = 'all';

    function showTab(type, event) {
      currentTab = type;

      // 탭 스타일
      document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
      event.target.classList.add('active');

      applyFilters();
    }

    function filterType(type, event) {
      currentType = type;

      // 필터 버튼 스타일
      document.querySelectorAll('.filter-button').forEach(btn => btn.classList.remove('active'));
      event.target.classList.add('active');

      applyFilters();
    }

    function applyFilters() {
      const rows = document.querySelectorAll('.listing-card');
      rows.forEach(card => {
        const status = card.dataset.status;
        const type = card.querySelector('.listing-price')?.textContent.trim();

        const typeMatched = (currentType === 'all') ||
          (currentType === '1' && type.startsWith('전세')) ||
          (currentType === '2' && type.startsWith('월세')) ||
          (currentType === '3' && type.startsWith('매매'));

        const statusMatched =
          (currentTab === 'all') ||
          (currentTab === 'active' && status === '1') ||
          (currentTab === 'inactive' && status !== '1');

        card.style.display = (typeMatched && statusMatched) ? 'flex' : 'none';
      });
    }
  </script>

</body>
</html>
