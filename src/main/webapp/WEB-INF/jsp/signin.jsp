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
				<p>Welcome to Glass Guardian Configuration Page</p>
			</td>
		</tr>
		<tr>
			<td align="center" valign="middle">
				<p>Enter your gmail</p>
				<form action="/view/emailConfigure/signIn" method="post">
					<input type="text" name="userId" id="userId"/>
					<input type="submit"/> 
				</form>
			</td>
		</tr>

	</table>
	
</body>
</html>

