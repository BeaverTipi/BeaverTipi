function runWhenJQueryLoaded(callback) {
    if (window.jQuery) {
        callback();
    } else {
        setTimeout(function() {
            runWhenJQueryLoaded(callback);
        }, 50);
    }
}

runWhenJQueryLoaded(function() {
    $(document).ready(function() {

        // 광고가 없거나 로드 오류 시 기본 광고를 표시하고 캐러셀 초기화하는 함수
        function displayDefaultAd(isError = false) {
            console.log(isError ? "광고 로드 오류 발생. 기본 광고 표시." : "승인된 광고 데이터 없음. 기본 광고 표시.");
            const $adCarouselInner = $('#adSlider .carousel-inner');
            $adCarouselInner.empty(); // 기존 내용 비우기

            $adCarouselInner.html(`
                <div class="carousel-item active">
                    <div class="d-flex justify-content-center align-items-center ad-slide-box">
                        <div class="ad-left-box">
                            <img src="${contextPath}/volt/assets/img/images/ourads.png" alt="기본 광고 로고" class="ad-logo">
                            <div class="ad-title">${isError ? '광고 로드 오류' : '광고 제목'}</div>
                            <div class="ad-desc">${isError ? '광고를 불러오는 중 오류가 발생했습니다.' : '광고 신청으로 광고를 넣어보세요!'}</div>
                        </div>
                        <div class="ad-right-box">
                            <img src="${contextPath}/volt/assets/img/images/ourads.png" class="ad-image" alt="기본 광고 배경">
                        </div>
                    </div>
                </div>
            `);

            // 캐러셀 컨트롤 버튼 숨기기 (기본 광고는 슬라이드될 필요가 없으므로)
            $('#adSlider .carousel-control-prev').hide();
            $('#adSlider .carousel-control-next').hide();

            // 캐러셀 재 초기화 (필요한 경우)
            const adCarouselElement = document.getElementById('adSlider');
            if (adCarouselElement) {
                const existingCarousel = bootstrap.Carousel.getInstance(adCarouselElement);
                if (existingCarousel) {
                    existingCarousel.dispose();
                }
                // 기본 광고는 슬라이드할 필요가 없으므로 interval 없이 초기화하거나 초기화하지 않아도 됩니다.
                // 여기서는 슬라이드 기능이 없도록 설정
                new bootstrap.Carousel(adCarouselElement, {
                    interval: false // 슬라이드 기능 비활성화
                });
            }
        }

        // 광고 데이터를 가져와 캐러셀에 동적으로 로드하는 함수
        function loadApprovedAds() {
            
            const requestUrl = contextPath + '/ajax/admin/businessAds/approvedAds';
            console.log("AJAX 요청 URL:", requestUrl);

            $.ajax({
                url: requestUrl,
                type: 'GET',
                dataType: 'json',
                success: function(adsList) {
                    console.log("메인 페이지용 승인된 광고 데이터:", adsList);
                    const $adCarouselInner = $('#adSlider .carousel-inner');
                    $adCarouselInner.empty(); // 기존 정적 광고 내용 비우기
                    console.log("adCarouselInner 비우기 완료. 현재 HTML:", $adCarouselInner.html());

                    if (adsList && adsList.length > 0) {
                        console.log("광고 데이터 존재함. 광고 수:", adsList.length);
                        adsList.forEach((ad, index) => {
                            let adTitle = ad.brdTitlNm || '';
                            let adContent = ad.brdCont || '';

                            // 광고 내용을 HTML로 파싱하여 <br> 태그 처리
                            adContent = adContent.replace(/\n/g, '<br>');

                            // 파일 목록에서 이미지 찾기 (image/로 시작하는 mime type만 필터링)
                            const imageFiles = ad.attachFiles ? ad.attachFiles.filter(file => file.fileMime.startsWith('image/')) : [];

                            let logoImgSrc = '';
                            let backgroundImgSrcs = [];

                            if (imageFiles.length > 0) {
                                logoImgSrc = imageFiles[0].filePathUrl;
                                // 첫 번째 이미지 (로고)를 제외한 나머지 이미지를 배경 이미지 후보로 사용
                                for (let i = 1; i < imageFiles.length; i++) {
                                    backgroundImgSrcs.push(imageFiles[i].filePathUrl);
                                    console.log(imageFiles[i].filePathUrl);
                                }
                            }
                            
                            // HTML 문자열 생성
                            let carouselItemHtml = `<div class="carousel-item${index === 0 ? ' active' : ''}">`;
                            carouselItemHtml += `  <div class="d-flex justify-content-center align-items-center ad-slide-box">`;
                            
                            carouselItemHtml += `    <div class="ad-left-box">`;
                            if (logoImgSrc) {
                                carouselItemHtml += `      <img src="${logoImgSrc}" alt="${adTitle} 로고" class="ad-logo">`;
                            } else {
                                carouselItemHtml += `      <img src="${contextPath}/volt/assets/img/images/default-logo.png" alt="기본 로고" class="ad-logo">`;
                            }
                            carouselItemHtml += `      <div class="ad-title">${adTitle}</div>`;
                            carouselItemHtml += `      <div class="ad-desc">${adContent}</div>`;
                            carouselItemHtml += `    </div>`;
                            carouselItemHtml += `    <div class="ad-right-box">`;
                            if (backgroundImgSrcs.length > 0) {
                                carouselItemHtml += `      <img src="${backgroundImgSrcs[0]}" class="ad-image" alt="${adTitle} 배경">`;
                            } else if (logoImgSrc) { 
                                carouselItemHtml += `      <img src="${logoImgSrc}" class="ad-image" alt="${adTitle} 배경">`; 
                            } else {
                                carouselItemHtml += `      <img src="${contextPath}/volt/assets/img/images/default-background.png" class="ad-image" alt="기본 배경">`;
                            }
                            carouselItemHtml += `    </div>`;
                            
                            carouselItemHtml += `  </div>`;
                            carouselItemHtml += `</div>`;
                            
                            $adCarouselInner.append(carouselItemHtml);
                        });

                        // 캐러셀 컨트롤 버튼 다시 표시 (광고가 여러 개일 때만)
                        if (adsList.length > 1) {
                            $('#adSlider .carousel-control-prev').show();
                            $('#adSlider .carousel-control-next').show();
                        } else {
                            // 광고가 하나뿐이면 컨트롤 숨김
                            $('#adSlider .carousel-control-prev').hide();
                            $('#adSlider .carousel-control-next').hide();
                        }

                        // Bootstrap 5 캐러셀 수동 초기화/업데이트
                        const adCarouselElement = document.getElementById('adSlider');
                        if (adCarouselElement) {
                            const existingCarousel = bootstrap.Carousel.getInstance(adCarouselElement);
                            if (existingCarousel) {
                                existingCarousel.dispose(); // 기존 인스턴스 제거
                                console.log("기존 캐러셀 인스턴스 제거됨.");
                            }
                            new bootstrap.Carousel(adCarouselElement, {
                                interval: 5000, // 5초 간격으로 자동 슬라이드 (요청하신 5초로 변경)
                                ride: 'carousel'
                            });
                        } else {
                            console.log("adSlider 요소를 찾을 수 없습니다.");
                        }

                    } else {
                        // 승인된 광고 데이터가 없는 경우
                        displayDefaultAd(false);
                    }
                },
                error: function(xhr, status, error) {
                    console.error("광고 데이터를 불러오는 데 실패했습니다:", status, error, xhr.responseText);
                    // 오류 발생 시 기본 광고 표시
                    displayDefaultAd(true);
                }
            });
        }

        // 페이지 로드 시 광고 로드 함수 호출
        loadApprovedAds();

        // 매물 슬라이더 (roomCarousel) 초기화 (Bootstrap 5 활용)
        const roomCarouselElement = document.getElementById('roomCarousel');
        if (roomCarouselElement) {
            const existingRoomCarousel = bootstrap.Carousel.getInstance(roomCarouselElement);
            if (existingRoomCarousel) {
                existingRoomCarousel.dispose();
            }
            new bootstrap.Carousel(roomCarouselElement, {
                interval: 5000, // 5초 간격으로 자동 슬라이드 (원하는 시간으로 설정)
                ride: 'carousel'
            });
        } else {
            console.log("roomCarousel 요소를 찾을 수 없습니다.");
        }
    });
});