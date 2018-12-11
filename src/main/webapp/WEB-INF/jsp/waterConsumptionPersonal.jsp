<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>

<jsp:include page="js.jsp" flush="true" />
<style type="text/css">
#select {
	float: right;
	margin-top: -36px;
	margin-right: 5%;
}

.myDiv {
	border: 1px solid black;
	overflow: scroll;
	overflow-x: hidden;
	font-size: 12px;
	float: right;
	margin-right: 5%;
}

.myDiv a {
	text-decoration: none;
	display: block;
	height: 18px;
	line-height: 18px;
	text-indent: 4px;
	text-align: left;
}

.myDiv a:link, .myDiv a:visited {
	color: #2A1B00;
	background-color: #FFF0D9;
}

.myDiv a:hover, .myDiv a:active {
	background-color: #C2C660;
}

#search{
	margin-left: 180px;
}
.zindex{
	z-index:999;
}
</style>
<script src="<%=path %>/js/echarts.min.js"></script>
<script type="text/javascript">
	$(function() {
        var toDay = new Date();
        var toYear = toDay.getFullYear(); //获取完整的年份(4位,1970-????)
        var yearHtml = "";
        $.each(JSON.parse('${selectTime}'), function (key, value) {
            if(key == toYear){
                yearHtml += "<option value='"+key+"'  selected>"+key+"</option>";
            }else{
                yearHtml += "<option value='"+key+"'  >"+key+"</option>";
            }
        })
        $("#year").html(yearHtml);

		$("#search").click(function (){
			if($("#cid").val() == null  ||  $("#cid").val() == ""){
				bootbox.alert("请正确选择用户！");
				return;
			}
			$("#uNameDiv").removeClass('zindex');
			var year = $("#year").val();
			var type = $("#type").val();
			var cid = $("#cid").val();
			$.ajax({
				url:"createEchartsByUname.action",
				type:"get",
				data:{year:year,type:type,cid:cid},
				dataType:"json",
				success:function(data){
					if(type ==1){
						createChartByYear(data);
					}else{
						createChartByMonth(data);
					}
				}
			});
		})

		//ajax输入框
		$("#uname").keyup(function(){
			showDiv();
			$("#uNameDiv").addClass('zindex');
			$("#cid").val("");
			$.ajax({
				url:"ajax.action",
				type:"post",
				data:{name:$("#uname").val()},
				dataType:"json",
				success:function(data){
					if($("#uname").val()==""){
						hideDiv();
						return;
					}
					if(data.length == 0 ){
						hideDiv();
						return;
					}
					$("#sel").html("");
					jQuery.each(data,function(key,value){
						var y='<div><a href="javascript:;" onclick="getValue('+value.cid+',\''+value.cname+'\');">'+value.cid+':'+value.cname+'</a></div>'
						$("#sel").append(y);
					})
				}
			});
		})
	});
	function createChartByYear(data) {
		var myChart = echarts.init(document.getElementById('show'));
		var option = {
			title : {
				text :  $("#cname").val()+"各月份用水量与水费统计",
				//subtext:'在此测试',
				x : 'center',
				y : 'top',
				textAlign : 'left',
				textStyle : {
					color : '#333333',
					fontWeight : 'bold',
					fontSize : 18
				}
			},
            color:["#50bfff","#13ce66"],
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'cross',
					crossStyle : {
						color : '#999'
					}
				},
                formatter: function (params) {
                    //x轴名称
                    var name = params[0].name
				    var returns = name +"<br />";
					returns += params[0].marker+"用水量: "+params[0].value+" 吨";
					returns += "<br />";
					returns += params[1].marker+"水费: "+params[1].value+" 元";
                    return returns
                }
            },
			toolbox : {
				feature : {
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'line', 'bar' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			legend : {
				show:true,
		    	left: 'left',
	        	data:['用水量','水费']
			},
			xAxis : [ {
				type : 'category',
				data : [ '1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月','10月', '11月', '12月' ],
				axisPointer : {
					type : 'shadow'
				}
			} ],
			yAxis : [ {
				type : 'value',
				name : '用水量',
				min : 0,
				axisLabel : {
					formatter : '{value} 吨'
				}
			}, {
				type : 'value',
				name : '水费',
				min : 0,
				axisLabel : {
					formatter : '{value} 元'
				}
			} ],
			series : [ {
				name : '用水量',
				type : 'bar',
				data : data.listamount
			}, {
				name : '水费',
				type : 'line',
				yAxisIndex : 1,
				data : data.listcost
			} ]
		};
		myChart.setOption(option);
	}

	function createChartByMonth(data) {
		var myChart = echarts.init(document.getElementById('show'));
		var option = {
			title : {
				text :  $("#cname").val()+"各季度用水量与水费统计",
				//subtext:'在此测试',
				x : 'center',
				y : 'top',
				textAlign : 'left',
				textStyle : {
					color : '#333333',
					fontWeight : 'bold',
					fontSize : 18
				}
			},
            color:["#50bfff","#13ce66"],
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'cross',
					crossStyle : {
						color : '#999'
					}
				},
                formatter: function (params) {
                    //x轴名称
                    var name = params[0].name
                    var returns = name +"<br />";
                    returns += params[0].marker+"用水量: "+params[0].value+" 吨";
                    returns += "<br />";
                    returns += params[1].marker+"水费: "+params[1].value+" 元";
                    return returns
                }
			},
			toolbox : {
				feature : {
					dataView : {
						show : true,
						readOnly : false
					},
					magicType : {
						show : true,
						type : [ 'line', 'bar' ]
					},
					restore : {
						show : true
					},
					saveAsImage : {
						show : true
					}
				}
			},
			legend : {
				show:true,
		    	left: 'left',
	        	data:['用水量','水费']
			},
			xAxis : [ {
				type : 'category',
				data : [ '第一季度', '第二季度', '第三季度', '第四季度' ],
				axisPointer : {
					type : 'shadow'
				}
			} ],
			yAxis : [ {
				type : 'value',
				name : '用水量',
				min : 0,
				axisLabel : {
					formatter : '{value} 吨'
				}
			}, {
				type : 'value',
				name : '水费',
				min : 0,
				axisLabel : {
					formatter : '{value} 元'
				}
			} ],
			series : [ {
				name : '用水量',
				type : 'bar',
				data : data.listamount
			}, {
				name : '水费',
				type : 'line',
				yAxisIndex : 1,
				data : data.listcost
			} ]
		};
		myChart.setOption(option);
	}
	function showDiv() {
		var div = document.getElementById('sel');
		div.style.visibility = "visible";
	}
	function hideDiv() {
		var div = document.getElementById('sel');
		div.style.visibility = "hidden";
	}
	function getValue(cid,cname) {
		$("#uname").val(cid+":"+cname);
		$("#cid").val(cid);
		$("#cname").val(cname);
		hideDiv();
	}
	//点击空白处隐藏ajax下拉选项框
	 $(document).click(function(e){
		 	var divTop = $('#uNameDiv');   // 设置目标区域
		 	if(!divTop.is(e.target) && divTop.has(e.target).length === 0){
		 		hideDiv();
		  }
	})
