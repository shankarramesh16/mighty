<%--
    Document   : device_firmware
    Created on : OCT 09, 2016, 03:51:01 PM
    Author     : Vikky
--%>

<%@page import="java.util.*"%>
<%@page import="com.team.mighty.domain.*"%>
<%@page import="com.team.mighty.dto.*"%>
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

<title>Mighty Firmware </title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom_siemens.css" rel="stylesheet">
<!-- Font Awesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
<!-- Ionicons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
<link rel="stylesheet" href="css/AdminLTE.min.css">
<link rel="stylesheet" href="css/skins/_all-skins.min.css">
<link rel="stylesheet" href="css/slider.css">


<script type="text/javascript" src="js/jquery-latest.js"></script>
<script  src="https://code.jquery.com/jquery-2.2.0.js"></script>
<script type="text/javascript" src="js/jquery-latest.js"></script>
<script type="text/javascript" src="js/jquery.validate.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script src="js/app.min.js"></script>
<script src="js/demo.js"></script>
<script src="js/scroller.js"></script>

<script type="text/javascript">



function deleteEvent(id) {
	
	var x = confirm("Are you sure you want to delete?");
	
    if (x){
    	
    	$.ajax({
            url: 'deleteFirmware',
            type: "POST",
            data: 'id='+id,
            success: function (data) {
            alert(data);
            window.location.reload();      	
                },
		 error: function(e){
	     			        alert('Error: ' + e);
	      }

               
            });
        return true;
    }else{
    	return false;
    }
      
  }
  
function editEvent(id) {
	
	alert("Not implemented");
	         
  }
		
</script>

</head>

<body class="hold-transition skin-blue sidebar-mini">
						<% AdminUser adminUser=(AdminUser)request.getSession().getAttribute("adminUser");
								String fname1=("DeviceFirmwareList :").concat(new Date().toString()).concat(".csv");
								String fname2=("DeviceFirmwareList :").concat(new Date().toString()).concat(".xls");
								String fname3=("DeviceFirmwareList :").concat(new Date().toString()).concat(".xml");
						   List<DeviceFirmWareDTO> deviceFirmwareList=(List<DeviceFirmWareDTO>)request.getAttribute("mightDeviceFirmware");
						%>
	
