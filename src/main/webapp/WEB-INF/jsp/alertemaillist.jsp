<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Glass Guardian Setting</title>
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
				<p>Hello, "${userId}"</p>
				<br><br><br>
				<h1>Alert Notification Email List</h1>
				<p>Set the email list to receive the alert notification email when Glass Guardian detect suspious behavior </p>
				<br>
				<p>Email List:</p>
				<c:forEach items="${emailList}" var="email">
					<p>${email} &nbsp;&nbsp;&nbsp;&nbsp;<a href="<c:url value="/view/emailConfigure/deleteEmail?userId=${userId}&email=${email}"/>">Delete</a></p>
				</c:forEach>
				<br>
				<form class="form-search" action="/view/emailConfigure/addEmail" method="post">
					<input type="hidden" name="userId" value="${userId}" />
					<div class="input-append">
						<input type="text" name="email" class="m-wrap" placeholder="Your Family And Friends' Email">
						<button type="submit" class="btn blue btn-subscribe"><span>Add to List</span> <i class="m-icon-swapright m-icon-white"></i></button>
					</div>
				</form>

			</div>
		</div>
		<!--/end row-fluid-->
		<div class="row-fluid">
			<div class="span12 coming-soon-footer">2013 &copy; Spark255</div>
		</div>
	</div>

</body>
</html>
