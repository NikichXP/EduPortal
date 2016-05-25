<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>


<html>
    <head>
        <title>Upload Test</title>
    </head>
    <body>
        <form action="<%= blobstoreService.createUploadUrl("/FileProcessorServlet") %>" method="post" enctype="multipart/form-data">
            <input type="file" name="myFile">
            <input type="hidden" name="orderid" value="<%= request.getParameter("order") %>">
            <input type="submit" value="Submit">
        </form>
    </body>
</html>