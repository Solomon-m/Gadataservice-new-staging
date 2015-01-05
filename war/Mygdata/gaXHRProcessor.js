function processGARequest(parms,url) {
	console.log("Inside processGARequest in Mygdata gaXHRProcessor::: calling getDSData.do");
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
			console.log("Successful Request from Mygdata gaXHRProcessor");
			
						$j('#loadingmessage').hide(); 
			
			var responseText = request.responseText;

			rowData=responseText;
			// console.debug("Checking rowData in gaXHRProcessor:::"+JSON.parse(rowData));
			jsonData=JSON.parse(rowData);
			filteredData=jsonData;
						document.getElementById("msg").innerHTML="######Total Records "+jsonData.length;
//			worker.postMessage(new createCustomObject('sendDetails',JSON.parse(localStorage.getItem("rowdata")),"Account Load",1,'false','sendDetails'));
			//test work heree
			}
		}
			request.open('POST', url, true);
			request.setRequestHeader("Content-type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			
			$j('#loadingmessage').show(); 
		document.getElementById('tablechart').innerHTML="";
			request.send(parms);
		}
		
		function fetchXHRData()
		{
		var date=document.getElementById('dateFrom').value;
        var	key="GaDataObject_SBLIVE_"+date.replace(/-/g,'')+"_Cat|Act|Lab|cVal1|cVal2|cVal3|cVal4_json";
		processGARequest('',"getDSData.do?key="+key);
		
		}