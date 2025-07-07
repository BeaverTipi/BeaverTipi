<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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

    .chat-popup-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 0.5rem;
    }

    .chat-logo img {
      height: 32px;
    }

    .chat-header-actions {
      display: flex;
      gap: 0.5rem;
    }

    .chat-toggle-section {
      margin-bottom: 1rem;
      text-align: right;
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

    /* ✅ 공통 버튼 스타일 */
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
    <!-- 상단: 로고 + 채팅방 개설 버튼 -->
    <div class="chat-popup-header">
      <div class="chat-logo">
        <img src="/static/img/logo.png" alt="로고" />
      </div>
      <div class="chat-header-actions">
        <button id="createChatBtn" class="chat-btn">➕ 채팅방 개설</button>
      </div>
    </div>

    <!-- 공개 채팅방 보기 버튼 -->
    <div class="chat-toggle-section">
      <button id="togglePublicBtn" class="chat-btn">🌐 공개 채팅방 보기</button>
    </div>

    <!-- 채팅방 목록 -->
    <div class="chat-room-list" id="chatRoomList">
      <c:choose>
        <c:when test="${empty chatRoomList}">
          <div class="chat-empty-message">
            참여 중인 채팅이 없습니다.<br />
            멤버와 채팅을 시작해보세요.
          </div>
        </c:when>
        <c:otherwise>
          <c:forEach var="room" items="${chatRoomList}">
            <div class="chat-room-item" data-room-id="${room.id}">
              <div class="chat-room-name">${room.name}</div>
              <div class="chat-room-last-message">${room.lastMessage}</div>
            </div>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </div>
  </div>

  <script>
    let showingPublic = false;

    document.addEventListener("DOMContentLoaded", () => {
      document.getElementById("createChatBtn").addEventListener("click", () => {
        alert("채팅방 개설 기능은 아직 구현 중입니다.");
      });

      document.getElementById("togglePublicBtn").addEventListener("click", () => {
        showingPublic = !showingPublic;

        if (showingPublic) {
          document.getElementById("togglePublicBtn").innerText = "👥 참여 중인 채팅방 보기";
          loadPublicChatRooms();
        } else {
          document.getElementById("togglePublicBtn").innerText = "🌐 공개 채팅방 보기";
          loadMyChatRooms();
        }
      });

      document.querySelectorAll(".chat-room-item").forEach(item => {
        item.addEventListener("click", () => {
          const roomId = item.dataset.roomId;
          alert(`채팅방 ${roomId} 열기`);
          // TODO: 채팅방 메시지창 열기
        });
      });
    });

    function loadPublicChatRooms() {
      // TODO: AJAX로 공개 채팅방 목록 불러오기
      alert("공개 채팅방 목록 로딩");
    }

    function loadMyChatRooms() {
      // TODO: AJAX로 참여 중인 채팅방 목록 불러오기
      alert("참여 중인 채팅방 목록 로딩");
    }
  </script>
</body>
</html>