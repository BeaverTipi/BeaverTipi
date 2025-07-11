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
    window.addEventListener("load", () => {
    	const urlParams = new URLSearchParams(window.location.search);
    	const mode = urlParams.get("mode") ?? "create";

    	console.log("🧭 mode:", mode);

    	document.getElementById("pageTitle").textContent =
    	  mode === "invite" ? "🏢 입주민 초대" : "🏢 입주민 선택";

    	if (mode === "create") {
    	  const bldgId = urlParams.get("bldgId");
    	  console.log("📦 bldgId:", bldgId);

    	  if (!bldgId) {
    	    alert("건물 ID가 전달되지 않았습니다.");
    	    return;
    	  }

    	  fetchResidents(bldgId);
    	} else if (mode === "invite") {
    	  const residentChatRoomId = urlParams.get("residentChatRoomId");
    	  console.log("💬 residentChatRoomId:", residentChatRoomId);

    	  if (!residentChatRoomId) {
    	    alert("채팅방 ID가 전달되지 않았습니다.");
    	    return;
    	  }

    	  fetchInviteResidents(residentChatRoomId);
    	}
	
      function fetchResidents(urlOrBldgId) {
        const url = urlOrBldgId.startsWith("/resident/")
          ? urlOrBldgId
          : "/resident/chat/getResidentList?bldgId=" + urlOrBldgId;

        console.log("📡 요청 URL:", url);

        fetch(url)
          .then(res => {
            if (!res.ok) throw new Error("입주민 목록 요청 실패");
            return res.json();
          })
          .then(data => {
            console.log("📥 받은 데이터:", data);
            renderResidents(data);
          })
          .catch(err => {
            console.error("❌ 입주민 목록 로딩 실패:", err);
            alert("입주민 목록을 불러오는 중 오류가 발생했습니다.");
          });
      }
      function fetchInviteResidents(residentChatRoomId) {
    	  const url = "/resident/chat/room/invite/residentList?residentChatRoomId=" + residentChatRoomId;
    	  console.log("📡 초대 모드 요청 URL:", url);

    	  fetch(url)
    	    .then(res => res.json())
    	    .then(data => {
    	      console.log("📥 초대 모드 받은 데이터:", data);
    	      renderResidents(data);
    	    })
    	    .catch(err => {
    	      console.error("❌ 초대 모드 입주민 목록 실패:", err);
    	      alert("입주민 목록을 불러오는 중 오류가 발생했습니다.");
    	    });
    	}
      
      
      
      function searchResidents() {
        const searchType = document.querySelector("#searchType").value;
        const keyword = document.querySelector("#searchKeyword").value.trim();

        if (!keyword) {
          fetchResidents(bldgId);
          return;
        }

        fetch("/resident/chat/getResidentList?bldgId=" + bldgId)
          .then(res => res.json())
          .then(data => {
            const filtered = data.filter(r => {
              const unitRoom = r.unit?.unitRoom ?? "";
              const mbrNnm = r.member?.mbrNnm ?? "";

              if (searchType === "unitRoom") {
                return unitRoom === keyword;
              } else if (searchType === "mbrNnm") {
                return mbrNnm.includes(keyword);
              }
              return false;
            });

            renderResidents(filtered);
          })
          .catch(err => {
            console.error("❌ 검색 실패:", err);
            alert("검색 중 오류가 발생했습니다.");
          });
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
          .forEach((r, i) => {
            const mbrCd = r.mbrCd ?? "";
            const mbrNnm = r.member?.mbrNnm != null ? String(r.member.mbrNnm) : "(닉네임 없음)";
            const unitRoom = r.unit?.unitRoom != null ? String(r.unit.unitRoom) : "(호실 없음)";

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
            checkbox.dataset.unitRoom = unitRoom; // ✅ 추가됨
            tdCheck.appendChild(checkbox);

            row.appendChild(tdRoom);
            row.appendChild(tdName);
            row.appendChild(tdCheck);

            tbody.appendChild(row);
          });
      }

      function confirmSelection() {
        const checked = Array.from(document.querySelectorAll("input[type='checkbox']:checked"));
        const members = checked.map(chk => ({
          mbrCd: chk.value,
          name: chk.dataset.name,
          unitRoom: chk.dataset.unitRoom // ✅ 추가됨
        }));

        handleSelectionByMode(members); // ✅ mode에 따라 함수 분기 호출
        window.close();
      }

      // ✅ mode 분기 처리용 함수 추가
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

      // 전역 함수로 노출
      window.searchResidents = searchResidents;
      window.confirmSelection = confirmSelection;
    });
  </script>
</body>
</html>