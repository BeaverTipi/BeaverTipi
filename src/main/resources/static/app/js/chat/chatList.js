/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      		수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 5.     	김재윤				최초 작성
 * 2025. 7. 8.      김재윤               채팅방 개설 과정 구현
 * 
 * </pre>
 */

let showingPublic = false; // 기본값: 참여 채팅방

document.addEventListener("DOMContentLoaded", () => {
  const createBtn = document.querySelector("#createChatBtn");
  const toggleBtn = document.querySelector("#togglePublicBtn");
  const buildingSelect = document.querySelector("#buildingSelect");

  // 1. 채팅방 개설 버튼
  createBtn?.addEventListener("click", () => {
    window.location.href = "/resident/chat/createRoom?popup=true";
  });

  // 공개 / 참여 채팅 토글
  toggleBtn?.addEventListener("click", () => {
    showingPublic = !showingPublic;
    updateToggleButton();
    loadChatRooms();
  });

  // 채팅 방 목록 갱신
  buildingSelect?.addEventListener("change", loadChatRooms);

  // 초기화
  updateToggleButton();
  loadBuildingOptions(); // 입주 중인 건물 목록 불러오기
});

// 토글 버튼
function updateToggleButton() {
  const toggleBtn = document.querySelector("#togglePublicBtn");
  toggleBtn.innerText = showingPublic
    ? "👥 참여 중인 채팅방 보기"
    : "🌐 공개 채팅방 보기";
}

// 입주 중인 건물 목록
function loadBuildingOptions() {
  fetch("/resident/chat/residentBuilding")
    .then(res => {
      if (!res.ok) throw new Error("건물 목록 요청 실패");
      return res.json();
    })
    .then(buildings => {
      const buildingSelect = document.querySelector("#buildingSelect");
      buildingSelect.innerHTML = "";

      buildings.forEach(bldg => {
        const option = document.createElement("option");
        option.value = bldg.bldgId;
        option.textContent = bldg.bldgNm;
        buildingSelect.appendChild(option);
      });

      if (buildings.length > 0) {
        loadChatRooms();
      } else {
        renderEmptyMessage("입주 중인 건물이 없습니다.");
      }
    })
    .catch(err => {
      console.error("건물 목록 로딩 실패:", err);
      renderEmptyMessage("건물 목록을 불러오지 못했습니다.");
    });
}

// 채팅방 목록 로딩
function loadChatRooms() {
  const buildingSelect = document.querySelector("#buildingSelect");
  const chatRoomList = document.querySelector("#chatRoomList");
  const bldgId = buildingSelect?.value || "";

  if (!bldgId) {
    renderEmptyMessage("건물을 선택해주세요.");
    return;
  }

  fetch(`/resident/chat/list?bldgId=${bldgId}`)
    .then(res => {
      if (!res.ok) throw new Error("채팅방 목록 요청 실패");
      return res.json();
    })
    .then(renderChatRoomList)
    .catch(err => {
      console.error("채팅방 목록 로딩 실패:", err);
      renderEmptyMessage("채팅방 목록을 불러오지 못했습니다.");
    });
}

// 채팅방 목록 렌더링
function renderChatRoomList(chatRooms) {
  const chatRoomList = document.querySelector("#chatRoomList");
  chatRoomList.innerHTML = "";

  if (chatRooms.length === 0) {
    renderEmptyMessage("참여 중인 채팅방이 없습니다.<br />새로운 채팅을 시작해보세요.");
    return;
  }

  chatRooms.forEach(room => {
    chatRoomList.appendChild(createChatRoomItem(room));
  });
}

// 🔧 채팅방 항목 DOM 생성
function createChatRoomItem(room) {
  const item = document.createElement("div");
  item.className = "chat-room-item";
  item.dataset.roomId = room.residentChatRoomId;

  item.innerHTML = `
    <div class="chat-room-name">${room.residentChatRoomTitle}</div>
    <div class="chat-room-last-message">
      ${room.lastMessage || "최근 메시지가 없습니다."}
    </div>
  `;

  item.addEventListener("dblclick", () => {
    const roomId = item.dataset.roomId;
    const popupUrl = `/resident/chat/room?residentChatRoomId=${roomId}&popup=true`;
    window.open(popupUrl, "chatRoomPopup", "width=450,height=600,scrollbars=yes");
  });

  return item;
}

// 공통 빈 메시지 렌더링
function renderEmptyMessage(message) {
  const chatRoomList = document.querySelector("#chatRoomList");
  chatRoomList.innerHTML = `
    <div class="chat-empty-message">${message}</div>
  `;
}

// 목록 수동 갱신 함수
function refreshChatRoomList() {
  const buildingSelect = document.querySelector("#buildingSelect");
  const bldgId = buildingSelect?.value;

  if (!bldgId) {
    console.warn("❌ 채팅 목록 갱신 실패: 건물이 선택되지 않았습니다.");
    renderEmptyMessage("건물을 선택해주세요.");
    return;
  }

  fetch(`/resident/chat/list?bldgId=${bldgId}`)
    .then(res => res.json())
    .then(renderChatRoomList)
    .catch(err => {
      console.error("❌ 목록 갱신 실패:", err);
    });
}

window.refreshChatRoomList = refreshChatRoomList;

function updateChatRoomPreview(roomId, sender, content) {
  const item = document.querySelector(`[data-room-id="${roomId}"]`);
  if (!item) return;

  const preview = item.querySelector(".chat-room-last-message");
  if (preview) {
    preview.textContent = `${sender} : ${content}`;
  }

  // 선택적 정렬 처리 로직 붙일 수 있음
  // window.refreshChatRoomList(); // 강제 전체 갱신 시
}

// ✅ 채팅 목록 WebSocket 연결
const chatListSocket = new WebSocket("ws://" + location.host + "/ws/chatlist");

chatListSocket.onmessage = (event) => {
  const data = JSON.parse(event.data);

  if (data.type === "messagePreview") {
    updateChatRoomPreview(data.residentChatRoomId, data.sender, data.content);
  } else if (data.type === "newRoom") {
    window.refreshChatRoomList();
  }
};

chatListSocket.onopen = () => {
  console.log("✅ 채팅 목록 WebSocket 연결 성공");
};

chatListSocket.onclose = () => {
  console.log("❌ 채팅 목록 WebSocket 연결 종료");
};