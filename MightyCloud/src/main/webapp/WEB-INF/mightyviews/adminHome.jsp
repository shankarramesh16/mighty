<%@page import="com.itextpdf.text.log.SysoLogger"%>
<%@page import="com.team.mighty.domain.*"%>
<%@page import="java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>Admin Home</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>
<body>

        <%
    	AdminUser adminUserObj = (AdminUser) request.getSession().getAttribute("adminUser");
        if (adminUserObj == null) {
			response.sendRedirect("/");
		} else {
		%>
	<%@include file="Header.jsp"%> 
	<%
		 }     
        %>
	
	
	<%-- <%@include file="Header.jsp"%>  --%>
	<main class="main-content" role="main">
	<div class="container">
	<div class="row">
					<div class="col-sm-12">
		
		<%-- <%="HI Admin" %> --%>
			  <%-- <div id="content">
			<%@include file="Footer.jsp"%>
			</div>   --%>
		
		</div>
		</div>
		</div>
		</main>
		
</body>
</html>