<div class="wrapper">  
  	<header class="main-header" >
   
	    <a href="https://bemighty.com" class="logo affix" >
			      
			     <svg width="121px" height="50px" viewBox="445 13 150 27" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
			                      					<defs>
			                         			 		<linearGradient x1="50%" y1="0%" x2="50%" y2="100%" id="linearGradient-1">
			                              					<stop stop-color="#F8F8F8" offset="0%"></stop>
			                              					<stop stop-color="#EEEEEE" offset="100%"></stop>
			                          					</linearGradient>
			                     					</defs>
			                     				                                        <path d="M538.634433,25.8630211 C538.249258,24.4232711 537.323724,23.2694736 536.165841,22.4791419 C534.59942,21.4100228 532.739348,21.1833452 530.855271,21.3444513 C530.466238,21.7843447 529.856002,21.3939556 529.413812,21.7635008 C529.064003,22.055967 528.587947,22.1873271 528.196985,22.4368027 C527.738933,22.7288346 527.310675,23.068851 526.873415,23.3934515 C526.788106,23.4568517 526.703655,23.534365 526.648997,23.6242544 C526.580836,23.7367247 526.564975,23.8845862 526.48824,23.9881544 C526.382354,24.1314563 526.252891,24.2643363 526.110138,24.3696415 C526.024829,24.4326075 525.999966,24.6597193 525.775763,24.5075153 L525.775763,14.006525 L519.805872,14.006525 L519.805872,43.0123515 L525.784765,43.0123515 L525.784765,42.5420607 C525.784765,38.7443429 525.786694,34.9466252 525.78305,31.1489074 C525.782407,30.4515048 525.979174,29.7929674 526.142075,29.1281334 C526.318051,28.4098868 526.822401,27.9059418 527.361046,27.4786415 C527.981141,26.9862041 528.798218,26.9762164 529.535987,26.7925294 C529.761048,26.7365114 530.041623,26.9718739 530.299693,26.9799075 C531.204865,27.0079165 531.923558,27.4710422 532.365105,28.1697476 C532.785647,28.8350158 533.090872,29.6010297 533.085084,30.4701775 C533.057005,34.6294069 533.072438,38.7888534 533.072438,43.0568619 L539.074051,43.0568619 L539.074051,41.9560427 C539.074051,37.5073869 539.052402,33.0582968 539.091199,28.6098581 C539.099558,27.6493011 538.87364,26.7573553 538.634433,25.8630211 L538.634433,25.8630211 Z M551.549476,26.9542868 C550.864649,25.1951473 550.202542,23.4948484 549.532291,21.7730543 L546.567279,21.7730543 L546.567279,16.3740463 L540.574454,16.3740463 L540.574454,17.3478479 C540.574454,24.0710958 540.573383,30.7943438 540.576169,37.5175917 C540.576384,38.1809059 540.621181,38.8279357 540.758147,39.4945067 C541.014716,40.7453587 541.81293,41.9983819 542.933731,42.6278247 C543.895061,43.1678123 544.93827,43.4029577 546.008058,43.4756943 C546.870147,43.5345349 547.732235,43.4932814 548.599683,43.2972183 C549.519001,43.0894305 550.368444,42.7320442 551.214457,42.3520771 C551.358496,42.2873741 551.490746,42.0151005 551.493532,41.8366244 C551.51561,40.3669114 551.505964,38.896764 551.505964,37.3836261 C551.245537,37.42184 551.051556,37.399042 550.916091,37.4787265 C550.022494,38.0037326 549.051733,38.1025241 548.049249,38.0640931 C547.519821,38.0436834 547.204522,37.6724012 546.831993,37.3643021 C546.521196,37.1074443 546.577997,36.8012993 546.577568,36.4888577 C546.574996,33.5971989 546.576068,30.70554 546.576068,27.813664 L546.576068,26.9542868 L551.549476,26.9542868 Z M570.083958,40.7143099 C569.491298,40.7006311 568.89821,40.6967229 568.305337,40.7032366 C568.277901,40.7034537 568.232246,40.8157069 568.230102,40.8769359 C568.228173,40.938382 568.262682,41.050418 568.294191,41.0554119 C568.530183,41.0927572 568.769176,41.1092587 569.082117,41.1392218 C569.082117,41.7493405 569.0714,42.2875912 569.093691,42.8245391 C569.097121,42.9053093 569.254663,42.9795657 569.340829,43.0568619 C569.397416,42.9854281 569.501587,42.915297 569.503301,42.8425604 C569.517019,42.2771692 569.510803,41.7111267 569.510803,41.1416102 C569.739508,41.1144697 569.909482,41.1046991 570.073026,41.0667024 C570.110965,41.0580174 570.14676,40.9535807 570.148475,40.8923517 C570.149975,40.8311227 570.108607,40.7149613 570.083958,40.7143099 L570.083958,40.7143099 Z M569.824816,29.608629 C570.229711,28.5842375 570.591737,27.5437788 570.99706,26.518953 C571.606653,24.9786746 572.121934,23.4001823 572.698303,21.7786995 C570.828372,21.7786995 568.978803,21.7763111 567.129449,21.7802194 C566.545149,21.781305 566.61181,21.8223415 566.268861,22.5225667 C566.085168,22.8973229 565.999002,23.2853237 565.882185,23.6633368 C565.64405,24.4332588 565.197573,25.1471629 565.223937,25.9937298 C564.664287,26.6348973 564.724517,27.510993 564.395929,28.2522547 C563.923945,29.31638 563.582496,30.4547617 563.312209,31.5922749 C562.961329,33.0695873 562.405965,34.4678665 561.936125,35.9501728 C561.796158,35.6831101 561.655335,35.4876984 561.585459,35.2692716 C561.14627,33.8968301 560.72187,32.5196119 560.294255,31.1432622 C559.867283,29.7684324 559.284483,28.4487521 558.938319,27.0430906 C558.73898,26.2334348 558.42068,25.453525 558.14975,24.6623248 C557.868103,23.8400758 557.581097,23.0197809 557.291948,22.2001374 C557.205567,21.9554385 557.072889,21.7747913 556.770451,21.7756598 C554.814996,21.7817393 552.859328,21.7786995 550.818994,21.7786995 C551.422585,23.5660652 552.100981,25.2238077 552.774019,26.8869784 C553.22414,27.999088 553.622176,29.1350814 554.056435,30.255876 C554.514058,31.4374654 554.956249,32.6292596 555.377219,33.8273504 C555.752963,34.8966866 556.241022,35.9249864 556.645274,36.9847692 C557.097538,38.1704839 557.489572,39.3800823 557.949339,40.5625402 C558.263352,41.370459 558.655386,42.147329 558.995334,42.9454772 C559.047205,43.0677181 559.068211,43.2939614 558.998121,43.3640925 C558.466978,43.8962637 558.048365,44.608865 557.147052,44.580856 C556.481516,44.5600121 555.814908,44.5767307 555.151944,44.5767307 L555.151944,49.5912119 C556.614409,49.4639772 558.002067,49.6730677 559.410303,49.3925433 C560.422646,49.190835 561.257942,48.7900239 562.015646,48.1471195 C563.19925,47.1433547 563.776905,45.7224946 564.33527,44.3461449 C564.835118,43.1137484 565.452213,41.9039329 565.654553,40.5579806 C566.44698,39.0778455 566.914677,37.4665676 567.461681,35.8900294 C567.768835,35.0052487 568.167943,34.1604188 568.454948,33.2656503 C568.852341,32.0269573 569.346616,30.819313 569.824816,29.608629 L569.824816,29.608629 Z M572.484388,40.7690252 C572.441305,40.7043222 572.179592,40.6856495 572.143368,40.7366737 C571.91745,41.0545434 571.727113,41.3982509 571.526274,41.7313192 C571.29414,41.3541747 571.28278,40.7989883 570.727202,40.633974 C570.182127,41.373933 570.396256,42.1903196 570.401614,42.9743548 C570.401829,43.0023638 570.524647,43.0642441 570.571374,43.0492626 C570.654325,43.0225563 570.783574,42.9526423 570.787004,42.8946702 C570.806938,42.5401065 570.796863,42.1838059 570.796863,41.8281566 C570.848091,41.7962394 570.899105,41.7645392 570.950119,41.7328391 C571.042072,41.9119665 571.101445,42.1243139 571.236267,42.2595822 C571.336366,42.3601107 571.536134,42.3968046 571.687674,42.3885539 C571.752835,42.3850799 571.811779,42.1983532 571.866437,42.0908768 C571.881012,42.0624335 571.853362,42.0046785 571.871152,41.9866572 C571.952817,41.9039329 572.043699,41.8307621 572.131151,41.7541173 C572.186666,41.8272881 572.285478,41.8980706 572.289765,41.974064 C572.307555,42.2973618 572.291694,42.6223966 572.304554,42.9461286 C572.306269,42.985211 572.413869,43.0562105 572.463168,43.0490454 C572.554264,43.0355837 572.699589,42.9919418 572.716094,42.9313642 C572.82005,42.5472716 572.668509,41.0456413 572.484388,40.7690252 L572.484388,40.7690252 Z M491.762906,37.7933393 C490.982268,35.4097509 490.262074,33.007924 489.363761,30.6618981 C488.635208,28.7590224 488.006539,26.8118534 487.429741,24.8553481 C486.520283,21.7704488 485.361328,18.7723991 484.416932,15.7009615 C484.338268,15.444538 484.201945,15.2645421 483.908724,15.2645421 C482.409178,15.2654106 480.909417,15.2738784 479.409872,15.2575941 C479.104218,15.2543372 478.904021,15.4202201 478.85708,15.6447264 C478.669744,16.5392777 478.283712,17.3591383 477.988347,18.2126532 C477.572735,19.4133495 477.138904,20.609052 476.772163,21.8251641 C476.288604,23.4284085 475.599705,24.958482 475.186451,26.5882156 C474.948744,27.5257575 474.63666,28.4550487 474.251485,29.3402636 C473.728702,30.5424798 473.391325,31.8022339 472.970569,33.0600338 C472.894049,32.9933767 472.783876,32.944958 472.757512,32.8674447 C470.762619,27.0129103 468.77437,21.1559876 466.780548,15.2845175 L461.515205,15.2845175 C458.423516,24.5101208 455.333114,33.7329014 452.25,42.9331012 C452.312588,42.9965014 452.337881,43.0444858 452.363173,43.0444858 C454.254324,43.0509996 456.14569,43.0509996 458.036841,43.0590332 C458.454168,43.0607701 458.589204,42.8796886 458.718453,42.455211 C459.178434,40.9457642 459.559322,39.4078742 460.058313,37.9175343 C460.803799,35.6911437 461.444471,33.4352243 462.113008,31.1875555 C462.61007,29.5154828 463.216662,27.8779328 463.632059,26.1798051 C463.758951,25.6613128 463.967292,25.1632301 464.172204,24.5557168 C466.18446,30.7739341 468.172494,36.9178949 470.157956,43.0531708 L475.563481,43.0531708 C475.829052,42.3034413 476.041466,41.5241828 476.310038,40.7716307 C477.367394,37.8091894 478.261634,34.7929013 479.276979,31.8174326 C479.548123,31.0223241 479.780686,30.2189649 480.010033,29.4106118 C480.429931,27.9300426 480.940283,26.4755282 481.420841,25.0127631 C481.446133,24.9352498 481.541945,24.8360241 481.610749,24.8310302 C481.684912,24.8258192 481.831522,24.9154915 481.834738,24.9704239 C481.906971,26.2099854 482.554074,27.2832299 482.810214,28.4765439 C483.090146,29.7816769 483.628148,31.0275351 483.976242,32.3207262 C484.334409,33.6514799 484.806608,34.9446711 485.149128,36.2823727 C485.53859,37.805064 486.087309,39.2858504 486.558007,40.7876979 C486.796786,41.5495864 487.016059,42.3177715 487.223114,43.0125686 L493.604544,43.0125686 C493.391058,42.4528226 493.178001,41.9317248 492.992808,41.4006392 C492.574624,40.2012457 492.15837,39.0007665 491.762906,37.7933393 L491.762906,37.7933393 Z M495.133669,21.809314 L488.917713,21.809314 C488.917713,22.8080849 488.916855,23.7766755 488.918785,24.7454833 C488.919213,24.886614 488.904209,25.0377323 488.946864,25.1671383 C489.239228,26.0547417 489.545953,26.9375683 489.846891,27.8225661 C490.394967,29.4342784 490.937899,31.0479447 491.491119,32.65792 C492.044125,34.2672438 492.683725,35.8509471 493.144349,37.486543 C493.635195,39.2293982 494.43341,40.8762845 494.713556,42.6834084 C494.723202,42.7457231 494.861025,42.8314871 494.934759,42.8271446 C495.004206,42.8232364 495.118451,42.7192339 495.124667,42.651057 C495.147602,42.4004957 495.133669,42.1462434 495.133669,41.893511 L495.133669,21.809314 Z M494.800365,13.9665742 C492.85027,13.957455 490.89996,13.9622317 488.917713,13.9622317 C488.917713,15.7654474 488.917498,17.4937553 488.919213,19.2220632 C488.919213,19.2535462 488.935289,19.2885031 488.953079,19.3152094 C488.97237,19.344304 489.002807,19.3655822 489.028314,19.3903343 L495.348013,19.3903343 C495.348013,17.7391055 495.353371,16.1560537 495.344369,14.5732189 C495.342011,14.1704537 495.163463,13.9683112 494.800365,13.9665742 L494.800365,13.9665742 Z M511.856307,33.8846712 C511.117465,35.0369488 510.108551,35.7610577 508.794412,36.1290831 C508.210327,36.2925775 507.639959,36.3551093 507.057589,36.3190667 C505.071055,36.1963915 503.202838,35.1376944 502.481573,32.7912342 C502.361326,32.4006279 502.48093,31.9066706 502.295308,31.5705625 C502.012375,31.0577153 502.49872,30.735286 502.452851,30.280411 C502.415555,29.9117343 502.623468,29.4998499 502.782725,29.1339957 C502.94284,28.7659704 503.120316,28.3755812 503.390174,28.0931028 C504.077359,27.3737706 504.89465,26.814676 505.864768,26.5745368 C506.156489,26.5024516 506.452712,26.3875929 506.737145,26.3569784 C508.059644,26.2141108 509.36628,26.529375 510.422564,27.246753 C511.668756,28.0933199 512.54735,29.333967 512.495907,31.0062569 C512.492692,31.1146018 512.495479,31.2231639 512.495479,31.4272605 C512.583145,32.2599315 512.358942,33.1010703 511.856307,33.8846712 L511.856307,33.8846712 Z M518.497092,22.7388223 L518.497092,21.8069256 L512.471258,21.8069256 L512.471258,24.4421609 C511.920396,23.9718701 511.439838,23.5560774 510.995933,23.1342053 C509.876418,22.0705143 508.474612,21.7572042 507.100028,21.3611699 C506.422061,21.1657582 503.756058,21.3270814 503.145394,21.6175934 C502.27859,22.0301292 501.361629,22.2932836 500.52419,22.8287117 C499.166968,23.6967739 498.267369,24.888351 497.475799,26.2369088 C496.586703,27.7513494 496.423373,29.4555565 496.232822,31.1354457 C496.143869,31.9190466 496.218675,32.7775554 496.449094,33.5298904 C496.828911,34.7703204 497.141638,36.0268176 497.88005,37.1695418 C498.625108,38.3224708 499.551714,39.2515448 500.622144,39.9819503 C501.68593,40.707362 502.974134,41.1498609 504.249691,41.27601 C505.340698,41.3837036 506.463429,41.4149694 507.59409,41.1368334 C509.055483,40.7772759 510.342186,40.2151416 511.37382,39.0945641 C511.700908,38.7391319 512.050716,38.4049779 512.459898,37.990488 C512.538562,39.0906559 512.507482,40.0614177 512.287994,41.0326139 C512.048787,42.0910939 511.019939,43.4735231 509.992592,43.9553215 C509.2276,44.3137934 508.451678,44.6357884 507.567511,44.5858499 C506.793518,44.5422079 506.015237,44.5767307 505.228811,44.5767307 L505.228811,49.4874266 C505.276824,49.526726 505.304046,49.568631 505.330196,49.5677625 C507.099814,49.5150013 508.896226,49.7907489 510.617831,49.1363369 C511.624602,49.1534897 512.491192,48.6795078 513.398293,48.3366688 C514.470009,47.9312981 515.203707,47.0973244 516.02014,46.3480291 C516.835288,45.6004708 517.273834,44.6331829 517.617212,43.6472224 C518.17129,42.0567883 518.513382,40.4109876 518.505666,38.6957071 C518.482088,33.3768179 518.497092,28.0577115 518.497092,22.7388223 L518.497092,22.7388223 Z" id="Fill-1" stroke="none" fill="url(#linearGradient-1)" fill-rule="evenodd"></path>
			                     					
			                     					
			     </svg>
	    </a>

	    <!-- Header Navbar: style can be found in header.less -->
	    <nav class="navbar navbar-static-top affix" >
	      <!-- Sidebar toggle button-->
	      <a href="#" class="sidebar-toggle" style="width:2.5em;" data-toggle="offcanvas" role="button">
	      </a> 
	    </nav>
	    
   </header>
  
  
  <aside class="main-sidebar affix" style="position:fixed;">
   
    <section class="sidebar">
        
      <!-- search form -->
      <form action="#" method="get" class="sidebar-form">
        <div class="input-group">
          <input type="text" name="q" class="form-control" placeholder="Search...">
              <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
        </div>
      </form>
     
      
      <ul class="sidebar-menu">
        <li class="header"><b>MAIN NAVIGATION</b></li>
        <li class="active treeview">
          <a href="adminHome">
            <i class="fa fa-dashboard"></i> <span><b>Dashboard</b></span>
          </a>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-files-o"></i>
            <span><b>Reports</b></span>
            <span class="pull-right-container">
              <span class="label label-primary pull-right">4</span>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="deviceUserInfo"><i class="fa fa-circle-o"></i><b>Mighty User</b></a></li>
            <li><a href="mightyDeviceInfo"><i class="fa fa-circle-o"></i><b>Mighty Device</b></a></li>
            <li><a href="#"><i class="fa fa-circle-o"></i><b>Device Firmware/OTA </b></a></li>
             <li><a href="mightyDlAuditLog"><i class="fa fa-download"></i> <b>Mighty Downloading AuditLog </b></a></li>
             <li><a href="otaFileUploadedReport"><i class="fa fa-upload"></i> <b>MightyUser Excel Upload </b></a></li>
             <li><a href="mightyToCloudLog"><i class="fa fa-download"></i> <b>Mighty to Cloud Log Download </b></a></li>
            <li><a href="#"><i class="fa fa-circle-o"></i> <b>Mighty Feature Playlist </b></a></li>
          </ul>
        </li>
        
        <li class="treeview">
          <a href="#">
            <i class="fa fa-upload"></i>
            <span><b>Upload</b></span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="uploadDeviceFirmware"><i class="fa fa-circle-o"></i><b>Device Firmware/OTA</b></a></li>
            <li><a href="otaFileUploading"><i class="fa fa-circle-o"></i><b>OTA Excel Upload</b></a></li>
           
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-user"></i>
            <span><b>User Management</b></span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="userMgmt"><i class="fa fa-circle-o"></i><b> User Mgmt</b></a></li>
           
          </ul>
        </li>
        
        <li class="treeview">
          <a href="#">
            <i class="fa fa-download"></i>
            <span><b>Log Handling</b></span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="mightyLog"><i class="fa fa-circle-o"></i><b> MightyLogs</b></a></li>
           
          </ul>
        </li>
        <li class="treeview">
          <a href="#">
            <i class="fa fa-shopping-cart"></i>
            <span><b>Orders</b></span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
          </a>
          <ul class="treeview-menu">
            <li><a href="#"><i class="fa fa-circle-o"></i><b> Mighty Device</b></a></li>
           
          </ul>
        </li>
        
      </ul>
    </section>
  
  </aside>
 
	<div class="content-wrapper">
		
			<section class="content">
		 		<div class="content-wrap box box-primary">
		 		
			 			
						
						<div class="row">
							<div class="col-sm-12 text-right ">	
							    <img src="images/user_iocn_header.png" />&nbsp;<b>Welcome  <%=adminUser.getDisplayname()%></b>  &nbsp;&nbsp;&nbsp;<a href="logout"><img src="images/logout_icon_header.png" />&nbsp;<b>Log Out</b></a>
							</div>
													
						</div><br/>
					
					
						<div class="row">
								<div class="col-sm-8 page-heading mar-top-20">
									<h5 class="text-blue text-semi-bold"><i class="fa fa-cloud-upload"></i>&nbsp;&nbsp;<b>Device Firmware/OTA</b></h5>
								</div>
													
						</div><br/>
						
						<div class="row" style="overflow-y: auto;">
							<div class="col-sm-12" >	
							
							
							<table class="table table-hover text-center">
									    <thead>
									      <tr class="text-blue text-semi-bold ">
									        <th>ID</th>
									        <th>FileName</th>
									        <th>SwVersion</th>
									        <th>HashValue</th>
									        <th>HashType</th>
									        <th>Requires</th>
									        <th>CompatibleIOS</th>
									        <th>CompatibleAND</th>
									        <th>CompatibleHW</th>
									        <!-- <th>CreatedDate</th>
									        <th>EffectiveDate</th> -->
									        <th>Status</th>
									        <th>Action</th>
									      </tr>
									    </thead>
									    <tbody>
									    <% if(deviceFirmwareList!=null && !deviceFirmwareList.isEmpty()){
										    for(DeviceFirmWareDTO dto :deviceFirmwareList){
										    %>
									      <tr>
									        <td><%=dto.getId()%></td>
									        <td><%=dto.getFileName()%></td>
									        <td><%=dto.getVersion()%></td>
									        <td><%=dto.getHashValue()%></td>
									        <td><%=dto.getHashType()%></td>
									        <td><%=dto.getRequires()%></td>
									        <td><%=dto.getCompatibleIOS()%></td>
									        <td><%=dto.getCompatibleAND()%></td>
									        <td><%=dto.getCompatibleHW()%></td>
									        <%-- <td><%=dto.getCreatedDt()%></td>
									        <td><%=dto.getUpdatedDt()%></td> --%>
									        <td><%=dto.getStatus()%></td>
									        <td>
									      		 <a onclick="editEvent('<%=dto.getId()%>')" title="EDIT"></i> <i class="fa fa-edit" style="color:blue;"></i></a>&nbsp;&nbsp;&nbsp;
												 <a onclick="deleteEvent('<%=dto.getId()%>')" title="DELETE"></i> <i class="fa fa-trash" style="color:red;"></i></a>
											</td>	 
									      </tr>
									      	<%}
									    }%>
									    </tbody>
									  </table>
						
					   <%--  <display:table class="table table-hover" name="<%=deviceFirmwareList%>" id="row"
									export="true" requestURI=""  defaultsort="1" defaultorder="descending" pagesize="50">
								<display:column property="id" title="ID" sortable="true" headerClass="sortable" />
							 	<display:column property="fileName" title="FileName" sortable="true"  />
								<display:column property="version" title="SwVersion" 	sortable="true"  />
								<display:column property="hashValue" title="HashValue" sortable="true"  />
								<display:column property="hashType" title="HashType" sortable="true"  />
								<display:column property="requires" title="Requires" sortable="true"  />
								<display:column property="compatibleIOS" title="CompatibleIOS" sortable="true"  />
								<display:column property="compatibleAND" title="CompatibleAND" sortable="true"  />
								<display:column property="compatibleHW" title="CompatibleHW" sortable="true"  />
								<display:column property="createdDt" title="CreatedDate"	format="{0,date,dd-MM-yyyy}" sortable="true"  />
								<display:column property="effectiveDt" title="EffectiveDate" format="{0,date,dd-MM-yyyy}" sortable="true" />
								<display:column property="status" title="Status" sortable="true" />
								<display:column title="Action">
								<a onclick="editEvent('${row.id}')" title="EDIT"></i> <i class="fa fa-edit" style="color:blue;"></i></a>&nbsp;&nbsp;&nbsp;
								<a onclick="deleteEvent('${row.id}')" title="DELETE"></i> <i class="fa fa-trash" style="color:red;"></i></a>
								</display:column>
								
								
								     		   
						 	 <display:setProperty name="export.csv.filename" value="<%=fname1%>" />
							 <display:setProperty name="export.excel.filename" value="<%=fname2%>" />
							 <display:setProperty name="export.xml.filename" value="<%=fname3%>" /> 
						</display:table> --%>
							</div>
						</div>
						<a  id="goTop"><i class="fa fa-eject"></i></a>	
				 </div>
			</section>	
			<%@include file="Footer.jsp"%>  		
	</div>	
</div>
		 
		
</body>
</html>