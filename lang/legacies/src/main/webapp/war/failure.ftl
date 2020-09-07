<html>
<head>
<style type="text/css">
#loadingBar{
	background:#ff3300;
	width:120px;
	height:25px;	
	position:absolute;
	top:0px;
	z-index:100;
	display:none;
}
</style>
<script type="text/javascript">
var xmlhttp = function(){
	var _msxml_progid = ['Microsoft.XMLHTTP', 'MSXML2.XMLHTTP.3.0', 'MSXML2.XMLHTTP'], http;  
	try{  
		http = new XMLHttpRequest();  
	}catch(e){  
		for(var i=0; i<this._msxml_progid.length; ++i){  
			try {                     
				http = new ActiveXObject(this._msxml_progid[i]);  
				break;  
			} catch(ignore){}  
		}   
	}  
	return http;          
}();

var ajax = function(url, method, callback){
    xmlhttp.open(method, url, true);  
    xmlhttp.onreadystatechange = function(){  
		switch(xmlhttp.readyState){
			case 0:  
 				loadingBar.style.display = 'block';				
  	            break;  
			case 4:
				var headers = xmlhttp.getAllResponseHeaders();  
				var str = headers;  
				str += xmlhttp.status + " " + xmlhttp.statusText;
				
				if(xmlhttp.status == 200 || xmlhttp.status == 500){  
                     str += "\n" + xmlhttp.responseText;  
				}
				
				loadingBar.style.display = 'none';
				callback(str);
                break;                
         }         
     };  
     xmlhttp.send(null);  
}

function display(str){
	messageBox.value = str;	
}

</script>
</head>

<body>
Sorry, your username <b style="color:#ff3300">${request.getParameter('username')?default('Anonymous')}</b> not allow to access this page.

<p>
username: <input type="text" id="username" value="James" /><br />
method: 
<select id="method">
	<option value="GET">GET</option>
    <option value="POST">POST</option>
    <option value="PUT">PUT</option>
    <option value="DELETE">DELETE</option>
    <option value="HEAD">HEAD</option>
</select><br />
<button onclick="ajax('?username=' + username.value, method.value, display)">AJAX</button><br />
<textarea id="messageBox" cols="120" rows="30"></textarea>

</p>

<div id="loadingBar">Loading...</div>
</body>