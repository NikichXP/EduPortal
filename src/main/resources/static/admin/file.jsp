<%--<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>--%>
<%--<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>--%>

<%--<%--%>
	<%--BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();--%>
<%--%>--%>

<html>
<head>
<title>Upload Test</title>
</head>
<body>
	<%
		String order = request.getParameter("order");
		String token = request.getParameter("token");
		if (token == null) {
			for (Cookie c : request.getCookies()) {
				if (c.getName().equals("sesToken")) {
					token = c.getValue();
				}
			}
		}
		String user = request.getParameter("user");
		String product = request.getParameter("product");
	%>
	<form
		action="<%=blobstoreService.createUploadUrl("/FileProcessorServlet")%>"
		method="post" enctype="multipart/form-data">
		<input type="file" name="myFile">
		<input type="hidden" name="token" value="<%=token%>">
		<%
			if (order != null) {
				out.println("	<input type=\"hidden\" name=\"orderid\" value=\"" + order + "\">");
			}
			if (user != null) {
				out.println("	<input type=\"hidden\" name=\"userid\" value=\"" + user + "\">");
			}
			if (product != null) {
				out.println("	<input type=\"hidden\" name=\"productid\" value=\"" + product + "\">");
			}
		%>
		<input type="submit" value="Submit">
	</form>
</body>
</html>