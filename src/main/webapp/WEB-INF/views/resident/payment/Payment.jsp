<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>납부/결제 화면 (JavaScript 제외)</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            padding: 0;
            background-color: #f4f4f4;
            color: #333;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background-color: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        /* Tabs Styling - 디자인만, 기능 없음 */
        .tabs {
            display: flex;
            margin-bottom: 20px;
            border-bottom: 2px solid #ddd;
        }
        .tab-button {
            padding: 12px 20px;
            cursor: default; /* 클릭해도 반응 없도록 커서 변경 */
            border: 1px solid transparent;
            border-bottom: none;
            background-color: #eee;
            font-size: 16px;
            font-weight: bold;
            color: #555;
        }
        .tab-button.active { /* '결제' 탭이 활성화된 것처럼 보이도록 */
            background-color: #fff;
            border-color: #ddd;
            border-bottom: 2px solid #fff;
            color: #007bff;
        }
        
        /* Tab Content - '결제' 탭 내용만 항상 보이도록 설정 */
        /* 납부 탭 내용은 기본적으로 숨기거나 아예 제거해야 합니다. */
        /* 여기서는 '결제' 탭 내용만 div.tab-content로 감싸져 항상 보이게 합니다. */
        .tab-content {
            padding-top: 20px;
            display: none; /* 모든 탭 내용은 기본적으로 숨김 */
        }
        /* '결제' 탭 내용을 활성화된 것처럼 보이게 함 */
        #content2.tab-content.active {
            display: block;
        }


        .payment-method {
            margin-bottom: 30px;
            padding: 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .payment-method h3 {
            margin-top: 0;
            color: #555;
        }
        .payment-options input[type="radio"] {
            margin-right: 8px;
        }
        .payment-options label {
            font-size: 16px;
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
            padding: 12px;
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
        /* 완료된 항목 스타일 */
        .completed-row {
            color: #999;
            background-color: #f0f0f0;
        }
        .completed-row input[type="checkbox"] {
            cursor: not-allowed;
        }
        .footer-buttons {
            text-align: center;
            margin-top: 20px;
        }
        .footer-buttons button {
            padding: 15px 30px;
            border: none;
            border-radius: 5px;
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
    <div class="container">
        <div class="tabs">
            <button class="tab-button">납부</button>
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

            <h2>납부 내역</h2>
            <table>
                <thead>
                    <tr>
                        <th><input type="checkbox" id="select_all_payment" disabled></th> <th>납부 내용</th>
                        <th>관리비 전달</th>
                        <th>관리비 납부 기한</th>
                        <th>납부 상태</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><input type="checkbox" name="payment_item_payment"></td>
                        <td>2025년 5월 관리비</td>
                        <td>2025-05-25</td>
                        <td>2025-06-10</td>
                        <td>미납</td>
                    </tr>
                    <tr class="completed-row">
                        <td><input type="checkbox" name="payment_item_payment" disabled></td>
                        <td>2025년 4월 관리비</td>
                        <td>2025-04-25</td>
                        <td>2025-05-10</td>
                        <td>납부 완료</td>
                    </tr>
                    <tr class="completed-row">
                        <td><input type="checkbox" name="payment_item_payment" disabled></td>
                        <td>2025년 3월 관리비</td>
                        <td>2025-03-25</td>
                        <td>2025-04-10</td>
                        <td>납부 완료</td>
                    </tr>
                </tbody>
            </table>

            <div class="footer-buttons">
                <button class="pay-button">납부하기</button>
                <button class="cancel-button">납부 취소</button>
            </div>
        </div>

        <div id="content2" class="tab-content active"> <div class="payment-method">
                <h3>결제 방식 선택</h3>
                <div class="payment-options">
                    <input type="radio" id="card_pay_2" name="payment_method_2" value="card" checked>
                    <label for="card_pay_2">신용카드</label>
                    <input type="radio" id="transfer_2" name="payment_method_2" value="transfer">
                    <label for="transfer_2">계좌이체</label>
                    <input type="radio" id="phone_2" name="payment_method_2" value="phone">
                    <label for="phone_2">휴대폰 결제</label>
                </div>
            </div>

            <h2>결제 내역</h2>
            <table>
                <thead>
                    <tr>
                        <th><input type="checkbox" id="select_all_checkout" disabled></th> <th>결제 항목</th>
                        <th>결제 금액</th>
                        <th>결제 일자</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="completed-row">
                        <td><input type="checkbox" name="payment_item_checkout" disabled></td>
                        <td>서비스 이용료</td>
                        <td>50,000원</td>
                        <td>2025-06-15</td>
                        <td>완료</td>
                    </tr>
                    <tr class="completed-row">
                        <td><input type="checkbox" name="payment_item_checkout" disabled></td>
                        <td>부가 서비스</td>
                        <td>10,000원</td>
                        <td>2025-06-01</td>
                        <td>완료</td>
                    </tr>
                    <tr>
                        <td><input type="checkbox" name="payment_item_checkout"></td>
                        <td>2025년 5월 관리비</td>
                        <td>75,000원</td>
                        <td>2025-05-20</td>
                        <td>미납</td>
                    </tr>
                </tbody>
            </table>
            <div class="footer-buttons">
                <button class="pay-button">결제하기</button>
                <button class="cancel-button">결제 취소</button>
            </div>
        </div>
    </div>
</body>
</html>