</script>
</head>

<body >

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
			<li><a href="#">相关数据报表</a></li>
			<li><a href="#">个人用水量&水费统计</a></li>
		</ul>
	</div>
	
	<div style="margin: 10px 20px;">
		<a class="colorF">年份：</a>
		<select id="year" style="width: 70px; height: 32px;" >

		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="colorF">类型：</a>
		<select id="type" style="width: 100px; height: 32px;">
			<option value="1"  selected>全年统计</option>
			<option value="2" >各季度统计</option>
		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<div style="Float: left; position: absolute; margin-left: 180px;margin-top: 2px" id = "uNameDiv" class="zindex">
			<div id="select">
				<input type='text' id="uname" class="myTxt" placeholder="用户名" style="width: 150px; height: 30px;" onclick="javascript:this.value='' " />
				<input type='hidden' id="cid"  />
				<input type='hidden' id="cname"  />
			</div>
			<div id='sel' class='myDiv'
				style="left: 10px; top: 40px; width: 150px; max-height: 350px; visibility: hidden;">
			</div>
		</div>
		<input type="button" class="btn btn-primary" value="查询"  id="search" />
	</div>


	<%--<div id="show" style="width: 1000px; height: 500px;"></div>--%>
	<div id="show" style="margin: 20px 20px;width: 94%; height: 600px;"></div>

</body>
</html>
