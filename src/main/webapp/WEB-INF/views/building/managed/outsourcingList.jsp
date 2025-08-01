<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>플랫폼 입점업체</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedList.css">
  <style>
    :root {
      --main: #00aaff;
      --main-dark: #008fcc;
      --header-bg: #f1f5fa;
      --border: #e0e0e0;
      --table-bg: #fff;
      --row-hover: #f5fbff;
      --btn-edit: #eaf0ff;
      --btn-delete: #fdf0f0;
      --btn-edit-txt: #2543b5;
      --btn-delete-txt: #d40023;
      --link: #007bff;
      --link-hover: #0056b3;
      --table-radius: 10px;
    }
    body {
      font-family: 'Noto Sans KR', Arial, sans-serif;
      background: #f8fbfe;
      margin: 0; padding: 36px 0 0 0;
    }
    .catalog-wrap {
      max-width: 1200px; margin: 0 auto; background: #fff;
      border-radius: 14px; box-shadow: 0 2px 8px #0001; padding: 0 0 40px 0;
    }
    h2 {
      padding: 38px 44px 12px 44px; margin: 0 0 10px 0; font-size: 25px; color: #222; font-weight: 700;
    }
    .tabs {
      display: flex; border-bottom: 2px solid var(--border); margin: 0 44px 22px 44px; background: #f8fbfe;
      border-radius: 14px 14px 0 0; overflow: hidden;
    }
    .tab-btn {
      flex: 1; padding: 18px 0 12px 0; text-align: center; font-size: 17px;
      font-weight: 600; color: #bbb; background: none; border: none; outline: none; cursor: pointer;
      border-bottom: 2px solid transparent; transition: color .16s, border .15s, background .15s;
    }
    .tab-btn.active {
      color: var(--main); background: #fff; border-bottom: 2.5px solid var(--main); z-index: 2;
    }
    .tab-content { display: none; }
    .tab-content.active { display: block; }

    .company-table-wrap {
      padding: 24px 44px 0 44px;
    }
    .company-table {
      width: 100%; border-collapse: separate; border-spacing: 0;
      background: var(--table-bg); border-radius: var(--table-radius); overflow: hidden;
      box-shadow: 0 1px 4px #0001;
      font-size: 15px;
    }
    .company-table th, .company-table td {
      border: 1px solid var(--border);
      text-align: center; vertical-align: middle; background: var(--table-bg);
    }
    .company-table th {
      background: var(--header-bg); color: #222; font-size: 15.5px; font-weight: 700; padding: 13px 0;
    }
    .company-table td { padding: 12px 8px; }
    .company-table tbody tr:hover { background: var(--row-hover);}
    .company-logo {
      width: 46px; height: 46px; border-radius: 8px; object-fit: cover; border: 1px solid #e4e7ef; background: #fafbff;
      vertical-align: middle; margin-right: 14px;
    }
    .company-name-cell { display: flex; align-items: center; justify-content: left; gap: 12px; }
    .download-link { color: var(--link); text-decoration: none; font-weight: 500; }
    .download-link:hover { text-decoration: underline; color: var(--link-hover);}
    .action-btn {
      padding: 5px 15px; border-radius: 5px; font-size: 14px; font-weight: 500; border: none; cursor: pointer;
    }
    .btn-edit { background: var(--btn-edit); color: var(--btn-edit-txt);}
    .btn-delete { background: var(--btn-delete); color: var(--btn-delete-txt);}
    @media (max-width: 900px) {
      .catalog-wrap, .company-table-wrap { padding: 0 8px 0 8px; }
      h2, .tabs { margin-left: 8px; margin-right: 8px; }
    }
  </style>
  <script>
    function showTab(idx) {
      document.querySelectorAll('.tab-btn').forEach((btn, i) => {
        btn.classList.toggle('active', i === idx);
      });
      document.querySelectorAll('.tab-content').forEach((tab, i) => {
        tab.classList.toggle('active', i === idx);
      });
    }
    window.addEventListener('DOMContentLoaded', () => showTab(0));
  </script>
