<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>채팅</title>
  <style>
    .chat-popup-wrapper {
      font-family: 'Noto Sans KR', sans-serif;
      padding: 1rem;
      background-color: #f5f5f5;
      height: 100vh;
      box-sizing: border-box;
    }

    .chat-popup-header-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      grid-template-rows: auto auto;
      gap: 0.5rem 1rem;
      margin-bottom: 1rem;
      align-items: center;
    }

    .chat-logo {
      grid-column: 1 / 2;
      grid-row: 1 / 2;
    }

    .chat-logo img {
      height: 32px;
    }

    .chat-header-actions {
      grid-column: 2 / 3;
      grid-row: 1 / 2;
      justify-self: end;
    }

    .building-select-area {
      grid-column: 1 / 2;
      grid-row: 2 / 3;
    }

    .building-select-area select {
      padding: 0.4rem 0.6rem;
      font-size: 0.9rem;
      border-radius: 6px;
      border: 1px solid #ccc;
    }

    .chat-toggle-section {
      grid-column: 2 / 3;
      grid-row: 2 / 3;
      justify-self: end;
    }

    .chat-room-list {
      max-height: calc(100vh - 120px);
      overflow-y: auto;
    }

    .chat-empty-message {
      padding: 2rem;
      text-align: center;
      color: #999;
      font-size: 0.95rem;
      background-color: #fff;
      border-radius: 8px;
    }

    .chat-room-item {
      background-color: #fff;
      padding: 1rem;
      margin-bottom: 0.5rem;
      border-radius: 8px;
      cursor: pointer;
      transition: background-color 0.2s;
    }

    .chat-room-item:hover {
      background-color: #e6f0ff;
    }

    .chat-room-name {
      font-weight: bold;
      font-size: 1rem;
    }

    .chat-room-last-message {
      font-size: 0.85rem;
      color: #666;
      margin-top: 0.25rem;
    }

    .chat-btn {
      width: 160px;
      padding: 0.5rem 1rem;
      font-size: 0.9rem;
      background-color: #ffffff;
      border: 1px solid #ccc;
      border-radius: 6px;
      cursor: pointer;
      transition: background-color 0.2s;
      text-align: center;
    }

    .chat-btn:hover {
      background-color: #e6f0ff;
    }
  </style>
</head>
<body>
  <div class="chat-popup-wrapper">


    <div class="chat-popup-header-grid">


      <div class="chat-logo">
        <img src="/volt/assets/img/brand/dark.png" alt="채팅방 로고"/>
      </div>


      <div class="chat-header-actions">
        <button id="createChatBtn" class="chat-btn">➕ 채팅방 개설</button>
      </div>


      <div class="building-select-area">
        <label for="buildingSelect" class="visually-hidden">건물 선택</label>
        <select id="buildingSelect" name="selectedBldgId">

        </select>
      </div>


      <div class="chat-toggle-section">
        <button id="togglePublicBtn" class="chat-btn">🌐 공개 채팅방 보기</button>
      </div>
    </div>


    <div class="chat-room-list" id="chatRoomList">
      <div class="chat-empty-message">
        채팅방 목록을 불러오는 중입니다...
      </div>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/app/js/chat/chatList.js"></script>
</body>
</html>