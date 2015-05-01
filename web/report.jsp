<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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
		var url = "rptapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = modifyStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        if(flag==1){
            xmlHttp.send("json={'flag':1,'AuthID':0,'BeginDate':'2014-05-01','EndDate':'2014-06-01'}");
        }
        if(flag==2){
            //xmlHttp.send("json={'flag':2,'AuthID':0,'VendorID':1058554,'BeginDate':'2014-05-01','EndDate':'2014-06-01'}");
            xmlHttp.send("json={'flag':2,'AuthID':0,'BeginDate':'2014-05-01','EndDate':'2014-06-01'}");
        }
        if(flag==3){
            //xmlHttp.send("json={'flag':3,'VendorID':1058554,'BeginDate':'2014-05-01','EndDate':'2014-06-01'}");
            xmlHttp.send("json={'flag':3,'BeginDate':'2014-05-01','EndDate':'2014-06-01'}");
        }
        if(flag==4){
            //xmlHttp.send("json={'flag':4,'VendorID':1058554,'BeginDate':'2014-05-01','EndDate':'2014-06-01'}");
            xmlHttp.send("json={'flag':4,'BeginDate':'2014-05-01','EndDate':'2014-06-01'}");
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
  	<!-- <button type="button" id="b2" onclick="add()">add </button>
  	<button type="button" id="b3" onclick="modify()">modify </button>      -->
  	<button type="button" id="b4" onclick="query(1)">query producttype report</button>
    <button type="button" id="b41" onclick="query(2)">query inspect report</button>
    <button type="button" id="b42" onclick="query(3)">query logistic report order by inspectcode</button>
    <button type="button" id="b43" onclick="query(4)">query logistic report order by inspectid</button>
    <!--  <button type="button" id="b5" onclick="merge()">merge</button>
 <button type="button" id="b6" onclick="del()">delete </button> -->
 <div id="myDiv"></div>
</body>
</html>