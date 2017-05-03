<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Login</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/custom.css" rel="stylesheet">
	<link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/theme.scss.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />
	<link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/unslider.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />
    <link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/unslider-dots.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />
  	<link href="//cdn.shopify.com/s/files/1/1198/3830/t/6/assets/custom.scss.css?10713448530586305152" rel="stylesheet" type="text/css" media="all" />

<!-- Font Awesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">    
<script type="text/javascript" src="js/jquery-latest.js"></script>

<script  type="text/javascript">
function browserIdentity()
{
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)){ //test for MSIE x.x;
		 var ieversion=new Number(RegExp.$1) // capture x.x portion and store as a number
		  
		   if (ieversion<=7)
		 	 alert("You are using older version of Internet Explorer. Please upgrade your browser.");
		 
}
}
 $(document).ready(function() {
	$( "#uname" ).click(function() {
		var passvalid= document.getElementById("sts").value;
		
		if(passvalid=="" || passvalid=="null"){
			return true;
		}else{
			window.location.reload();
		}
			
		});
	}); 

function validate() {
	var uname = document.getElementById("uname").value;
	var password = document.getElementById("pass").value;
	document.getElementById("namevalid").innerHTML = "";
	document.getElementById("passvalid").innerHTML = "";
	var passvalid= document.getElementById("sts").value;
	
	if(passvalid!="" && passvalid!="null"){
		window.location.reload();
		return false;
	}else if (uname.length < 1 && password.length < 1) {
		document.getElementById("passvalid").innerHTML = "Please enter username/password!";
		document.getElementById("uname").focus(); 
		return false;
	}else if (uname.length < 1) {
		document.getElementById("namevalid").innerHTML = "Please enter user name!";
		document.getElementById("uname").focus(); 
		return false;
	}else if (password.length < 1) {
		document.getElementById("passvalid").innerHTML = "Please enter password!";
		document.getElementById("pass").focus(); 
		return false;
	}  else{
		return true;
	}
	
}
</script>
   
</head>


<body class="login-bg">

<div class="wrapper">
<%@include file="Header.jsp"%> 
	<div class="container">
	
					<%
						response.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
						response.setHeader("Pragma", "co-cache");
						response.setDateHeader("Expires", 0);
  
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
				
						
		<div class="login-input-wrap">
			<div class="login-input-border-wrap">
			<form action="onSubmitlogin" name="user_validation_form" id="user_validation_form" method="post" onsubmit="return validate()">		
				<div class="row">
					<div class="col-sm-12">
						<h5 class="text-regular text-uppercase" style="text-decoration:blink;"><b><i class="fa fa-lock">&nbsp;<b>Please Login</b></i></b></h5>
						<input type="text" name="uname" id="uname"  placeholder="Username" class="form-control login-fields username mar-top-20" /><label id="namevalid" style="color: red;"></label>
						<input type="password" name="pass" id="pass" placeholder="Password"  class="form-control login-fields password mar-top-10" /><label id="passvalid" style="color: red;"></label>
						<label id="passvalid" style="color: red;" ><%=message %></label>
						<input type="hidden" name="sts" id="sts" value="<%=(String)request.getAttribute("status")%>">
					</div>
				</div>
				
				<div class="row">
					<div class="col-sm-6 mar-top-25">
						<a href="forgotPassword" class="text-grey link-underline text-bold ">Forgot Password?</a>
					</div>
					
					<div class="col-sm-6 mar-top-20 text-right ">
						<input type="submit" value="Login" class="btn btn-blue text-bold"/>
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
	

  </body>
</html>