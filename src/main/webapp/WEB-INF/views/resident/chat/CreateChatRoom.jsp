<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>채팅방 개설</title>
</head>
<body>
  <h2>채팅방 개설</h2>

  <form id="chatRoomForm" action="/resident/chat/createRoom" method="post">
    <label>건물 선택:
      <select id="buildingSelect" name="bldgId" required></select>
    </label><br><br>

    <label>채팅방 제목:
      <input type="text" name="residentChatRoomTitle" required />
    </label><br><br>

    <label>
      <input type="checkbox" name="residentChatRoomIsPublic" value="Y" />
      공개 채팅방
    </label><br><br>

    <button type="button" id="selectMembersBtn">입주민 선택</button>
    <div id="selectedMemberList"><p>선택된 입주민이 없습니다.</p></div><br>

    <button type="submit">개설</button>
    <button type="button" onclick="window.history.back()">취소</button>
  </form>

  <script src="${pageContext.request.contextPath}/app/js/chat/chatCreate.js"></script>
</body>
</html>