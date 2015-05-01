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

	function add()
	{
		var url = "appformataddapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = addStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xmlHttp.send("json={'Authid':-1,'Vendorid':-1,'Typeid':-1,'Flag':0,'Formatinfo':'ProductName,StatusDesc,Count,LabelProof,OriginCountry,OriginRegion,ProductDate,ShelfLife,OutDistributors,NetContent,InDistributors,InAgentor,importer,Vendor,ImportGate,InspectCode,InspectInCode,IntoDate,Urgent,CNProofSample,InspectReport'}");
	}
    function merge()
    {
        var url = "appformatmergeapi.action";
        xmlHttp.open("post", url, true);
        xmlHttp.onreadystatechange = addStateChange;
        xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xmlHttp.send("json={'Authid':-1,'Vendorid':-1,'Typeid':-1,'Flag':0,'Formatinfo':'ProductName,StatusDesc,Count,LabelProof,OriginCountry,OriginRegion,ProductDate,ShelfLife,OutDistributors,NetContent,InDistributors,InAgentor,importer,Vendor,ImportGate,InspectCode,InspectInCode,IntoDate,Urgent,CNProofSample,InspectReport'}");
    }
    function del()
    {
        var url = "appformatdelapi.action";
        xmlHttp.open("post", url, true);
        xmlHttp.onreadystatechange = addStateChange;
        xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xmlHttp.send("json={'Id':1}");
    }
	function addStateChange()
	{
		if(xmlHttp.readyState == 4)
		{
			{
				parseAddResults();
			}
		}
	}

	function parseAddResults()
	{
		document.getElementById("myDiv").innerHTML = xmlHttp.responseText;
	}
	
	function modify()
	{
		var url = "mailmodifyapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = modifyStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xmlHttp.send("json={'IDS':[{'ID':2,'STATUS':0},{'ID':3,'STATUS':0}]}");
	}
	
	function modifyStateChange()
	{
		if(xmlHttp.readyState == 4)
		{
			parseModifyResults();
		}
	}

	function parseModifyResults()
	{
		document.getElementById("myDiv").innerHTML = xmlHttp.responseText;
	}
	
	
	function query(flag)
	{
		var url = "mailqueryapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = modifyStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        if(flag==0){
            xmlHttp.send("json={}");
        }
        if(flag==1){
            xmlHttp.send("json={'AUTHID':0}");
        }
        if(flag==2){
            xmlHttp.send("json={'AUTHID':0,'VENDORID':1058554}");
        }

	}


	function modifyStateChange()
	{
		if(xmlHttp.readyState == 4)
		{
			parseModifyResults();
		}
	}

	function parseModifyResults()
	{
		document.getElementById("myDiv").innerHTML = xmlHttp.responseText;
	}
	
	
</script>
  </head>
  <body>
  	<button type="button" id="b1" onclick="createXMLHttpRequest()">Create XML</button>
  	<!-- <button type="button" id="b2" onclick="add()">add </button> -->
  	<button type="button" id="b3" onclick="modify()">modify </button>
  	<button type="button" id="b4" onclick="query(1)">query authid</button>
    <button type="button" id="b41" onclick="query(0)">query all</button>
    <button type="button" id="b42" onclick="query(2)">query authid+vendorid</button>
    <!--  <button type="button" id="b5" onclick="merge()">merge</button>
 <button type="button" id="b6" onclick="del()">delete </button> -->
 <div id="myDiv"></div>
</body>
</html>