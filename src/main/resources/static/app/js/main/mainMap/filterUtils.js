window.getFilterParams = function() {
	const params = {};

	const saleType = document.querySelector('#saleTypeFilter')?.value;
	if (saleType) params.saleTypeList = [saleType];

	const listingType = document.querySelector('#listingTypeFilter')?.value;
	if (listingType) params.listingTypeCodeList = [listingType];

	const area = document.querySelector('#areaFilter')?.value;
	if (area) params.areaCode = area;

	const keyword = document.querySelector('#keywordFilter')?.value;
	if (keyword) params.keyword = keyword.trim();

	if (document.querySelector('#parkingYn:checked')) {
		params.parkingYn = 'Y';
	}

	const minFloor = document.querySelector('#minFloor')?.value;
	const maxFloor = document.querySelector('#maxFloor')?.value;
	if (minFloor) params.minFloor = minFloor;
	if (maxFloor) params.maxFloor = maxFloor;

	const facilityOpts = Array.from(document.querySelectorAll('.facilityOpt:checked')).map(el => el.value);
	if (facilityOpts.length > 0) {
		params.facilityOptionList = facilityOpts;
	}

	return params;
};
