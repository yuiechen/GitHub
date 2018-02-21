<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// 	String boId = request.getParameter("userName");
// 	if(!"12".equalsIgnoreCase(boId)){
// 		response.sendRedirect("./error.jsp?error=You do not have permission to access this application.");
// 	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport"
	content="width=device-width,height=device-height,initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信应用</title>
<link rel="stylesheet" href="./css/bootstrap.min.css">
<script src="../js/jquery-3.1.0.min.js"></script>
<script src="../js/bootstrap.min.js"></script>

</head>
<body style="margin: auto; height: 100%; width: 100%;">

	<button onClick="testAjax()">test</button>

	<!-- 	<ul id="appTab" class="nav nav-tabs"> -->
	<!-- 		<li class="active" style="font-weight: bold;" onClick="switchTab('wechatRegisterTab',['wechatConfigTab'])"> -->
	<!-- 			<a id="wechatRegisterTab" href="#wechatRegister" data-toggle="tab" style="background-color: #d0def0;">微信通讯录/归户设置</a> -->
	<!-- 		</li> -->
	<!-- 		<li style="font-weight: bold;" onClick="switchTab('wechatConfigTab',['wechatRegisterTab'])"> -->
	<!-- 			<a id="wechatConfigTab" href="#wechatConfig" data-toggle="tab" >微信环境参数设置</a> -->
	<!-- 		</li> -->
	<!-- 	</ul> -->
	<!-- 	<div id="appTabContent" class="tab-content"> -->
	<!-- 		<div id="wechatRegister" class="tab-pane fade in active"> -->
	<%-- 			<iframe id="wechatRegisterFrame" src="./WechatRegister.jsp?boId=<%=boId%>" style="position: absolute; height: 93%; width: 100%; border: none;"></iframe> --%>
	<!-- 		</div> -->
	<!-- 		<div id="wechatConfig" class="tab-pane fade"> -->
	<%-- 			<iframe id="wechatConfigFrame" src="./WechatConfig.jsp?boId=<%=boId%>" style="position: absolute; height: 93%; width: 100%; border: none;"></iframe> --%>
	<!-- 		</div> -->
	<!-- 	</div> -->


</body>

<script>

function switchTab(targetTab, disabledTab){
	document.getElementById(targetTab).style.backgroundColor = "#d0def0";
	for(var i in disabledTab){
		document.getElementById(disabledTab[i]).style.backgroundColor = "";
	}
}

function testAjax(){
	var sendData = {
		
	};
	$.ajax({
		//url : "../loadObjToDb.do",
		url : "../loadMainSubObjToDb.do",
		type : "post",
		data: JSON.stringify(sendData),
		contentType: "application/json; charset=utf-8",
		dataType : "json",
		cache : false,
		async: false,
		success : function(response) {
			alert("success");
		},
		error: function(xhr, status, error){
			alert("Ajax Error Status:"+xhr.statu+" and Error Reponse:"+xhr.responseText+" and Error Message:"+error);
		},
	});
}



</script>

</html>