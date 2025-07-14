let showingPublic = false;
const chatRoomStates = [];

document.addEventListener("DOMContentLoaded", () => {
  const createBtn = document.querySelector("#createChatBtn");
  const toggleBtn = document.querySelector("#togglePublicBtn");
  const buildingSelect = document.querySelector("#buildingSelect");

  createBtn?.addEventListener("click", () => {
    window.location.href = "/resident/chat/createRoom?popup=true";
  });

  toggleBtn?.addEventListener("click", () => {
    showingPublic = !showingPublic;
    updateToggleButton();
    loadChatRooms();
  });

  buildingSelect?.addEventListener("change", loadChatRooms);

  updateToggleButton();
  loadBuildingOptions();
  initializeSocket();
});

function initializeSocket() {
  try {
    const socket = new WebSocket("ws://" + location.host + "/ws/chatList");

    socket.onmessage = (event) => {
      const msg = JSON.parse(event.data);
      if (msg.type === "messagePreview") {
        const state = chatRoomStates.find(r => r.room.residentChatRoomId === msg.residentChatRoomId);
        if (state) {
          state.lastMessage = msg.rcmCont || "";
          state.rcmTime = msg.rcmTime || "";
          state.unitRoom = msg.unitRoom || "";
          state.mbrNnm = msg.mbrNnm || "";
          sortAndRenderChatRooms();
        }
      }
    };
  } catch (e) {
    console.warn("❌ WebSocket 연결 실패:", e);
  }
}

function updateToggleButton() {
  const toggleBtn = document.querySelector("#togglePublicBtn");
  toggleBtn.innerText = showingPublic
    ? "👥 참여 중인 채팅방 보기"
    : "🌐 공개 채팅방 보기";
}

function loadBuildingOptions() {
  fetch("/resident/chat/residentBuilding")
    .then(res => res.json())
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

function loadChatRooms() {
  const buildingSelect = document.querySelector("#buildingSelect");
  const bldgId = buildingSelect?.value || "";

  if (!bldgId) {
    renderEmptyMessage("건물을 선택해주세요.");
    return;
  }

  fetch(`/resident/chat/list?bldgId=${bldgId}`)
    .then(res => res.json())
    .then(chatRooms => {
      const fetches = chatRooms.map(room =>
        fetch(`/resident/chat/lastMessage?residentChatRoomId=${room.residentChatRoomId}`)
          .then(res => res.text())
          .then(text => {
            if (!text.trim()) {
              return {
                room,
                lastMessage: "",
                rcmTime: "",
                unitRoom: "",
                mbrNnm: ""
              };
            }

            const msg = JSON.parse(text);
            
            console.log("메시지 응답 : ", {
				title: room.residentChatRoomTitle,
				rcmTime: msg.rcmTime,
				parsedTime: new Date(msg.rcmTime).getTime()
			});
			
            return {
              room,
              lastMessage: msg.rcmCont || "",
              rcmTime: msg.rcmTime || "",
              unitRoom: msg.unitRoom || "",
              mbrNnm: msg.mbrNnm || ""
            };
          })
          .catch(err => {
            console.warn("❌ 메시지 fetch 실패:", err);
            return {
              room,
              lastMessage: "",
              rcmTime: "",
              unitRoom: "",
              mbrNnm: ""
            };
          })
      );

      Promise.all(fetches).then(results => {
        const sorted = results.sort((a, b) => {
          const timeA = a.rcmTime ? new Date(a.rcmTime).getTime() : 0;
          const timeB = b.rcmTime ? new Date(b.rcmTime).getTime() : 0;
          return timeB - timeA;
        });

        chatRoomStates.length = 0;
        sorted.forEach(r => chatRoomStates.push(r));
        sortAndRenderChatRooms();
      });
    })
    .catch(err => {
      console.error("채팅방 목록 로딩 실패:", err);
      renderEmptyMessage("채팅방 목록을 불러오지 못했습니다.");
    });
}

function sortAndRenderChatRooms() {
  const chatRoomList = document.querySelector("#chatRoomList");
  chatRoomList.innerHTML = "";

  const sorted = chatRoomStates.slice().sort((a, b) => {
    const timeA = a.rcmTime ? new Date(a.rcmTime).getTime() : 0;
    const timeB = b.rcmTime ? new Date(b.rcmTime).getTime() : 0;
    return timeB - timeA;
  });

  sorted.forEach(({ room, lastMessage, unitRoom, mbrNnm }) => {
    const hasMessage = lastMessage?.trim();
    const previewText = hasMessage
      ? `${unitRoom} ${mbrNnm} : ${lastMessage}`
      : "최근 메시지가 없습니다.";

    const item = document.createElement("div");
    item.className = "chat-room-item";
    item.dataset.roomId = room.residentChatRoomId;

    item.innerHTML = `
      <div class="chat-room-name">${room.residentChatRoomTitle}</div>
      <div class="chat-room-last-message">${previewText}</div>
    `;

    item.addEventListener("dblclick", () => {
      const popupUrl = `/resident/chat/room?residentChatRoomId=${room.residentChatRoomId}&popup=true`;
      window.open(popupUrl, "chatRoomPopup", "width=450,height=600,scrollbars=yes");
    });

    chatRoomList.appendChild(item);
  });
}

function renderEmptyMessage(message) {
  const chatRoomList = document.querySelector("#chatRoomList");
  chatRoomList.innerHTML = `<div class="chat-empty-message">${message}</div>`;
}

window.refreshChatRoomList = loadChatRooms;