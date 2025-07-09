/**
 * 
 */
const customAxios = axios.create({
	 withCredentials : true
	, headers :{
		'Content-Type' : 'application/json'
	}
	})