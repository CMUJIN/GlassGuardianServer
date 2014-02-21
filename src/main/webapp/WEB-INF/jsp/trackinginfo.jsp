<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Glass Guardian Tracking</title>
<!-- BEGIN GLOBAL MANDATORY STYLES -->
	<link href="/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
	<link href="/assets/plugins/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css"/>
	<link href="/assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
	<link href="/assets/css/style-metro.css" rel="stylesheet" type="text/css"/>
	<link href="/assets/css/style.css" rel="stylesheet" type="text/css"/>
	<link href="/assets/css/style-responsive.css" rel="stylesheet" type="text/css"/>
	<link href="/assets/css/themes/default.css" rel="stylesheet" type="text/css" id="style_color"/>
	<link href="/assets/plugins/uniform/css/uniform.default.css" rel="stylesheet" type="text/css"/>
	<!-- END GLOBAL MANDATORY STYLES -->
	<!-- BEGIN PAGE LEVEL STYLES -->
	<link href="/assets/css/pages/coming-soon.css" rel="stylesheet" type="text/css"/>
	<!-- END PAGE LEVEL STYLES -->
	<link rel="shortcut icon" href="favicon.ico" />
</head>

<body>
	<div class="container">
		<div class="row-fluid">
			<div class="span12 coming-soon-header">
				<a class="brand" href="/index.html"> <img src="/assets/img/logo-big.png" alt="logo" /></a>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span6 coming-soon-content">
				<table width="800" border="1" align="center">
					<tr>
						<td align="center" valign="middle"><p>Your friend ${email}
								May be in Danger !</p>
							<p>Google Glass had detected a abnormal shock and the user had
								failed to respon to the security question.</p>
							<p>To ensure your friend is alright, please contact him/her
								immediately</p>
	
							<p>Here are his/her last known locations and surrounding data.</p></td>
					</tr>
					<c:forEach items="${trackDataList}" var="trackData">
						<tr>
							<td>
							<div width="100%"><p>${trackData.creationDate}</p></div>
							<div width="100%">	
								<div height="270" display:inline float:left><img src="${trackData.imagePath}" height="270" /></div>
								<div height="270" display:inline float:left><iframe height="270" frameborder="0" scrolling="no" marginheight="0" marginwidth="0" src="https://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;sspn=0.156597,0.292511&amp;ie=UTF8&amp;q=${trackData.latitude},${trackData.longtitude}&amp;ll=${trackData.latitude},${trackData.longtitude}&amp;spn=0.001221,0.002285&amp;t=m&amp;z=14&amp;output=embed"></iframe></div>
							</div>
							</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</div>
</body>
</html>
