<%--
    Document   : device_firmware
    Created on : OCT 12, 2016, 03:51:01 PM
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
<title>Mighty Featured Playlist</title>

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
	    var playlist = $("input[name=playlist]").val();
	    var playlistName = $("input[name=playlistName]").val();
	    var playlistUrl = $("input[name=playlistUrl]").val();
	    var genre = $("input[name=genre]").val();
	  	var flag = true;
		
		if(playlist=="" )
			{
			flag=false;
			alert("Please Specify Playlist Id!");
			}
		else if(playlistName==""){
			flag=false;
			alert("Please Specify Playlist Name!");
		}else if(playlistUrl==""){
			flag=false;
			alert("Please Specify Playlist URL!");
		}else if(genre==""){
			flag=false;
			alert("Please Specify Genre!");
		}
				
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
			<form action="mightyFeaturedPlaylist" name="mightyFeaturedPlaylist"
				onsubmit="return confirmValidate();" method="post">
				<h3>Add Mighty Featured Playlist</h3>
				<table align="center">
				<tr>
						<td align="right">Playlist Id</td>
						<td><input type="text" value=""	 name="playlist" id="playlistId" /> 
						</td>
					</tr>
					
					<tr>
						<td align="right">Playlist Name</td>
						<td><input type="text" value=""	 name="playlistName" id="playlistName" /> 
						</td>
					</tr>
					<tr>
						<td align="right">Playlist URL</td>
						<td><input type="text" value=""	 name="playlistUrl" id="playlistUrl" /> 
						</td>
					</tr>
					<tr>
						<td align="right">Genre</td>
						<td><input type="text" value=""	 name="genre" id="genreId" /> 
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
