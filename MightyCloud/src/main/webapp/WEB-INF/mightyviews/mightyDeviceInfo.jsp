<%@page import="java.util.*"%>
<%@page import="com.team.mighty.domain.*"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"   pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mighty Device View</title>
<link rel="stylesheet" href="css/displaytag.css" media="all">
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/style1.css">
<link rel="stylesheet" href="css/style2.css"> 
<script type="text/javascript" src="js/jquery-latest.js"></script>
<!-- <script src="http://code.jquery.com/jquery-latest.js"></script>  -->

</head>
<body>
	<%
	String fname1=("MightyDevices :").concat(new Date().toString()).concat(".csv");
	String fname2=("MightyDevices :").concat(new Date().toString()).concat(".xls");
	String fname3=("MightyDevices :").concat(new Date().toString()).concat(".xml");
	List<MightyDeviceInfo> mightDeviceList=(List<MightyDeviceInfo>)request.getAttribute("mightyDeviceList");
	%>
		<div id="body">
	<div class="content">
			<hr />
			<h2 align="center">Mighty Device Report</h2>
		 <display:table class="alternateColor" name="<%=mightDeviceList%>" id="row"
			export="true" requestURI="" defaultsort="1" defaultorder="descending" pagesize="50">
				<display:column property="deviceId" title="Device_ID"	sortable="true" headerClass="sortable" />
				
				<display:column  property="deviceName" title="Device_Name" sortable="true" headerClass="sortable"/>
				
				<display:column  property="deviceType" title="Device_Type" sortable="true" headerClass="sortable"/>
				
				<display:column property="isRegistered" title="Is_Registered" 	sortable="true" headerClass="sortable" />
				
				<display:column  property="isActive" title="Is_Active" sortable="true" headerClass="sortable"/>
				
			
     		   
		 	 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			 <display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			 <display:setProperty name="export.xml.filename" value="<%=fname3%>" /> 
		</display:table>
	
		</div>
		</div>
</body>
</html>