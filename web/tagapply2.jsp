<%@ page language="java" contentType="text/html; charset=utf-8"%>
<html>
  <head>
    <script type="text/javascript">
	var xmlHttp,token;
	function xml()
	{
		if(window.ActiveXObject)
		{
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		}
		else if(window.XMLHttpRequest)
		{
			xmlHttp = new XMLHttpRequest();
		}
	}
	
	
	function login()
	{
		var url = "loginapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = handleLStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xmlHttp.send("json={\"Username\":'testca-01', 'Password':'123456'}");
	}
	function handleLStateChange()
	{
		if(xmlHttp.readyState == 4)
		{
			{
				parseLResults();
			}
		}
	}

	function parseLResults()
	{
		token = xmlHttp.getResponseHeader("CA-Token");
	}
	
	

	function sendInfo()
	{
		var url = "tagapply2api.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = handleStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xmlHttp.setRequestHeader("CA-Token", token);
		xmlHttp.send("json={'TypeID':69373987486, 'ProductCount':'200', 'DeviceID':'1'}");
	}
	function handleStateChange()
	{
		if(xmlHttp.readyState == 4)
		{
			//if(xmlHttp.status == 200)
			{
				parseResults();
			}
		}
	}

	function parseResults()
	{
		//var result = eval('(' + xmlHttp.responseText + ')');

		//if (result == "Y")
		{
			document.getElementById("myDiv").innerHTML = xmlHttp.responseText;
		}
		//else if (result == "N")
		{
		}
	} 
</script>
  </head>
  <body>
    <button type="button" onclick="xml()">xml</button>
    <button type="button" onclick="login()">Login</button>
  	<button type="button" onclick="sendInfo()">Apply</button>
	<div id="myDiv"></div>
  </body>
</html>