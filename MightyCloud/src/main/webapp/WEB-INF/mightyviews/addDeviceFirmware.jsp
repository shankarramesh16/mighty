<%--
    Document   : device_firmware
    Created on : OCT 09, 2016, 03:51:01 PM
    Author     : Vikky
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*"%>
<%@page import="com.team.mighty.domain.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"
	errorPage="error.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Device Firmware</title>

<link href="css/style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/coin-slider.css" />
<link href="css/validate.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery-latest.js"></script>

<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script src="js/dateValidation.js"></script>

<!-- Beginning of compulsory code below -->
<link href="css/dropdown/dropdown.css" media="screen" rel="stylesheet"
	type="text/css" />

<link href="css/dropdown/dropdown.vertical.css" media="screen"
	rel="stylesheet" type="text/css" />
<link href="css/dropdown/themes/default/default.advanced.css"
	media="screen" rel="stylesheet" type="text/css" />

<script type="text/javascript">
	$(function() {
		$('#fromDate').datepick();
	});
</script>
<script type="text/javascript">
	 function confirmValidate() {
	    var version = $("input[name=version]").val();
		var flag = true;
		var currentDate = new Date();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		var fromDate = $("input[name=fromDate]").val();
		
		if(fromDate=="")
			{
			flag=false;
			alert("Please Specify Effective Date!");
			}
		else if(version==""){
			flag=false;
			alert("Please Specify Version!");
		}
		/* else if (CompareTwoDatesddmmyyyy(fromDate, currentDatevar)) {
			alert("Effective date must be after current date !");
			flag = false;
		} */

		
		return flag;

	} 
</script>

</head>
<body>
<%
String message = (String)request.getAttribute("status");
System.out.println("Status Message"+message);
if (message != null && !message.equals("")) {
%>
<div id="statusMessage">
<%=message%>
<%} %>
</div>
</div>
	<div id="body">
		<div class="content">
			<form action="deviceFirmwareSubmit" name="deviceFirmwareForm"
				onsubmit="return confirmValidate();" method="post">
				<h3>Add Device Firmware</h3>
				<table align="center">
					
					<tr>
						<td align="right">Effective Date</td>
						<td><input type="text" name="fromDate" id="fromDate"
							class="required" readOnly
							class="{showOtherMonths: true, firstDay: 1, dateFormat: 'yyyy-mm-dd', minDate: new Date(2012,  1, 25)}" />
							<label for="fromDate" class="requiredLabel">*</label></td>
					</tr>
					
					<tr>
						<td align="right">Version</td>
						<td><input type="text" value=""	 name="version" id="vId" /> 
						</td>
					</tr>
					
					<tr>
						<td></td>
						<td><input class="formbutton" type="submit" value="Add" /> </td>
							<!-- <input type="button" class="formbutton" onclick="javascript:history.go(-1);" value="Back" /></td> -->
					</tr>
					
				</table>
			</form>

		</div>
	</div>

</body>
</html>
