<%@page import="com.team.mighty.domain.*"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="java.util.List"%>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    
    <title>Mighty Admin Home</title>
    
	<script type="text/javascript" src="js/jquery-latest.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom_siemens.css" rel="stylesheet">
    
      
  </head>
  
  <body >
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
					<a href="#" class="current">My Information </a>
					<a href="deviceUserInfo" >Mighty User </a>
					<a href="uploadDeviceFirmware" >Device Firmware Upload</a>
					<a href="deviceFirmwareReport" >Mighty Device Firmware Report</a>
					<a href="mightyDeviceInfo" >Mighty Device Report</a>
					<a href="addDevicePlaylist" >Mighty Featured Playlist</a>
					<a href="devicePlaylist" >Mighty Featured Playlist Report</a>
					
				</div>
				
				<div class="content-wrap">
				
				<%if((String)request.getAttribute("status")!=null){ %>
						<div class="row mar-top-40">
						<div class="col-sm-12">
							<div class="alert alert-success"><%= (String)request.getAttribute("status")%></div>
						</div>
					</div>
						<%} %>
					<div class="row">
						<div class="col-sm-8 page-heading mar-top-20">
							<i class="page-heading-icon"><img src="images/user_icon.png" /></i>
							<h5 class="text-blue text-semi-bold">My Information </h5>
						</div>
												
					</div>
				
				
					<div class="section-heading">
						<div class="row">
							<div class="col-sm-12">
								Personal Information
							</div>
						</div>
					</div>
					
					
					<div class="push-15">
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">First Name:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><%=adminUser.getFirstname()%></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Last Name:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=adminUser.getLastname()%></div>						
					</div>
					
									
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Email:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break"><%=adminUser.getEmailaddress()%></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Mobile No:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><%=adminUser.getContactnumber()%></div>						
					</div>
					
									
					
					
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