<%--
    Document   : device_firmware
    Created on : OCT 09, 2016, 03:51:01 PM
    Author     : Vikky
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*"%>
<%@page import="com.team.mighty.domain.*"%>
<!DOCTYPE html >
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Device Firmware</title>

<script  src="https://code.jquery.com/jquery-2.2.0.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script type="text/javascript" src="js/jquery.datepick.js"></script>
<script src="js/dateValidation.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css">
  <script src="js/jquery-ui.js"></script>
<style type="text/css">
@import "css/jquery.datepick.css";
</style>

<script type="text/javascript">
	
	$(document).ready(function() {
		$("#fromDate").datepicker({ minDate: 0,dateFormat: 'dd/mm/yy' }); 
		
	});
	

	 function confirmValidate() {
	    var input1 = document.getElementById("file1");
	    var input2 = document.getElementById("file2");
	    var fromDate = $("input[name=fromDate]").val();
		var flag = true;
		var currentDate = new Date();
		var currentDatevar = currentDate.getDate() + "/"
				+ currentDate.getMonth() + "/" + currentDate.getFullYear();
		
		
		if(fromDate=="")
		{
		 document.getElementById("errortag").innerHTML = "Please Specify Effective Date!";
		 $('.validation-required').removeClass("validation-required").addClass("form-control");
		 $('.san').show();
		 document.getElementById("fromDate").focus();
		  flag=false;
		
		}/* else if (CompareTwoDatesddmmyyyy(fromDate, currentDatevar)) {
			alert("Effective date must be after current date !");
			flag = false;				
		}*/ 
		
		else  if (!input1.files[0]) {
		 document.getElementById("errortag").innerHTML = "Please select a file before clicking 'Upload'";
		 $('.validation-required').removeClass("validation-required").addClass("form-control");
		 $('.san').show();
		 document.getElementById("file1").focus();
		   flag=false;
		}else if (!input2.files[0]) {
			 document.getElementById("errortag").innerHTML = "Please select a Version File before clicking 'Upload'";
			 $('.validation-required').removeClass("validation-required").addClass("form-control");
			 $('.san').show();
			 document.getElementById("file2").focus();
			   flag=false;
		} 
		
			
		return flag;

	} 
</script>
</head>
<body>
	<% AdminUser adminUser=(AdminUser)request.getSession().getAttribute("adminUser");%>
							 <%@include file="Header.jsp"%> 
	<div class="wrapper">
		<div class="header-wrap">
		<div class="container">
		
			<div class="row">
				
				<div class="col-sm-12 text-right">
					<img src="images/user_iocn_header.png" />&nbsp;Welcome  <%=adminUser.getDisplayname()%>  &nbsp;&nbsp;&nbsp;<a href="logout"><img src="images/logout_icon_header.png" />&nbsp;Log Out</a>
				</div>
				
			</div>
										
		</div>
	</div>
		<div class="main-page-container">
			<div class="container">
				<div class="row">
					<div class="col-sm-12">
						<div class="breadcrumb-wrap">
							<a href="adminHome"><img src="images/home.png" /></a>
							<a href="adminHome">My Information </a>
							<a href="deviceUserInfo" >Mighty User </a>
							<a href="#" class="current" >Device Firmware Upload</a>
							<a href="deviceFirmwareReport" >Mighty Device Firmware Report</a>
							<a href="mightyDeviceInfo" >Mighty Device Report</a>
							<a href="addDevicePlaylist" >Mighty Featured Playlist</a>
							<a href="devicePlaylist" >Mighty Featured Playlist Report</a>
							
						</div>
						
						<div class="content-wrap">

							<%
								if ((String)request.getAttribute("status") != null) {
							%>
							<div class="row mar-top-40">
								<div class="col-sm-12">
									<div class="alert alert-success"><%=(String)request.getAttribute("status")%></div>
								</div>
							</div>
							<%
								}
							%>
							
							<div class="section-heading">
								<div class="row">
									<div class="col-sm-12 bold">
										<Strong>Upload Device Firmware</Strong>
									</div>
								</div>
							</div>
							<div class="row mar-top-20">
								<div class="col-sm-12">
									<div class="alert alert-danger san" hidden="hidden"
										style="color: red">
										<p id="errortag"></p>
									</div>
								</div>
							</div>
							
							
							
							
		<form action="deviceFirmwareSubmit" enctype="multipart/form-data" name="deviceFirmwareForm"
				onsubmit="return confirmValidate();" method="post">
								<div class="push-200 login-input-wrap">
								
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey" style="text-align: left;">Effective Date</div>
					<div class="col-md-8 col-sm-7 col-xs-6 mar-top-15">
						 <input type="text" name="fromDate" id="fromDate"  class="form-control" />
					</div>
										
					</div>	
								
								
								
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey " style="text-align: left;">Binary File:</div>
					<div class="col-md-8 col-sm-7 col-xs-6 mar-top-15" >
					  <input type="file" name="file1"  id="file1"  />  
						 
					</div>
					
					</div>	
					
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey " style="text-align: left;">Version File:</div>
					<div class="col-md-8 col-sm-7 col-xs-12 mar-top-15">
						 <input class="textfile" type="file" name="file2" id="file2" />  
					</div>
					
					</div>	
							 
								
					<div class="row text-left mar-btm-30">
					<div class="col-sm-12">
						<input class="btn btn-blue save-btn" type="submit" value="Upload" />
					</div>
					</div>
					<div class="row text-left mar-btm-200">
					<P><b>Note:</b>Version file contains text data with specific format of mighty-(version,hash value,hast type).</P></div>
	  </form>
				</div>
						
					

					</div>
				</div>

			</div>
		</div>
	</div>
<div class="footer-wrap">
					
					<div class="row">
					<div class="col-sm-12 text-center">
							 <p class="text-12">The information stored on this website is maintained in accordance with the organization's Data Privacy Policy. </span><br />Copyright © Mighty
 					</div>
					</div>
					
</div>
		

</body>


</html>