</head>
<body>
<div class="catalog-wrap">
  <h2>플랫폼 입점업체</h2>
  <!-- 탭 -->
  <div class="tabs">
    <button type="button" class="tab-btn active" onclick="showTab(0)">승강기</button>
    <button type="button" class="tab-btn" onclick="showTab(1)">냉/난방</button>
    <button type="button" class="tab-btn" onclick="showTab(2)">청소/미화</button>
    <button type="button" class="tab-btn" onclick="showTab(3)">보안</button>
    <button type="button" class="tab-btn" onclick="showTab(4)">기타공사</button>
  </div>

  <!-- 승강기 업체 리스트 -->
  <div class="tab-content active">
    <div class="company-table-wrap">
      <table class="company-table">
        <thead>
          <tr>
            <th>순번</th>
            <th>업체</th>
            <th>업체코드</th>
            <th>대표자명</th>
            <th>연락처</th>
            <th>입점일</th>
            <th>계약서</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>
              <div class="company-name-cell">
                <img src="/images/elevator-company.jpg" alt="승강기업체" class="company-logo">
                <span>엘리베이터 주식회사</span>
              </div>
            </td>
            <td>ELV001</td>
            <td>최상승</td>
            <td>010-7777-8888</td>
            <td>2024-01-25</td>
            <td><a href="#" class="download-link">📥 다운로드</a></td>
            <td>
              <button class="action-btn btn-edit">수정</button>
              <button class="action-btn btn-delete">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 냉/난방 업체 리스트 -->
  <div class="tab-content">
    <div class="company-table-wrap">
      <table class="company-table">
        <thead>
          <tr>
            <th>순번</th>
            <th>업체</th>
            <th>업체코드</th>
            <th>대표자명</th>
            <th>연락처</th>
            <th>입점일</th>
            <th>계약서</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>
              <div class="company-name-cell">
                <img src="/images/facility-company.jpg" alt="냉난방업체" class="company-logo">
                <span>쿨앤웜 에너지</span>
              </div>
            </td>
            <td>HVA001</td>
            <td>김냉난</td>
            <td>010-5555-9999</td>
            <td>2023-07-11</td>
            <td><a href="#" class="download-link">📥 다운로드</a></td>
            <td>
              <button class="action-btn btn-edit">수정</button>
              <button class="action-btn btn-delete">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 청소/미화 업체 리스트 -->
  <div class="tab-content">
    <div class="company-table-wrap">
      <table class="company-table">
        <thead>
          <tr>
            <th>순번</th>
            <th>업체</th>
            <th>업체코드</th>
            <th>대표자명</th>
            <th>연락처</th>
            <th>입점일</th>
            <th>계약서</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>
              <div class="company-name-cell">
                <img src="/images/facility-company.jpg" alt="청소/미화업체" class="company-logo">
                <span>클린빌 시설관리</span>
              </div>
            </td>
            <td>FAC001</td>
            <td>정청소</td>
            <td>010-2345-1112</td>
            <td>2022-03-18</td>
            <td><a href="#" class="download-link">📥 다운로드</a></td>
            <td>
              <button class="action-btn btn-edit">수정</button>
              <button class="action-btn btn-delete">삭제</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 보안 업체 리스트 -->
  <div class="tab-content">
    <div class="company-table-wrap">
      <table class="company-table">
        <thead>
          <tr>
            <th>순번</th>
            <th>업체</th>
            <th>업체코드</th>
            <th>대표자명</th>
            <th>연락처</th>
            <th>입점일</th>
            <th>계약서</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>1</td>
            <td>
              <div class="company-name-cell">
                <img src="/images/security-company.jpg" alt="보안업체" class="company-logo">
                <span>세이프가드 보안</span>
              </div>
            </td>
            <td>SEC001</td>
            <td>홍보안</td>
            <td>010-1234-0001</td>
            <td>2021-11-05</td>
            <td><a href="#" class="download-link">📥 다운로드</a></td>
            <td>
              <button class="action-btn btn-edit"
