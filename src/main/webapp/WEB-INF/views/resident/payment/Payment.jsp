<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>납부 화면</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/css/theme.css'/>"/>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f8f9fa;
            color: #212529;
        }

        .container {
            max-width: 960px;
            margin: 50px auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .tabs {
            display: flex;
            margin-bottom: 20px;
            border-bottom: 2px solid #ddd;
        }

        .tab-button {
            padding: 15px 30px;
            cursor: pointer;
            border: 1px solid transparent;
            background-color: #f0f0f0;
            font-size: 18px;
            font-weight: bold;
            color: #555;
            transition: background-color 0.3s ease;
        }

        .tab-button.active {
            background-color: #ffffff;
            border-color: #ddd;
            border-bottom: 2px solid #007bff;
            color: #007bff;
        }

        .tab-content {
            padding-top: 20px;
            display: none;
        }

        #content2.tab-content.active {
            display: block;
        }

        .payment-method {
            margin-bottom: 30px;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #f9f9f9;
        }

        .payment-method h3 {
            margin-top: 0;
            color: #007bff;
        }

        .payment-options input[type="radio"] {
            margin-right: 10px;
        }

        .payment-options label {
            font-size: 18px;
            cursor: pointer;
            margin-right: 20px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }

        table th, table td {
            border: 1px solid #ddd;
            padding: 14px;
            text-align: left;
        }

        table th {
            background-color: #f2f2f2;
            font-weight: bold;
            color: #444;
        }

        table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        table tr:hover {
            background-color: #f1f1f1;
        }

        .completed-row {
            color: #999;
            background-color: #f0f0f0;
        }

        .footer-buttons {
            text-align: center;
            margin-top: 20px;
        }

        .footer-buttons button {
            padding: 15px 30px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-size: 18px;
            font-weight: bold;
            margin: 0 10px;
        }

        .footer-buttons .pay-button {
            background-color: #28a745;
            color: white;
        }

        .footer-buttons .pay-button:hover {
            background-color: #218838;
        }

        .footer-buttons .cancel-button {
            background-color: #6c757d;
            color: white;
        }

        .footer-buttons .cancel-button:hover {
            background-color: #5a6268;
        }

    </style>
</head>
<body>
    <body>
    <div class="container">
        <div class="tabs">
            <button class="tab-button active">납부</button>
            <form method="get" action="/resident/notice" id="noticeSearchForm">
                <input type="hidden" name="page" value="${pagingInfo.currentPageNo}">
                <!-- 🔽 건물 선택 -->
                <select name="bldgIdParam" onchange="document.getElementById('noticeSearchForm').submit();">
                    <c:forEach var="unit" items="${unitList}">
                        <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
                            ${unit.building.bldgNm}
                        </option>
                    </c:forEach>
                </select>
            </form>
        </div>

        <div id="content1" class="tab-content">
            <div class="payment-method">
                <h3>결제 방식 선택</h3>
                <div class="payment-options">
                    <input type="radio" id="card_pay" name="payment_method" value="card" checked>
                    <label for="card_pay">카드 납부</label>
                    <input type="radio" id="virtual_account_pay" name="payment_method" value="virtual_account">
                    <label for="virtual_account_pay">가상계좌 입금</label>
                </div>
            </div>

            <h2>납부 내역 (전전월)</h2>
            <c:if test="${not empty chargeBillListBeforeLastMonth}">
                <table border="1">
                    <thead>
                        <tr>
                            <th>청구서 ID</th>
                            <th>청구 월</th>
                            <th>청구 금액</th>
                            <th>상태</th>
                            <th>납부 기한</th>
                            <th>설명</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="chargeBill" items="${chargeBillListBeforeLastMonth}">
                            <tr>
                                <td>${chargeBill.chgbillId}</td>
                                <td>${chargeBill.chgbillChargeMonth}</td>
                                <td>${chargeBill.chgbillAmount}</td>
                                <td>${chargeBill.chgbillStatus}</td>
                                <td>${chargeBill.chgbillDueDate}</td>
                                <td>${chargeBill.chgbillDesc}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <h2>납부 내역 (전월)</h2>
            <c:if test="${not empty chargeBillListLastMonth}">
                <table border="1">
                    <thead>
                        <tr>
                            <th>청구서 ID</th>
                            <th>청구 월</th>
                            <th>청구 금액</th>
                            <th>상태</th>
                            <th>납부 기한</th>
                            <th>설명</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="chargeBill" items="${chargeBillListLastMonth}">
                            <tr>
                                <td>${chargeBill.chgbillId}</td>
                                <td>${chargeBill.chgbillChargeMonth}</td>
                                <td>${chargeBill.chgbillAmount}</td>
                                <td>${chargeBill.chgbillStatus}</td>
                                <td>${chargeBill.chgbillDueDate}</td>
                                <td>${chargeBill.chgbillDesc}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

        </div>

        <div class="footer-buttons">
            <button class="pay-button">납부하기</button>
            <button class="cancel-button">납부 취소</button>
        </div>
    </div>
</body>
</html>
