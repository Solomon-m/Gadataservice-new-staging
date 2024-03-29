<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib  prefix="c"   uri="http://java.sun.com/jsp/jstl/core"  %> 
     <%@page import="java.util.* "%>
     <%@page import="com.account.AccountDetails"%>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" type="text/css" href="http://gadataservice-new-s.appspot.com/css/dataTable.css">
 <!--  <script src="http://images.sb.a-cti.com/testing/shashank/gData/reportProcessor.js"></script>-->
      <script src="/Mygdata/reportProcessor.js"></script>
 
<script src="/js/gaVisual.js"></script>
  <script src="/js/gaXHRProcessor.js"></script>
  <script src="/js/localData.js"></script>
  <!--  <script src="/js/localstoragehelper.js"></script>-->
  
<!-- <script src="http://s3-ap-southeast-1.amazonaws.com/shashanksworld/gaprocessor/reportProcessor.js"></script>
<script src="http://s3-ap-southeast-1.amazonaws.com/shashanksworld/gaprocessor/gaVisual.js"></script>
 -->
 
   	<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
 
  <script>
  var $j = jQuery.noConflict();
  
</script>
<script src="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/js/bootstrap.min.js"></script>
 <link href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet" media="screen">
<link type="text/css" href="/calendar/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="/calendar/ui/ui.core.js"></script>
	<script type="text/javascript" src="/calendar/ui/ui.datepicker.js"></script>
	<script type='text/javascript' src='https://www.google.com/jsapi'></script>
	


<%
    		String pageName= null;
    		if(request.getAttribute("page")!=null)
    		{
    			pageName=(String)request.getAttribute("page");    			
    		}
    %>
<head>
<%
	int startIndex=1;
	int totalRows=0;
	String rowData="";
	System.out.println("inside scriptlets..");
	String dateFrom="";
	if(request.getAttribute("rowData")!=null)
	{
		rowData=(String)request.getAttribute("rowData");
		
	}
	dateFrom=(String)request.getAttribute("date");
	String msg=(String)request.getAttribute("error");
	System.out.println("dateFrom::"+dateFrom);
	System.out.println("jsp page");
	
	int k=startIndex;
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Analytics Report</title>
<% String code= request.getParameter("code");
String accessToken=(String)request.getAttribute("accessToken");
String dimension=(String)request.getAttribute("dimension");
//String totalRec=(String)request.getAttribute("recordsFetched");
%>
<script>

var pageName='<%=pageName%>';
 var code='<%=code%>';
 var rowData='<%=rowData%>';
 var dimension='<%=dimension%>';
 var dateFrom='<%=dateFrom%>';
 var rawCSVURL=null;
 var activePage=null;
 var rawDataWidth=null;
 
  
 $j(document).ready(function(){
	
	 $j("#dateFrom").datepicker({ dateFormat: "yy-mm-dd" });
	 $j("#dateTo").datepicker({ dateFormat: "yy-mm-dd" });
	 
	 
 });
 if(rowData=="")
 {
	 		rowData=localStorage.getItem("rowdata");
 			console.log("row data copied");
 	 }
 rowData=decodeURI(rowData,"UTF-8");
 
 var jsonData=JSON.parse(rowData);
 if(jsonData)
	 {
	 rawDataWidth=jsonData[0].toString().split(",").length-1
	 }
 
 
 

</script>

