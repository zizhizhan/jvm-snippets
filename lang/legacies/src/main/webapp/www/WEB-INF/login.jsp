<%@ page contentType="text/html; charset=gb2312" language="java" errorPage="" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html xmlns="http://www.w3.org/1999/xhtml">  
<head>  
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />  
<title>JSP Login Page</title>  
</head>  
  
<body>  
<table width="400" border="1" align="center" style="margin-top:50px;">  
<tr><th colspan="2">Login Form</th></tr>
<form action="/mvc/login?v=jsp" method="post" enctype="application/x-www-form-urlencoded">  
  <tr>  
    <td>User Name:</td>  
    <td><input name="username" type="text" /></td>  
  </tr>  
  <tr>  
    <td>Password:</td>  
    <td><input name="password" type="password" /></td>  
  </tr>  
  <tr>  
    <td colspan="2"><button type="submit">Submit</button></td>      
  </tr>    
</form>  
  
</table>  
</body>  
</html>  