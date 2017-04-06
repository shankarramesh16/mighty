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
							 
<div class="wrapper">
<%@include file="Header.jsp"%> 
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
					<a href="#" class="current"><b>My Information</b></a>
					<a href="deviceUserInfo" ><b>Mighty User </b></a>
					<a href="uploadDeviceFirmware" ><b>Device Firmware Upload</b></a>
					<a href="deviceFirmwareReport" ><b>Mighty Device Firmware Report</b></a>
					<a href="mightyDeviceInfo" ><b>Mighty Device Report</b></a>
					<a href="addDevicePlaylist" ><b>Mighty Featured Playlist</b></a>
					<a href="devicePlaylist" ><b>Mighty Featured Playlist Report</b></a>
					<a href="addOrderDevice" ><b>Mighty Device Order</b></a>
					
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
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15"><b><%=adminUser.getFirstname()%></b></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Last Name:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><b><%=adminUser.getLastname()%></b></div>						
					</div>
					
									
					
					<div class="row">
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 text-lightgrey">Email:</div>
						<div class="col-md-3 col-sm-7 col-xs-6 mar-top-15 text-break"><b><%=adminUser.getEmailaddress()%></b></div>
						
						<div class="col-md-2 col-sm-5 col-xs-6 mar-top-15 col-md-offset-2 text-lightgrey">Mobile No:</div>
						<div class="col-md-2 col-sm-7 col-xs-6 mar-top-15"><b><%=adminUser.getContactnumber()%></b></div>						
					</div>
					
									
					
					
				</div>
				
				
				
		
				</div>
			</div>
	
		</div>
	</div>
</div>

<%@include file="Footer.jsp"%> 

</div>
   

  </body>
</html>