window.getFilterParams = function() {
	const params = {};

	const saleType = document.querySelector('#saleTypeFilter')?.value;
	if (saleType) params.saleTypeList = [saleType];

	const listingType = document.querySelector('#listingTypeFilter')?.value;
	if (listingType) params.typeCode1List = [listingType];

	const subType = document.querySelector('#saleDetailTypeFilter')?.value;
	if (subType) params.typeCode2List = [subType];

	const keyword = document.querySelector('#keywordFilter')?.value;
	if (keyword) params.keyword = keyword.trim();

	const areaCode = document.querySelector('#areaFilter')?.value;
	if (areaCode) {
		switch (areaCode) {
			case '1': // 10평 이하
				params.minArea = 0;
				params.maxArea = 33;
				break;
			case '2': // 10~20평
				params.minArea = 33;
				params.maxArea = 66;
				break;
			case '3': // 20~30평
				params.minArea = 66;
				params.maxArea = 99;
				break;
			case '4': // 30평 이상
				params.minArea = 99;
				break;
		}
	}

	if (document.querySelector('#parkingYn:checked')) {
		params.parkingYn = 'Y';
	}

	const minFloor = document.querySelector('#minFloor')?.value;
	const maxFloor = document.querySelector('#maxFloor')?.value;
	if (minFloor) params.minFloor = minFloor;
	if (maxFloor) params.maxFloor = maxFloor;

	const minArea = document.querySelector('#minArea')?.value;
	const maxArea = document.querySelector('#maxArea')?.value;
	if (minArea) params.minArea = minArea;
	if (maxArea) params.maxArea = maxArea;

	const facilityOpts = Array.from(document.querySelectorAll('.facilityOpt:checked')).map(el => el.value);
	if (facilityOpts.length > 0) {
		params.facilityOptionList = facilityOpts;
	}

	const mbrCd = document.querySelector('#currentUserCode')?.value;
	if (mbrCd) params.mbrCd = mbrCd;

	return params;
};

window.toURLParams = function(params){
	const searchParams = new URLSearchParams();
	for (const key in params) {
		const value = params[key];
		if (Array.isArray(value)) {
			value.forEach(v => searchParams.append(key, v)); // ✅ 배열은 append로 각각 추가
		} else {
			searchParams.append(key, value);
		}
	}
	return searchParams.toString();
}
