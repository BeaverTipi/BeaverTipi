function pyToM2(py) {
	return Math.round(py * 3.3);
}
window.getFilterParams = function() {
	const params = {};

	const saleType = document.querySelector('#saleTypeFilter')?.value;
	if (saleType && saleType.trim() !== "") params.saleTypeList = [saleType];

	const listingType = document.querySelector('#listingTypeFilter')?.value;
	if (listingType && listingType.trim() !== "") params.typeCode1List = [listingType];

	const subType = document.querySelector('#saleDetailTypeFilter')?.value;
	if (subType && subType.trim() !== "") params.typeCode2List = [subType];

	const keyword = document.querySelector('#keywordFilter')?.value;
	if (keyword && keyword.trim() !== "") params.keyword = keyword.trim();

	const areaCode = document.querySelector('#areaFilter')?.value;
	if (areaCode && areaCode.trim() !== "") {
		switch (areaCode) {
			case '1':
				params.minArea = 0;
				params.maxArea = pyToM2(10); // 10평 이하
				break;
			case '2':
				params.minArea = pyToM2(10);
				params.maxArea = pyToM2(20); // 10~20평
				break;
			case '3':
				params.minArea = pyToM2(20);
				params.maxArea = pyToM2(30); // 20~30평
				break;
			case '4':
				params.minArea = pyToM2(30); // 30평 이상
				break;
		}
	}


	if (document.querySelector('#parkingYn:checked')) {
		params.parkingYn = 'Y';
	}

	const minFloor = document.querySelector('#minFloor')?.value;
	if (minFloor && minFloor.trim() !== "") params.minFloor = minFloor;

	const maxFloor = document.querySelector('#maxFloor')?.value;
	if (maxFloor && maxFloor.trim() !== "") params.maxFloor = maxFloor;

	const facilityOpts = Array.from(document.querySelectorAll('.facilityOpt:checked')).map(el => el.value);
	if (facilityOpts.length > 0) {
		params.facilityOptionList = facilityOpts;
	}

	const mbrCd = document.querySelector('#currentUserCode')?.value;
	if (mbrCd && mbrCd.trim() !== "") params.mbrCd = mbrCd;

	return params;
};
