<%--
    Document   : device_firmware
    Created on : OCT 09, 2016, 03:51:01 PM
    Author     : Vikky
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.*"%>
<%@page import="com.team.mighty.domain.*"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Change Password</title>
	   <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom_siemens.css" rel="stylesheet">
   
  </head>
  <body class="login-bg">
  <% AdminUser adminUser=(AdminUser)request.getSession().getAttribute("adminUser");%>
  
<div class="wrapper">
 <%@include file="Header.jsp"%> 
	<div class="header-wrap-login">
	<div class="container-fluid">
		
	</div>
	</div>
		
	<div class="container">
			<div class="login-input-wrap">
			<div class="login-input-border-wrap">
			<form action="changePasswordSubmit"  name="changePasswordSubmit" onsubmit="return confirmValidate();" method="post">	
				<div class="row">
					<div class="col-sm-10">
						
						<h><b> <i>CHANGE PASSWORD</i></b>
						<p>Enter New Password</p><input type="password" id="pwd" name="pwd"  class="form-control login-fields password mar-top-10" />&nbsp;&nbsp;&nbsp;
						<p>Confirm New Password</p><input type="password" id="conpwd" name="conpwd"  class="form-control login-fields password mar-top-10" />&nbsp;&nbsp;&nbsp;
						
					</div>
				</div>
				
				
				
				<div class="row text-left mar-btm-30">
					<div class="col-sm-12">
						<input type="submit" value="Save"  class="btn btn-blue" onSubmit="return confirmValidate()"/> 
						<input type="reset" value="Reset" class="btn btn-blue"/> 
					</div>
					</div>
				</form>
			</div>
		</div>
		<div class="modal fade" tabindex="-1" role="dialog" 	id="validationAlertModal">
			<div class="modal-dialog modal-md">
				<div class="modal-content">

					<div class="modal-body">
						<p class="alert alert-warning" id="alertwarn">
							<img src="images/alert_icon.png" />
						</p>
					</div>
					<div class="modal-footer text-center">
						<button type="button" class="btn btn-blue" data-dismiss="modal">OK</button>
					</div>
				</div>
				<!-- /.modal-content -->
			</div>
			<!-- /.modal-dialog -->
		</div>
	</div>
	
	
	
</div>

	

    
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
	<script>
	
	var docHeight = $(window).height();
	//alert(docHeight);
	var footerHeight = $(".login-footer").outerHeight();
	var headerHeight = $(".header-wrap-login").outerHeight() + 50;
	var contentheight = $(".login-bg .container").outerHeight();
	
	
	
	$(window).on('resize', function () {	
		$(".wrapper").css({
			"min-height" : $(window).height(),
			"padding-bottom" : (footerHeight + 30)
		});				
		var marginValue = ($(window).height() - footerHeight - headerHeight - contentheight)/2;		
		function loginMargin() {
			if (marginValue > 0) {
				$(".login-bg .container").css({
					"margin-top": marginValue
				});
			}
		}
		loginMargin();
	});			
	$( document ).ready(function() {		
		$(window).trigger('resize');		
	});	
</script>

<script type="text/javascript">
function confirmValidate() {
	var flag = true;
	var newpwd = $("input[name=pwd]").val();
	var conpwd = $("input[name=conpwd]").val();
	var ogpwd = $("input[name=ogpwd]").val();
	 if(newpwd=="")
		{
		document.getElementById("alertwarn").innerHTML ="Please Enter New Password!";
		$('#validationAlertModal').modal();
		flag=false;
	
		}
	else if(conpwd=="")
		{
		document.getElementById("alertwarn").innerHTML ="Please confirm your Password!";
		$('#validationAlertModal').modal();
		flag=false;
		
		}
	else if(newpwd!= "" && newpwd == conpwd) {
	      if(newpwd.length < 6) {
	    	  document.getElementById("alertwarn").innerHTML ="New Password must contain at least six characters!";
	  		$('#validationAlertModal').modal();
	        flag=false;
	      }
	} 
	else if(newpwd!=conpwd)
		{
		document.getElementById("alertwarn").innerHTML ="New Password And Confirm Password Are Different!";
		$('#validationAlertModal').modal();
		flag=false;
		
		
		}
		
	return flag;
		}
		</script>

  </body>
</html>