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
		var url = "tagdealqueryapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = handleStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		//xmlHttp.send("Deals={'TypeID':86879759008}");
		//xmlHttp.send("Deals={'DateStart':'28 Apr 2013 09:11:10 GMT'}");
		xmlHttp.send("json={'ProductCount':10}");
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