<script type='text/javascript'>
      google.load('visualization', '1', {packages:['table']});
      google.setOnLoadCallback(sbEventDetail);
      var dataset="";
      var table='';
      var data='';
      var selectedRow='';
      try{
      function sbEventDetail() {
    	  data=getTableStructure("v2App");
    	  		console.log("add rows");
          data.addRows(jsonData);
          table = new google.visualization.Table(document.getElementById('table_div'));
          console.log(createDataView("v2App",data));
          table.draw(createDataView("v2App",data), options);
       	  //google.visualization.events.addListener(table, 'select', selectHandler);
          activePage="SB Event Detail";
      }
      }
      catch(e)
      {
    	  console.log(e);
      }
      function setKeyword(keyword)
      {
    	  document.getElementById("filterData").value=keyword;    	  
      }
   
      if(pageName!=null)
  	{
    	  console.info("setting page tab",pageName);
  		  document.getElementById(pageName).class="active";	
  	}
      </script>
      <body>
      <div class="page-header" style="margin:20px 0 0px;padding-bottom:0">
  		<h1><small>SwitchBoard</small></h1></div>
  		<div style="text-align: right;padding-right:10px; margin-right:0;position: relative;">
		  <div ><a href="#" id="csvAction" onclick="loadCSVPage()"><img style="width: 30px;height: 30px" src="/images/csv_text.png"/></a>
			</div></div>
  </div>
   <ul class="nav nav-tabs">
		
			  
		  <li id="chr" >
		    <a href="#" id="loadTime" onclick="navigate(this.innerHTML,id)">Load Time</a>
		  </li>
		  <li id="car" >
		    <a href="#" id="callAction" onclick="navigate(this.innerHTML,this.id)">Call Action</a>
		  </li>
		  <li id="cdr">
		    <a href="#" id="callDetails" onclick="navigate(this.innerHTML,id)">Call Details</a>
		  </li>
		  	  <li id="obr" >
		    <a href="#" id="eventDetails" onclick="navigate(this.innerHTML,id)">Outbound</a>
		  </li>
		  
		 <li id="cxr">
		    <a href="#" id="feedbackData" onclick="navigate(this.innerHTML,id)">CX Feedback Data</a>
		    
		  </li>
		  <li  class="active">
		    <a href="#" id="v2Report" onclick="navigate(this.innerHTML,id)">V2 Outbound Report</a>
		  </li>
		  <li id="msgNotes">
		    <a href="#" id="sbNotes" onclick="navigate(this.innerHTML,id)">SB Notes</a>
		  </li>
  
</ul>
  
<div  id="msg"class="alert" >
			  <button type="button" class="close" data-dismiss="alert">&times;</button>
  				<strong>Info::!</strong> <%=msg!=null?msg:"Select a Date"%>
		</div>
  	
  		<div class="btn dropdown-toggle"  data-toggle="dropdown" style="width:100%">
	<div >
    <table>
    <tr><h3>
    		<td>
    				<form id="dataFilter" action="/getCallReport.do">
    				<input type="hidden" name="page" value="obr"/>
				<h2><small>Date: </small><input type="text" name="dateFrom"  id="dateFrom" onchange="document.getElementById('dataFilter').submit();"  value="${dateFrom}"/>
			<button class="btn" onclick="document.getElementById('dataFilter').submit();">Fetch Data</button></h3>
			</form>
    		</td>
    <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
    <td><h2><small>Dimension  :</small></h2></td>
	    		<td>
	    			<select  onchange="getDataSet(this.value);" id="dimension">
	    				<option value="0">AccountNo</option>
	    				<option  value="1">Action</option>
	    				<option selected="selected" value="2">Agent Email</option>
	    			</select>
	    		</td>
	    <td><h2><small>Value:</small></h2></td>
	    <td>
	    <div class="btn-group" >
                <input type='text' class="span4 dropdown-toggle" style="width:200px;"  onkeypress="filterData('lTable');" data-toggle="" id="filterData" value="">
                <ul class="dropdown-menu" id="actionlist">
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">Send</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">CallConclusion</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">CallerHistory Annotation</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">Account Load</a></li>
                  
                </ul>
                
              </div>
	    </td>
	    <td>&nbsp;</td>
	    <td>
	    <button   class="btn" onclick="filterEventData()">Filter</button>
	    <button class="btn" onclick="clearFilters()">Clear</button>
	    </td>
	     <td><h2><small>Page Size :</small></h2></td>
	    		<td>
	    			<select id="pageOption"  onchange="setPageSize(this.value,'Event Details')">
	    				<option value="20" selected="selected">20</option>
	    				<option value="50">50</option>
	    				<option value="100">100</option>
	    				<option  value="" >No Paging</option>
	    			</select>
	    		</td>
    		<td>
	    </h2>
	    </tr>
    		</table>
    </div> 
    </div>	
    <div class="table table-hover" id='table_div'></div>
    
    <div id="table_call"  style="width: 100%;text-align: justify;"></div>
    
  </body>
<script>
document.getElementById("dateFrom").value=dateFrom!="null"?dateFrom:"";
document.getElementById("csvAction").setAttribute("onclick","DownloadJSON2CSV(jsonData)");
function loadCSVPage()
{console.log("loading;;");
if(dateFrom=="null")
	{
	if(window.location.search.split("&")[1].split("=")[0]=="dateFrom")
	{
		dateFrom=window.location.search.split("&")[1].split("=")[1];
	}
}
var csvURl="http://gadataservice-new-s.appspot.com/getCsvData.do?dateFrom="+dateFrom+"&page=csv";
	window.open(csvURl,"AnalyticsData","");
	console.log("loading;;"+csvURl);
	}




</script>
</html>