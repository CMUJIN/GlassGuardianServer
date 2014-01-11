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
			<td align="center" valign="middle"><p id="RedHeader">Your
					friend ${email} May be in Danger !</p>
				<p>Google Glass had detected a abnormal shock and
					the user had failed to respon to the security question.</p>
				<p>To ensure your friend is alright, please contact him/her immediately</p>
				
				<p>Here are his last known locations and surrounding data.</p></td>
		</tr>
		<tr>
			<td><table width="800" border="1">
					<c:forEach items="${trackDataList}" var="trackData">
						<tr>
							<td>
								<p id="Status_Card_Title">${trackData.creationDate}</p> <span
								id="Status_card_element"><img src="${trackData.imagePath}" height="270" /></small>
									<iframe width="270" height="270" frameborder="0" scrolling="no"
										marginheight="0" marginwidth="0"
										src="https://maps.google.com/maps?f=q&amp;source=s_q&amp;hl=en&amp;geocode=&amp;sspn=0.156597,0.292511&amp;ie=UTF8&amp;q=${trackData.latitude},${trackData.longtitude}&amp;ll=${trackData.latitude},${trackData.longtitude}&amp;spn=0.001221,0.002285&amp;t=m&amp;z=14&amp;output=embed"></iframe>
							</span><br /> <br /> </a> <span id="Status_card_element">
							</span>
							</td>
						</tr>
					</c:forEach>

				</table></td>
		</tr>
	</table>
</body>
</html>
