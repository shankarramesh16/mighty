<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Forgot Password</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
    <link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/theme.scss.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />
<link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/unslider.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />
<link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/unslider-dots.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />
<link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/custom.scss.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />
    
	
  </head>
  <body class="login-bg">
  
<div class="wrapper">
	<%@include file="Header.jsp"%> 	
	<div class="container">
	<div class="login-input-wrap">
			<div class="login-input-border-wrap">
							<%
						  
						String message="";
						
						try{
							message=request.getParameter("message");
							if(message!=null&&!message.equals(""))
									{
							}
							else
								{						
							message = "";
							message = request.getAttribute("status").toString();
						session.setAttribute("status", "");
								}
				
						}catch(Exception e)
						{
							;
						}
					
					%>	
							<div class="row mar-top-20">
								<div class="col-sm-12">
									<div class="alert alert-danger san" hidden="hidden"
										style="color: red">
										<p id="errortag"></p>
									</div>
								</div>
							</div>
							
			<form name="ChangePassword" action="resetPassword" method="post" onSubmit="return confirmValidate()">		
				<div class="row">
					<div class="col-sm-12">
						<h6 class="text-regular text-uppercase"><b>Forgot Password</b></h6>
						
						<input type="text" name="email" id="email" placeholder="Email Address" class="form-control" />
						<label id="emailvalid" style="color: red;" ><%=message%></label>
						
					</div>
				</div>
				
				<div class="row">
					<div class="col-sm-6 mar-top-20 text-left">
						<input type="submit" class="btn btn-blue" value="Submit"/>
					</div>
				
				<div class="col-sm-6 mar-top-20 text-right">
						<input type="button" value="Back"  onclick="window.history.back()" class="btn btn-blue"/>
					</div>
					</div>
				</form>
	</div>
	</div>
	</div>
	
	
</div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
	<script src="http://maps.googleapis.com/maps/api/js"></script>
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
	var email=document.getElementById("email").value;
		
	if(email==null || email==""){
		 document.getElementById("emailvalid").innerHTML = "Please enter email address!";
		 document.getElementById("email").focus(); 
		flag=false;
	}
	return flag;
		}
		
		</script>
		</div>
  </body>
</html>