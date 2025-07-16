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
			case '1': params.minArea = 0; params.maxArea = 33; break;
			case '2': params.minArea = 33; params.maxArea = 66; break;
			case '3': params.minArea = 66; params.maxArea = 99; break;
			case '4': params.minArea = 99; break;
		}
	}

	if (document.querySelector('#parkingYn:checked')) {
		params.parkingYn = 'Y';
	}

	const minFloor = document.querySelector('#minFloor')?.value;
	if (minFloor && minFloor.trim() !== "") params.minFloor = minFloor;

	const maxFloor = document.querySelector('#maxFloor')?.value;
	if (maxFloor && maxFloor.trim() !== "") params.maxFloor = maxFloor;

	const minArea = document.querySelector('#minArea')?.value;
	if (minArea && minArea.trim() !== "") params.minGrArea = minArea;

	const maxArea = document.querySelector('#maxArea')?.value;
	if (maxArea && maxArea.trim() !== "") params.maxGrArea = maxArea;

	const facilityOpts = Array.from(document.querySelectorAll('.facilityOpt:checked')).map(el => el.value);
	if (facilityOpts.length > 0) {
		params.facilityOptionList = facilityOpts;
	}

	const mbrCd = document.querySelector('#currentUserCode')?.value;
	if (mbrCd && mbrCd.trim() !== "") params.mbrCd = mbrCd;

	return params;
};
