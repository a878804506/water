<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- 引用的js -->
<script language="JavaScript" src="<%=path %>/js/JQuery.js"></script>
<script language="JavaScript" src="<%=path %>/js/bootstrap.min.js"></script>
<script language="JavaScript" src="<%=path %>/js/bootbox.min.js"></script>
<script language="JavaScript" src="<%=path %>/js/jquery.ztree.core.js"></script>
<script language="JavaScript" src="<%=path %>/js/jquery.ztree.excheck.js"></script>
<script language="JavaScript" src="<%=path %>/js/jquery.ztree.exedit.js"></script>

<link href="<%=path %>/css/bootstrap.css" rel="stylesheet"/>
<link href="<%=path %>/css/style.css" rel="stylesheet" type="text/css" />
