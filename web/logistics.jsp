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
		var url = "logisticsaddapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = addStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        /*
		xmlHttp.send("json={'TypeID':69373332125,'InspectID':123456, 'OutDate':'2014-04-22','OutCount':100," + 
						"'PassGateID':'G1001','ProductStartID':1,'ProductEndID':10,'ReceiveCompany':'gt.cmp'," + 
						"'ReceiveAddress':'gt.addr','ReceivePhone':'gt.phone'}");
						*/
        xmlHttp.send("json={'PassGateID':'-1111','PassInspectID':'-1111', 'InspectID':-1111, "+
       " 'InspectCode':'-1111','TypeID':69373266589,'Category':'1','VendorID':-1111,"+
        "    'OutDate':'2014-05-22','OutCount':1900,'ProductStartID':1,'ProductEndID':1900,  "+
         "   'ReceiveCompany':'≤‚ ‘','ReceiveAddress':'≤‚ ‘µÿ÷∑', 'ReceivePhone':'192222', "+
          "  'ProductCountUnit':'1'}");
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
	
	function modify(flag)
	{
		var url = "logisticsmodifyapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = modifyStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        if(flag==1){
            xmlHttp.send("json={'LogisticsID':18,'ReceiveCompany':'≤‚ ‘','ReceiveAddress':'≤‚ ‘µÿ÷∑', 'ReceivePhone':'13212345123'}");
        }
        if(flag==2){
            xmlHttp.send("json={'LogisticsID':18,'ReceiveCompany':'≤‚ ‘','ReceiveAddress':'≤‚ ‘µÿ÷∑', 'ReceivePhone':'13212345000','OutDate':'20140620','ProductStartID':10,'ProductEndID':21,'PassInspectID':'','PassGateID':'pwss123'}");
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
	
	
	function query()
	{
		var url = "logisticsqueryapi.action";
		xmlHttp.open("post", url, true);
		xmlHttp.onreadystatechange = modifyStateChange;
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		/*
        xmlHttp.send("json={'TypeID':653855389,'PassGateID':'G1001'," +
						"'InspectID':123456,'OutStartDate':'2013-01-01','OutEndDate':'2014-12-12'}");
         */
        xmlHttp.send("json={'PassGateID':'-1111','VendorID':-1111," +
                "'TypeID':69373266589,'Category':'1'}");
	}
    function queryvtype()
    {
        var url = "vptypequerybyinspectapi.action";
        xmlHttp.open("post", url, true);
        xmlHttp.onreadystatechange = modifyStateChange;
        xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        xmlHttp.send("json={'InspectCode':'111WWEe12121','VendorID':1058567}" );
    }
    function querybyinsp()
    {
        var url = "logisticsquerybyinspectapi.action";
        xmlHttp.open("post", url, true);
        xmlHttp.onreadystatechange = modifyStateChange;
        xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        /*
        xmlHttp.send("json={'InspectCode':'77777777','VendorID':1058555," +
                "'TypeID':69373463197,'Category':'1','PassGateID':'001'}");
         */
        xmlHttp.send("json={'InspectCode':'77777777','VendorID':1058555," +
                "'TypeID':69373463197,'Category':'1'}");
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
  	<button type="button" id="b2" onclick="add()">add logistics</button>
  	<button type="button" id="b3" onclick="modify(1)">modify logistics Contact Information</button>
    <button type="button" id="b31" onclick="modify(2)">modify logistics all</button>
  	<button type="button" id="b4" onclick="query()">query logistics</button>
    <button type="button" id="b5" onclick="querybyinsp()">query logistics from inspect</button>
      <button type="button" id="b6" onclick="queryvtype()">query vtype from inspect</button>
	<div id="myDiv"></div>
  </body>
</html>