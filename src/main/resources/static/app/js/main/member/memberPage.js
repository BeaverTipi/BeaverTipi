/**
 * 
 */
/**
 * 회원정보 탭 전환 및 초기 탭 활성화 스크립트
 */

function openTab(tabId) {
  const buttons = document.querySelectorAll('.tab-button');
  const contents = document.querySelectorAll('.tab-content');

  // 모든 버튼과 콘텐츠에서 active 제거
  buttons.forEach(btn => btn.classList.remove('active'));
  contents.forEach(cont => cont.classList.remove('active'));

  // 선택된 버튼과 콘텐츠에 active 추가
  const button = document.querySelector(`[onclick="openTab('${tabId}')"]`);
  const content = document.getElementById(tabId);

  if (button) button.classList.add('active');
  if (content) content.classList.add('active');
}

// 페이지 로딩 시 defaultTab 자동 열기
document.addEventListener("DOMContentLoaded", function () {
  const wrapper = document.querySelector(".register-wrapper");
  const defaultTabId = wrapper?.dataset?.defaultTab;

  if (defaultTabId) {
    openTab(defaultTabId);
  }
});
