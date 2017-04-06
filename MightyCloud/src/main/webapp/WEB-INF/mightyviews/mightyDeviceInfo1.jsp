<%@page import="java.util.*"%>
<%@page import="com.team.mighty.domain.*"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ page buffer = "900kb" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>

<!DOCTYPE html >
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mighty Devices View</title>
<link rel="stylesheet" href="css/displaytag.css" media="all">
<link rel="stylesheet" href="css/style.css">
<!-- <link rel="stylesheet" href="css/style1.css">
<link rel="stylesheet" href="css/style2.css"> --> 
<script type="text/javascript" src="js/jquery-latest.js"></script>

<script  src="https://code.jquery.com/jquery-2.2.0.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">

<script type="text/javascript" src="js/jquery-latest.js">

</script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
</head>

<body>
	<% AdminUser adminUser=(AdminUser)request.getSession().getAttribute("adminUser");
	String fname1=("MightyDevices :").concat(new Date().toString()).concat(".csv");
	String fname2=("MightyDevices :").concat(new Date().toString()).concat(".xls");
	String fname3=("MightyDevices :").concat(new Date().toString()).concat(".xml");
	List<MightyDeviceInfo> mightDeviceList=(List<MightyDeviceInfo>)request.getAttribute("mightyDeviceList");
	%>
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
					<a href="adminHome" ><b>My Information</b></a>
					<a href="deviceUserInfo" ><b>Mighty User </b></a>
					<a href="uploadDeviceFirmware" ><b>Device Firmware Upload</b></a>
					<a href="deviceFirmwareReport"><b>Mighty Device Firmware Report</b></a>
					<a href="#" class="current" ><b>Mighty Device Report</b></a>
					<a href="addDevicePlaylist" ><b>Mighty Featured Playlist</b></a>
					<a href="devicePlaylist" ><b>Mighty Featured Playlist Report</b></a>
					<a href="addOrderDevice" ><b>Mighty Device Order</b></a>
														
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
										<Strong>Mighty Device View</Strong>
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
	
		 <display:table class="alternateColor" name="<%=mightDeviceList%>" id="row"
			export="true" requestURI="" defaultsort="1" defaultorder="descending" pagesize="50">
				<display:column style="text-align:center;" property="id" title="ID" sortable="true" headerClass="sortable" />
							
				<display:column  style="text-align:center;" property="deviceId" title="Device_ID/Hw_SerialNo."	sortable="true" headerClass="sortable" />
				
				<display:column  style="text-align:center;" property="deviceName" title="Device_Name" sortable="true" headerClass="sortable"/>
				
				<display:column  style="text-align:center;" property="deviceType" title="Device_Type" sortable="true" headerClass="sortable"/>
				
				<display:column style="text-align:center;" property="swVersion" title="Sw_Version"	sortable="true" headerClass="sortable" />
				
				<display:column style="text-align:center;" property="appBuild" title="App_Build"	sortable="true" headerClass="sortable" />
				
				<%-- <display:column style="text-align:center;" property="appVersion" title="App_Version"	sortable="true" headerClass="sortable" />
				 --%>
				<display:column style="text-align:center;" property="isRegistered" title="Is_Registered" 	sortable="true" headerClass="sortable" />
				
				<display:column  style="text-align:center;" property="isActive" title="Is_Active" sortable="true" headerClass="sortable"/>
				
			
     		   
		 	 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			 <display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			 <display:setProperty name="export.xml.filename" value="<%=fname3%>" /> 
		</display:table>
	
		</div>
						
					

					</div>
				</div>

			</div>
		</div>
	</div>
<%@include file="Footer.jsp"%> 
</body>
</html>