/**
 * 
 */
const customAxios = axios.create({
	baseURL : 'http://localhost/'
	, withCredentials : true
	, headers :{
		'Content-Type' : 'application/json'
	}
	})