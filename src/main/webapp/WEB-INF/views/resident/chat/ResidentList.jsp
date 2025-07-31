<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주민 검색</title>
  <style>
    body { font-family: sans-serif; padding: 20px; }
    input, select { margin-right: 10px; padding: 5px; }
    button { padding: 6px 12px; }
    table { border-collapse: collapse; width: 100%; margin-top: 20px; }
    th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
    th { background-color: #f9f9f9; }
  </style>
</head>
<body>
  <h2 id="pageTitle">🏢 입주민 검색</h2>

  <div>
    <select id="searchType">
      <option value="unitRoom">호실</option>
      <option value="mbrNnm">닉네임</option>
    </select>
    <input type="text" id="searchKeyword" placeholder="검색어 입력" />
    <button type="button" onclick="searchResidents()">검색</button>
  </div>

  <form id="residentForm">
    <table>
      <thead>
        <tr>
          <th>호실</th>
          <th>닉네임</th>
          <th>선택</th>
        </tr>
      </thead>
      <tbody id="residentTableBody">
        <tr><td colspan="3">검색 결과가 없습니다.</td></tr>
      </tbody>
    </table>

    <div style="margin-top: 20px;">
      <button type="button" onclick="confirmSelection()">선택 완료</button>
      <button type="button" onclick="window.close()">닫기</button>
    </div>
  </form>

  <script>
    let mode = "create";
    let bldgId = null;
    let residentChatRoomId = null;
    let residentData = [];

    const selectedMembers = new Map(); // ✅ 전역에서 체크 상태 기억

    window.addEventListener("load", () => {
      const urlParams = new URLSearchParams(window.location.search);
      mode = urlParams.get("mode") ?? "create";

      document.getElementById("pageTitle").textContent =
        mode === "invite" ? "🏢 입주민 초대" : "🏢 입주민 선택";

      if (mode === "create") {
        bldgId = urlParams.get("bldgId");
        if (!bldgId) {
          alert("건물 ID가 전달되지 않았습니다.");
          return;
        }
        fetchResidents("/resident/chat/getResidentList?bldgId=" + bldgId);
      } else if (mode === "invite") {
        residentChatRoomId = urlParams.get("residentChatRoomId");
        if (!residentChatRoomId) {
          alert("채팅방 ID가 전달되지 않았습니다.");
          return;
        }
        fetchResidents("/resident/chat/room/invite/residentList?residentChatRoomId=" + residentChatRoomId);
      }
    });

    function fetchResidents(url) {
      fetch(url)
        .then(res => {
          if (!res.ok) throw new Error("입주민 목록 요청 실패");
          return res.json();
        })
        .then(data => {
          residentData = data;
          renderResidents(data);
        })
        .catch(err => {
          console.error("❌ 입주민 목록 로딩 실패:", err);
          alert("입주민 목록을 불러오는 중 오류가 발생했습니다.");
        });
    }

    function searchResidents() {
      const searchType = document.querySelector("#searchType").value;
      const keyword = document.querySelector("#searchKeyword").value.trim().toLowerCase();

      if (!Array.isArray(residentData) || residentData.length === 0) {
        alert("입주민 데이터가 없습니다.");
        return;
      }

      const filtered = !keyword
        ? residentData
        : residentData.filter(r => {
            const unitRoom = String(r.unit?.unitRoom ?? "").toLowerCase();
            const mbrNnm = String(r.member?.mbrNnm ?? "").toLowerCase();

            if (searchType === "unitRoom") return unitRoom === keyword;
            if (searchType === "mbrNnm") return mbrNnm.includes(keyword);
            return false;
          });

      renderResidents(filtered); 
    }

    function renderResidents(data) {
      const tbody = document.querySelector("#residentTableBody");
      tbody.innerHTML = "";

      if (!Array.isArray(data) || data.length === 0) {
        tbody.innerHTML = "<tr><td colspan='3'>검색 결과가 없습니다.</td></tr>";
        return;
      }

      data
        .slice()
        .sort((a, b) => {
          const roomA = parseInt(a.unit?.unitRoom ?? "0", 10);
          const roomB = parseInt(b.unit?.unitRoom ?? "0", 10);
          return roomA - roomB;
        })
        .forEach(r => {
          const mbrCd = r.mbrCd ?? "";
          const mbrNnm = r.member?.mbrNnm ?? "(닉네임 없음)";
          const unitRoom = r.unit?.unitRoom ?? "(호실 없음)";

          const row = document.createElement("tr");

          const tdRoom = document.createElement("td");
          tdRoom.textContent = unitRoom;

          const tdName = document.createElement("td");
          tdName.textContent = mbrNnm;

          const tdCheck = document.createElement("td");
          const checkbox = document.createElement("input");
          checkbox.type = "checkbox";
          checkbox.value = mbrCd;
          checkbox.dataset.name = mbrNnm;
          checkbox.dataset.unitRoom = unitRoom;


          if (selectedMembers.has(mbrCd)) {
            checkbox.checked = true;
          }


          checkbox.addEventListener("change", () => {
            if (checkbox.checked) {
              selectedMembers.set(mbrCd, {
                mbrCd,
                name: mbrNnm,
                unitRoom
              });
            } else {
              selectedMembers.delete(mbrCd);
            }
          });

          tdCheck.appendChild(checkbox);
          row.appendChild(tdRoom);
          row.appendChild(tdName);
          row.appendChild(tdCheck);
          tbody.appendChild(row);
        });
    }

    function confirmSelection() {
      const members = Array.from(selectedMembers.values()); // ✅ 선택된 전체 목록 전달
      handleSelectionByMode(members);
      window.close();
    }

    function handleSelectionByMode(members) {
      if (mode === "invite") {
        if (window.opener && typeof window.opener.receiveInviteTargets === "function") {
          window.opener.receiveInviteTargets(members);
        }
      } else {
        if (window.opener && typeof window.opener.receiveSelectedMembers === "function") {
          window.opener.receiveSelectedMembers(members);
        }
      }
    }

    window.searchResidents = searchResidents;
    window.confirmSelection = confirmSelection;
  </script>
</body>
</html>