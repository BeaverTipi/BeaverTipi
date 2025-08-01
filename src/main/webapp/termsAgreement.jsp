<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<head>
<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일               수정자           수정내용
 *  ============      ============== =======================
 *  2025. 8. 1.           김아린           최초 생성
 *
-->
  <title>이용약관 동의</title>
  <link rel="stylesheet" href="/app/css/main/member/terms.css">
</head>
<body>
  <div class="terms-container">
    <h2>이용약관 동의</h2>

    <form action="/member/register/form" method="get" onsubmit="return validateAgreement();">
      <!-- 전체 동의 -->
      <div class="checkbox-all">
        <label><input type="checkbox" id="checkAll"> <strong>전체 동의</strong></label>
      </div>
      <hr/>

      <!-- 약관 목록 -->
      <div class="checkbox-item">
        <label><input type="checkbox" name="termsRequired" class="term required"> [필수] 이용약관 동의</label>
        <div class="terms-box">
          <pre>여기에 이용약관 내용이 들어갑니다...</pre>
        </div>
      </div>

      <div class="checkbox-item">
        <label><input type="checkbox" name="privacyRequired" class="term required"> [필수] 개인정보 처리방침 동의</label>
        <div class="terms-box">
          <pre>여기에 개인정보 처리방침 내용이 들어갑니다...</pre>
        </div>
      </div>

      <div class="checkbox-item">
        <label><input type="checkbox" name="marketingOptional" class="term"> [선택] 마케팅 정보 수신 동의</label>
        <div class="terms-box">
          <pre>여기에 마케팅 수신 관련 안내문이 들어갑니다...</pre>
        </div>
      </div>

      <div class="button-area">
        <button type="submit" class="btn btn-primary">동의하고 다음 단계로</button>
      </div>
    </form>
  </div>

  <script>
    // 전체 동의 동작
    document.getElementById("checkAll").addEventListener("change", function () {
      const checked = this.checked;
      document.querySelectorAll(".term").forEach(cb => cb.checked = checked);
    });

    // 유효성 검사: 필수 항목 체크 여부 확인
    function validateAgreement() {
      const required = document.querySelectorAll(".required");
      for (let cb of required) {
        if (!cb.checked) {
          alert("필수 약관에 모두 동의해주세요.");
          return false;
        }
      }
      return true;
    }
  </script>
</body>
</html>