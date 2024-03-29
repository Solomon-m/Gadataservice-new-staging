<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib  prefix="c"   uri="http://java.sun.com/jsp/jstl/core"  %> 
     <%@page import="java.util.* "%>
     <%@page import="com.account.AccountDetails"%>
     
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app>
<link rel="stylesheet" type="text/css" href="http://gadataservice-new-s.appspot.com/css/dataTable.css">
 <script src="/reportProcessor.js"></script>
<script src="/js/gaVisual.js"></script>
  <script src="/js/gaXHRProcessor.js"></script>
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

   	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.0.7/angular-resource.min.js"></script>
	<script type="text/javascript" src="/calendar/ui/ui.core.js"></script>

	<script type="text/javascript" src="/calendar/ui/ui.datepicker.js"></script>
	<script type='text/javascript' src='https://www.google.com/jsapi'></script>
	
	

<!--  <script src="/js/XmlTextParser.js"></script>
<script src="/js/XmlGenerator.js"></script>-->
	

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


 var code='<%=code%>';
 var rowData='<%=rowData%>';
 var dimension='<%=dimension%>';
 var dateFrom='<%=dateFrom%>';
 if(rowData=="")
 {rowData=localStorage.getItem("rowdata");
 console.log("row data copied")
 	 }
 rowData=decodeURI(rowData,"UTF-8");
 var jsonData=JSON.parse(rowData);
 
 if(jsonData)
	 {
	 formatTimeStamp();	 
	 }
 
 $j(document).ready(function(){
	
	 $j("#dateFrom").datepicker({ dateFormat: "yy-mm-dd" });
	 $j("#dateTo").datepicker({ dateFormat: "yy-mm-dd" });
	
 });
 
</script>

<script type='text/javascript'>
      google.load('visualization', '1', {packages:['table']});
      google.setOnLoadCallback(drawTable);
      var dataset="";
      var table='';
      var data='';
      var selectedRow='';
      function drawTable() {
    	  var options = {'showRowNumber': true};
    	  		data=getTableStructure("callList");
    	  		console.log("add rows");
          data.addRows(getCallerAccountDetails());
        	  options['page'] = 'enable';
          options['pageSize'] = 50;
          options['pagingSymbols'] = {prev: 'prev', next: 'next'};
          options['pagingButtonsConfiguration'] = 'auto';

          table = new google.visualization.Table(document.getElementById('table_div'));
          table.draw(createDataView("callDataReport",data), options);
       	  google.visualization.events.addListener(table, 'select', selectHandler);
       
      }
      function setKeyword(keyword)
      {
    	  document.getElementById("filterData").value=keyword;    	  
      }
     
      </script>
      <body>
      <div class="page-header">
  		<h1><small>Call Data Report</small></h1>
  </div>
  
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
				<h2><small>Date: </small><input type="text" name="dateFrom"  id="dateFrom" onchange="document.getElementById('dataFilter').submit();"  value="${dateFrom}"/>
			<button class="btn" onclick="document.getElementById('dataFilter').submit();">Fetch Data</button></h3>
			</form>
    		</td>
    <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
    <td><h2><small>Dimension  :</small></h2></td>
	    		<td>
	    			<select onchange="getDataSet(this.value);" id="dimension">
	    				<option value="0">AccountNo</option>
	    				<option selected="selected" value="1">Action</option>
	    				<option value="2">AgentInitial</option>
	    				<option  value="3" >ConnId</option>
	    				<option value="4">IncomingANI</option>
	    			</select>
	    		</td>
	    <td><h2><small>Value:</small></h2></td>
	    <td>
	    <div class="btn-group" >
                <input type='text' class="span4 dropdown-toggle" onkeypress="filterData('lTable');" data-toggle="dropdown" id="filterData" value="">
                <ul class="dropdown-menu" id="actionlist">
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">Send</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">CallConclusion</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">CallerHistory Annotation</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">Account Load</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">InBoundCall</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">AP Refetch</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">Outbound Call</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">DIR</a></li>
                  <li><a href="#" onclick="$j('#filterData').attr('value',this.innerHTML);">Scenario</a></li>
                  
                </ul>
              </div>
	    </td>
	    <td>&nbsp;</td>
	    <td>
	    <button class="btn" onclick="filterData('lTable')">Filter</button>
	    <button class="btn" onclick="document.getElementById('filterData').value=''">Clear</button>
	    </td>
	    
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

</script>
</html>