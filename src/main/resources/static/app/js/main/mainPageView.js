// /BeaverTipi/src/main/resources/static/app/js/main/mainPageView.js

// jQuery가 로드되었는지 확인하고, 로드될 때까지 기다리는 함수
function runWhenJQueryLoaded(callback) {
    if (window.jQuery) { // jQuery ($) 객체가 존재하는지 확인
        callback();
    } else {
        // jQuery가 아직 로드되지 않았다면, 짧은 간격으로 재확인
        setTimeout(function() {
            runWhenJQueryLoaded(callback);
        }, 50); // 50ms마다 재확인
    }
}

// jQuery가 로드되면 이 함수를 실행합니다.
runWhenJQueryLoaded(function() {
    $(document).ready(function() {
		console.log("jQuery ready 이벤트 발생!"); // <--- 이 메시지가 뜨는지 확인!
        // 광고 데이터를 가져와 캐러셀에 동적으로 로드하는 함수
        function loadApprovedAds() {
			console.log("loadApprovedAds 함수 호출 시작!");
            $.ajax({
                url: contextPath + '/admin/businessAds/approvedAds', // 이전에 추가한 API 엔드포인트
                type: 'GET',
                dataType: 'json',
                success: function(adsList) {
                    console.log("메인 페이지용 승인된 광고 데이터:", adsList); //
                    const $adCarouselInner = $('#adSlider .carousel-inner'); //
                    $adCarouselInner.empty(); // 기존 정적 광고 내용 비우기

                    if (adsList && adsList.length > 0) { //
                        adsList.forEach((ad, index) => { //
                            let adTitle = ad.brdTitlNm || ''; //
                            let adContent = ad.brdCont || ''; //

                            // 광고 내용을 HTML로 파싱하여 <br> 태그 처리
                            adContent = adContent.replace(/\n/g, '<br>'); //

                            let carouselItemHtml = `<div class="carousel-item${index === 0 ? ' active' : ''}">`; //
                            carouselItemHtml += `  <div class="d-flex justify-content-center align-items-center ad-slide-box">`; //
                            
                            // 파일 목록에서 이미지 찾기 (image/로 시작하는 mime type만 필터링)
                            const imageFiles = ad.attachFiles ? ad.attachFiles.filter(file => file.fileMime.startsWith('image/')) : []; //

                            let logoImgSrc = ''; //
                            let backgroundImgSrcs = []; //

                            if (imageFiles.length > 0) { //
                                logoImgSrc = contextPath + `/admin/business/file/preview/${imageFiles[0].fileId}`; //
                                // 첫 번째 이미지 (로고)를 제외한 나머지 이미지를 배경 이미지 후보로 사용
                                for (let i = 1; i < imageFiles.length; i++) { //
                                    backgroundImgSrcs.push(contextPath + `/admin/business/file/preview/${imageFiles[i].fileId}`); //
                                }
                            }

                            // mainPage.jsp의 기존 구조를 최대한 따름
                            // 하나의 광고 게시글(BoardVO)당 하나의 캐러셀 아이템을 생성하는 정책
                            carouselItemHtml += `    <div class="ad-left-box">`; //
                            if (logoImgSrc) { //
                                carouselItemHtml += `      <img src="${logoImgSrc}" alt="${adTitle} 로고" class="ad-logo">`; //
                            } else { //
                                carouselItemHtml += `      <img src="${contextPath}/volt/assets/img/images/default-logo.png" alt="기본 로고" class="ad-logo">`; // 기본 로고 이미지 경로
                            }
                            carouselItemHtml += `      <div class="ad-title">${adTitle}</div>`; //
                            carouselItemHtml += `      <div class="ad-desc">${adContent}</div>`; //
                            carouselItemHtml += `    </div>`; //
                            carouselItemHtml += `    <div class="ad-right-box">`; //
                            if (backgroundImgSrcs.length > 0) { //
                                carouselItemHtml += `      <img src="${backgroundImgSrcs[0]}" class="ad-image" alt="${adTitle} 배경">`; //
                            } else if (logoImgSrc) { // 배경 이미지가 없으면 로고를 배경으로 사용 (선택 사항)
                                carouselItemHtml += `      <img src="${logoImgSrc}" class="ad-image" alt="${adTitle} 배경">`; //
                            } else { //
                                carouselItemHtml += `      <img src="${contextPath}/volt/assets/img/images/default-background.png" class="ad-image" alt="기본 배경">`; // 기본 배경 이미지 경로
                            }
                            carouselItemHtml += `    </div>`; //
                            
                            carouselItemHtml += `  </div>`; //
                            carouselItemHtml += `</div>`; //
                            $adCarouselInner.append(carouselItemHtml); //
                        });

                        // Bootstrap 5 캐러셀은 data-bs-ride="carousel" 속성으로 자동 초기화됩니다.
                        // 동적으로 내용을 추가한 후에는 carousel 인스턴스를 새로 생성하거나 업데이트할 필요가 있습니다.
                        // jQuery를 사용하고 있다면, 직접 인스턴스 생성
                        const adCarouselElement = document.getElementById('adSlider'); //
                        if (adCarouselElement) { //
                            // 기존에 인스턴스가 있었다면 제거 후 새로 생성 (안전한 방법)
                            const existingCarousel = bootstrap.Carousel.getInstance(adCarouselElement); //
                            if (existingCarousel) { //
                                existingCarousel.dispose(); // 기존 인스턴스 제거
                            }
                            new bootstrap.Carousel(adCarouselElement, { //
                                interval: 3000, // 3초 간격으로 자동 슬라이드
                                ride: 'carousel' //
                            });
                        }

                    } else { //
                        // 승인된 광고가 없는 경우, 기본 광고를 표시
                        $adCarouselInner.html(`
                            <div class="carousel-item active">
                                <div class="d-flex justify-content-center align-items-center ad-slide-box">
                                    <div class="ad-left-box">
                                        <img src="${contextPath}/volt/assets/img/images/ourads.png" alt="기본 광고 로고" class="ad-logo">
                                        <div class="ad-title">현재 광고 없음</div>
                                        <div class="ad-desc">새로운 광고가 준비 중입니다.</div>
                                    </div>
                                    <div class="ad-right-box">
                                        <img src="${contextPath}/volt/assets/img/images/ourads.png" class="ad-image" alt="기본 광고 배경">
                                    </div>
                                </div>
                            </div>
                        `); //
                        // 기본 광고로 캐러셀 초기화
                        const adCarouselElement = document.getElementById('adSlider'); //
                        if (adCarouselElement) { //
                            const existingCarousel = bootstrap.Carousel.getInstance(adCarouselElement); //
                            if (existingCarousel) { //
                                existingCarousel.dispose(); //
                            }
                            new bootstrap.Carousel(adCarouselElement, { //
                                interval: 3000, //
                                ride: 'carousel' //
                            });
                        }
                    }
                },
                error: function(xhr, status, error) { //
                    console.error("광고 데이터를 불러오는 데 실패했습니다:", status, error, xhr.responseText); //
                    // 오류 발생 시에도 기본 광고 유지
                    const $adCarouselInner = $('#adSlider .carousel-inner'); //
                    $adCarouselInner.empty(); //
                    $adCarouselInner.html(`
                        <div class="carousel-item active">
                            <div class="d-flex justify-content-center align-items-center ad-slide-box">
                                <div class="ad-left-box">
                                    <img src="${contextPath}/volt/assets/img/images/ourads.png" alt="기본 광고 로고" class="ad-logo">
                                    <div class="ad-title">광고 로드 오류</div>
                                    <div class="ad-desc">광고를 불러오는 중 오류가 발생했습니다.</div>
                                </div>
                                <div class="ad-right-box">
                                    <img src="${contextPath}/volt/assets/img/images/ourads.png" class="ad-image" alt="기본 광고 배경">
                                </div>
                            </div>
                        </div>
                    `); //
                    const adCarouselElement = document.getElementById('adSlider'); //
                    if (adCarouselElement) { //
                        const existingCarousel = bootstrap.Carousel.getInstance(adCarouselElement); //
                        if (existingCarousel) { //
                            existingCarousel.dispose(); //
                        }
                        new bootstrap.Carousel(adCarouselElement, { //
                            interval: 3000, //
                            ride: 'carousel' //
                        });
                    }
                }
            });
        }

        // 페이지 로드 시 광고 로드 함수 호출
        loadApprovedAds(); //

        // 매물 슬라이더 (roomCarousel) 초기화 (Bootstrap 5 활용)
        const roomCarouselElement = document.getElementById('roomCarousel'); //
        if (roomCarouselElement) { //
            const existingRoomCarousel = bootstrap.Carousel.getInstance(roomCarouselElement); //
            if (existingRoomCarousel) { //
                existingRoomCarousel.dispose(); //
            }
            new bootstrap.Carousel(roomCarouselElement, { //
                interval: 5000, // 5초 간격으로 자동 슬라이드 (원하는 시간으로 설정)
                ride: 'carousel' //
            });
        }
    });
});