<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Your friend May be in Danger! - Protect Me!</title>
<style type="text/css">
#RedHeader {
	font-family: Georgia, "Times New Roman", Times, serif;
	color: #F00;
	font-size: x-large;
	font-weight: bold;
	text-transform: capitalize;
}

body {
	background-color: #E3E8EC;
}

table {
	border-radius: 10px;
	-moz-border-radius: 10px;
	-webkit-border-radius: 10px;
}

#Status_Card_Title {
	font-family: "Courier New", Courier, monospace;
	font-size: 18px;
	color: #009;
	text-decoration: underline;
	text-align: center;
	font-weight: bold;
}

#Status_card_element {
	text-align: center;
}

#Content {
	font-family: Georgia, "Times New Roman", Times, serif;
	font-size: medium;
}
</style>
</head>

<body>
	<table width="800" border="1" align="center">
		<tr>
			<td align="center" valign="middle"><img src="/static/images/Banner.jpg" width="800" align="middle" /></td>
		</tr>
		<tr>
			<td align="center" valign="middle">
				<p>Your gmail: ${userId}</p>
			</td>
		</tr>
		<tr>
			<td align="center" valign="middle">
				<p>Alert Notification Email List</p>
			</td>
		</tr>
			<c:forEach items="${emailList}" var="email">
				<tr>
					<td align="left">
						<p>${email} &nbsp;&nbsp;&nbsp;&nbsp;<a href="<c:url value="/view/emailConfigure/deleteEmail?userId=${userId}&email=${email}"/>">Delete</a></p>
					</td>
				</tr>
			</c:forEach>
		<tr>
			<td align="center" valign="middle">
				<p>Add notification email</p>
				<form action="/view/emailConfigure/addEmail" method="post" align="center">
					<input type="hidden" name="userId" value="${userId}" />
					<input type="text" name="email" id="email"/>
					<input type="submit"/> 
				</form>
			</td>
		</tr>

	</table>
	
</body>
</html>
