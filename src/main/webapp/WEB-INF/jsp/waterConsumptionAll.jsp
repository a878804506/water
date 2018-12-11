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
		$("#month").html(yearHtml);

		$("#search").click(function (){
			var year = $("#month").val();
			var type = $("#type").val();
			$.ajax({
				url:"createEchartsByAll.action",
				type:"get",
				data:{year:year,type:type},
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
        //进入该页面时自动点击一次查询按钮；
        $("#search").click();
	});
	function createChartByYear(data) {
		var myChart = echarts.init(document.getElementById('show'));
		var option = {
			title : {
				text : "全镇各月份用水量与水费统计",
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
				text : "全镇各季度用水量与水费统计",
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
</script>
</head>

<body >

	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
			<li><a href="#">相关数据报表</a></li>
			<li><a href="#">全镇用水量&水费统计</a></li>
		</ul>
	</div>
	
	<div style="margin: 10px 20px;">
		<a class="colorF">年份：</a>
		<select id="month" style="width: 70px; height: 32px;" name="month">

		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="colorF">类型：</a>
		<select id="type" style="width: 100px; height: 32px;" name="month">
			<option value="1"  selected>全年统计</option>
			<option value="2" >各季度统计</option>
		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="btn btn-primary" value="查询"  id="search" />
	</div>

	<div id="show" style="margin: 20px 20px;width: 94%; height: 600px;"></div>

</body>
</html>
