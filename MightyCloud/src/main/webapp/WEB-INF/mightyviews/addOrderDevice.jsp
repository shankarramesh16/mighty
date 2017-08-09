<%--
    Document   : device_firmware
    Created on : OCT 09, 2016, 03:51:01 PM
    Author     : Vikky
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*"%>
<%@page import="com.team.mighty.domain.*"%>
<%@page import="org.displaytag.decorator.TotalTableDecorator"%>
<%@page import="org.displaytag.decorator.MultilevelTotalTableDecorator"%>
<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display"%>
<!DOCTYPE html >
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Add Device Order</title>

<script  src="https://code.jquery.com/jquery-2.2.0.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">
<link rel="stylesheet" href="css/displaytag.css" media="all">

<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script src="js/dateValidation.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css">
<script src="js/jquery-ui.js"></script>
<script type="text/javascript">
	/*  function confirmValidate() {
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

	}  */
</script>

</head>
<body>
	<%
	String fname1=("DeviceOrderList :").concat(new Date().toString()).concat(".csv");
	String fname2=("DeviceOrderList :").concat(new Date().toString()).concat(".xls");
	String fname3=("DeviceOrderList :").concat(new Date().toString()).concat(".xml"); 
	
	AdminUser adminUser=(AdminUser)request.getSession().getAttribute("adminUser");
	
	List<MightyDeviceOrderInfo> mightyDeviceOrderList=(List<MightyDeviceOrderInfo>)request.getAttribute("mightyDeviceOrder");

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
					<a href="deviceFirmwareReport" ><b>Mighty Device Firmware Report</b></a>
					<a href="mightyDeviceInfo" ><b>Mighty Device Report</b></a>
					<li><a href="mightyToCloudLog"><i class="fa fa-upload"></i> <b>Mighty to cloud Log Upload </b></a></li>
					<a href="addDevicePlaylist" ><b>Mighty Featured Playlist</b></a>
					<a href="devicePlaylist" ><b>Mighty Featured Playlist Report</b></a>
					<a href="#" class="current"><b>Mighty Device Order</b></a>
							
							
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
										<Strong>Mighty Device Order</Strong>
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
							
							
							
		<form action="mightyDeviceOrdersubmit" name="mightyDeviceOrder"	onsubmit="return confirmValidate();" method="post">					
			<div class="push-200 login-input-wrap">
								
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey" style="text-align: left;">OrderID:</div>
					<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
						 <input type="text" value="" name="orderId" id="orderId" class="form-control"/>
					</div>
										
					</div>	
								
								
								
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-blue " style="text-align: left;">IS_GIFT:</div>
					<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15" >
					  <select name="giftId" id="giftId" style="width: 160px" class="form-control">
							<option value="N">NO</option>
							<option value="Y">YES</option>
							
					  </select>
					</div>
					
					</div>	
					
					<div class="row">
					
					<div class="col-md-5 col-sm-5 col-xs-6 mar-top-15 text-lightgrey " style="text-align: left;">DeviceSerial#</div>
					<div class="col-md-6 col-sm-7 col-xs-6 mar-top-15">
						 <input type="text"  name="deviceId" id="deviceId" class="form-control"/>  
					</div>
					
					</div>	
					
												 
								
					<div class="row text-left mar-btm-30">
					<div class="col-sm-12">
						<input class="btn btn-blue save-btn" type="submit" value="Add" />
					</div>
					</div>
			</form>
			
				</div>
						
					<% if(mightyDeviceOrderList!=null){
			System.out.println("listsize"+mightyDeviceOrderList.size());%>
			 <display:table class="alternateColor" name="<%=mightyDeviceOrderList%>" id="row"
			export="false" requestURI=""  defaultsort="1" defaultorder="descending" pagesize="50">
			<%-- <display:column property="id" title="ID" sortable="true" headerClass="sortable" /> --%>
				<display:column property="orderId" title="OrderId" sortable="true"  />
				<display:column property="isGift" title="Is_Gift" sortable="true"  />
				<display:column property="deviceSerialNo" title="Device_Serial#" 	sortable="true"  />
				<display:column property="createdDt" title="Created_Date"	format="{0,date,dd-MM-yyyy}" sortable="true"  />
				<display:column  property="updatedDt" title="Updated_Date" format="{0,date,dd-MM-yyyy}" sortable="true" />
				<display:column property="createdBy" title="CreatedBy" sortable="true"  />
				<display:column property="updatedBy" title="updatedBy" sortable="true"  />
				
				<display:column  title="Edit"   ><a onclick="fillFieldsEdit('${row.id}')" title="Edit"><i><img src="images/edit1.png" /></i></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</display:column>
				
				<display:column  title="Delete"   ><a onclick="fillFieldsDelete('${row.id}')" title="Delete"><i><img src="images/delete1.png" /></i></a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</display:column>
				     		   
		 	 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
			 <display:setProperty name="export.excel.filename" value="<%=fname2%>" />
			 <display:setProperty name="export.xml.filename" value="<%=fname3%>" /> 
		</display:table>
		<% }%>

					</div>
					
				</div>

			</div>
			
		</div>
		
	</div>
	
<%@include file="Footer.jsp"%> 
		

</body>


</html>