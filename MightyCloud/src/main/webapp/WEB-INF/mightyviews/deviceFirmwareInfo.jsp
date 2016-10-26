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
<title>Mighty Device Firmware View</title>
<link rel="stylesheet" href="css/displaytag.css" media="all">
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/style1.css">
<link rel="stylesheet" href="css/style2.css"> 
<script type="text/javascript" src="js/jquery-latest.js"></script>
<!-- <script src="http://code.jquery.com/jquery-latest.js"></script>  -->
<!-- <script src="http://code.jquery.com/jquery-latest.js"></script>  -->

</head>
<body>
	<%
	String fname1=("DeviceFirmwareList :").concat(new Date().toString()).concat(".csv");
	String fname2=("DeviceFirmwareList :").concat(new Date().toString()).concat(".xls");
	String fname3=("DeviceFirmwareList :").concat(new Date().toString()).concat(".xml");
	List<MightyDeviceFirmware> deviceFirmwareList=(List<MightyDeviceFirmware>)request.getAttribute("mightDeviceFirmware");
	%>
		<div id="body">
	<div class="content">
			<hr />
			<h2 align="center">Device Firmware Report</h2>
		 <display:table class="alternateColor" name="<%=deviceFirmwareList%>" id="row"
			export="true" requestURI="" defaultsort="1" defaultorder="descending" pagesize="50">
				<display:column property="createdDt" title="Created_Date"	sortable="true" headerClass="sortable" />
				
				<display:column  property="effectiveDt" title="Effective_Date" sortable="true" headerClass="sortable"/>
				
				<display:column  property="status" title="Status" sortable="true" headerClass="sortable"/>
				
				<display:column property="version" title="Version" 	sortable="true" headerClass="sortable" />
				
				
				     		   
		 	 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			 <display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			 <display:setProperty name="export.xml.filename" value="<%=fname3%>" /> 
		</display:table>
	
		</div>
		</div>
</body>
</html>