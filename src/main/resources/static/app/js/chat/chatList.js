const chatRoomStates = [];

document.addEventListener("DOMContentLoaded", () => {
  const createBtn = document.querySelector("#createChatBtn");
  const toggleBtn = document.querySelector("#toggleBrokerBtn");
  const buildingSelect = document.querySelector("#buildingSelect");

  createBtn?.addEventListener("click", () => {
    window.location.href = "/resident/chat/createRoom?popup=true";
  });

  toggleBtn?.addEventListener("click", () => {
    updateToggleButton();
    loadBrokerChatRooms();
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
        const state = chatRoomStates.find(
          r => r.type === "resident" && r.room.residentChatRoomId === msg.residentChatRoomId
        );
        if (state) {
          state.lastMessage = msg.rcmCont || "";
          state.rcmTime = msg.rcmTime || "";
          state.unitRoom = msg.unitRoom || "";
          state.mbrNnm = msg.mbrNnm || "";
          sortAndRenderChatRooms();
        }
      }

      if (msg.type === "newRoom") {
        chatRoomStates.push({
          type: "resident",
          room: {
            residentChatRoomId: msg.residentChatRoomId,
            residentChatRoomTitle: msg.residentChatRoomTitle
          },
          lastMessage: "",
          rcmTime: "",
          unitRoom: "",
          mbrNnm: ""
        });
        sortAndRenderChatRooms();
      }
    };
  } catch (e) {
    console.warn("❌ WebSocket 연결 실패:", e);
  }
}

function updateToggleButton() {
  const toggleBtn = document.querySelector("#toggleBrokerBtn");
  toggleBtn.innerText = "🧑‍💼 중개 채팅방 보기";
}

function loadBuildingOptions() {
  fetch("/resident/chat/residentBuilding")
    .then(res => res.json())
    .then(buildings => {
      const buildingSelect = document.querySelector("#buildingSelect");
      buildingSelect.innerHTML = "";

      const allOption = document.createElement("option");
      allOption.value = "ALL";
      allOption.textContent = "===건물 전체===";
      buildingSelect.appendChild(allOption);

      buildings.forEach(bldg => {
        const option = document.createElement("option");
        option.value = bldg.bldgId;
        option.textContent = bldg.bldgNm;
        buildingSelect.appendChild(option);
      });

      loadChatRooms();
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
            const msg = text.trim() ? JSON.parse(text) : {};
            return {
              type: "resident",
              room,
              lastMessage: msg.rcmCont || "",
              rcmTime: msg.rcmTime || "",
              unitRoom: msg.unitRoom || "",
              mbrNnm: msg.mbrNnm || ""
            };
          })
          .catch(() => ({
            type: "resident",
            room,
            lastMessage: "",
            rcmTime: "",
            unitRoom: "",
            mbrNnm: ""
          }))
      );

      Promise.all(fetches).then(results => {
        chatRoomStates.length = 0;
        results.forEach(r => chatRoomStates.push(r));
        sortAndRenderChatRooms();
      });
    })
    .catch(err => {
      console.error("채팅방 목록 로딩 실패:", err);
      renderEmptyMessage("채팅방 목록을 불러오지 못했습니다.");
    });
}

function loadBrokerChatRooms() {
  fetch("/broker/chat/list")
    .then(res => res.json())
    .then(chatRooms => {
      const results = chatRooms.map(room => ({
        type: "broker",
        room: {
          brokerChatRoomId: room.crId,
          brokerChatRoomTitle: room.crTitle
        },
        lastMessage: "",
        rcmTime: "",
        unitRoom: "",
        mbrNnm: ""
      }));

      chatRoomStates.length = 0;
      results.forEach(r => chatRoomStates.push(r));
      sortAndRenderChatRooms();
    })
    .catch(err => {
      console.error("❌ 중개 채팅방 로딩 실패:", err);
      renderEmptyMessage("중개 채팅방을 불러오지 못했습니다.");
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

  sorted.forEach(({ type, room, lastMessage, unitRoom, mbrNnm }) => {
    const previewText = lastMessage?.trim()
      ? `${unitRoom} ${mbrNnm} : ${lastMessage}`
      : "최근 메시지가 없습니다.";

    const item = document.createElement("div");
    item.className = "chat-room-item";
    item.dataset.roomId = type === "resident"
      ? room.residentChatRoomId
      : room.brokerChatRoomId;

    item.innerHTML = `
      <div class="chat-room-name">${type === "resident" ? room.residentChatRoomTitle : room.brokerChatRoomTitle}</div>
      <div class="chat-room-last-message">${type === "resident" ? previewText : "중개 채팅방"}</div>
    `;

    item.addEventListener("dblclick", () => {
      const popupUrl = type === "resident"
        ? `/resident/chat/room?residentChatRoomId=${room.residentChatRoomId}&popup=true`
        : `/broker/chat/room?crId=${room.brokerChatRoomId}&popup=true`;

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