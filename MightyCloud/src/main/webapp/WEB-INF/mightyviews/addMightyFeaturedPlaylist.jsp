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

<title>Add Mighty Featured Playlist</title>

<script  src="https://code.jquery.com/jquery-2.2.0.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script src="js/dateValidation.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css">
<script src="js/jquery-ui.js"></script>
<script type="text/javascript">
	 function confirmValidate() {
	    var playlist = $("input[name=playlist]").val();
	    var playlistName = $("input[name=playlistName]").val();
	    var playlistUrl = $("input[name=playlistUrl]").val();
	    var genre = $("input[name=genre]").val();
	  	var flag = true;
		
		if(playlist=="" )
			{
			document.getElementById("errortag").innerHTML = "Please Specify Playlist Id!";
			 $('.validation-required').removeClass("validation-required").addClass("form-control");
			 $('.san').show();
			 document.getElementById("playlistId").focus();
			flag=false;
			
			}
		else if(playlistName==""){
			document.getElementById("errortag").innerHTML = "Please Specify Playlist Name!";
			 $('.validation-required').removeClass("validation-required").addClass("form-control");
			 $('.san').show();
			 document.getElementById("playlistName").focus();
			flag=false;
			
		}else if(playlistUrl==""){
			document.getElementById("errortag").innerHTML = "Please Specify Playlist URL!";
			 $('.validation-required').removeClass("validation-required").addClass("form-control");
			 $('.san').show();
			 document.getElementById("playlistUrl").focus();
			flag=false;
			
		}else if(genre==""){
			document.getElementById("errortag").innerHTML = "Please Specify Genre!";
			 $('.validation-required').removeClass("validation-required").addClass("form-control");
			 $('.san').show();
			 document.getElementById("genreId").focus();
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
							<a href="uploadDeviceFirmware" >Device Firmware Upload</a>
							<a href="deviceFirmwareReport" >Mighty Device Firmware Report</a>
							<a href="mightyDeviceInfo" >Mighty Device Report</a>
							<a href="addDevicePlaylist" class="current">Mighty Featured Playlist</a>
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
										<Strong>Mighty Featured Playlist</Strong>
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
							
							
							
		<form action="mightyFeaturedPlaylist" name="mightyFeaturedPlaylist"	onsubmit="return confirmValidate();" method="post">					
			<div class="push-200 login-input-wrap">
								
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey" style="text-align: left;">Playlist Id</div>
					<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
						 <input type="text" value="" name="playlist" id="playlistId" class="form-control"/>
					</div>
										
					</div>	
								
								
								
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-blue " style="text-align: left;">Playlist Name</div>
					<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15" >
					  <input type="text" value="" name="playlistName" id="playlistName" class="form-control"/>  
						 
					</div>
					
					</div>	
					
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey " style="text-align: left;">Playlist URL</div>
					<div class="col-md-6 col-sm-7 col-xs-12 mar-top-15">
						 <input type="text" value="" name="playlistUrl" id="playlistUrl" class="form-control"/>  
					</div>
					
					</div>	
					
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey " style="text-align: left;">Genre</div>
					<div class="col-md-6 col-sm-7 col-xs-12 mar-top-15">
						 <input type="text" value=""	 name="genre" id="genreId" class="form-control"/> 
					</div>
					
					</div>	
							 
								
					<div class="row text-left mar-btm-30">
					<div class="col-sm-12">
						<input class="formbutton" type="submit" value="Add" />
					</div>
					</div>
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