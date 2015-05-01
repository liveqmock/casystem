<%@ page language="java" contentType="text/html; charset=GBK"%>
<html>
  <head>
    <script type="text/javascript">
	var xmlHttp;
	function createXMLHttpRequest()
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

	function sendInfo()
	{
		createXMLHttpRequest();
		var url = "authaddapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = handleStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xmlHttp.send("json={'AuthName':'≤‚ ‘æ÷', 'AuthAddress':'∫º÷›', 'AuthURL':'www.tauth.gov°£cn', 'AuthPhone':'010-58619900', 'AuthEmail':'', 'AuthStatus':1, 'FatherID':1}");
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
  	<button type="button" onclick="sendInfo()">Send</button>
	<div id="myDiv"></div>
  </body>
</html>