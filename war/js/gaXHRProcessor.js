function processGARequest(parms,url) {
	
	var isSynchronous = false;
	if (window.XMLHttpRequest) {
		request = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		request = new ActiveXObject("Msxml2.XMLHTTP");
	} else {
		request = new ActiveXObject("Microsoft.XMLHTTP");
	}
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			console.log("Successful Request from js gaXHRProcessor::");
			
			var responseText = request.responseText;
			console.log("response:"+responseText);
			console.debug("Checking responseText in js processGARequest::"+responseText);
			//console.log("responseData::"+responseText);
			//fetchV2Outbound.do?dateFrom=2014-05-01&dateTo=2014-05-29&range=monthly
				//	http://gabackend.gadataservice.appspot.com/
			//test work heree
			}
		}
			request.open('POST', url, true);
			request.setRequestHeader("Content-type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			request.setRequestHeader("Access-Control-Allow-Origin", "*");
			request.send(parms);
		}