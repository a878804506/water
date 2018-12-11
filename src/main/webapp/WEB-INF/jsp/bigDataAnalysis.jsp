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
<script language="JavaScript" src="<%=path %>/js/jquery.freezeheader.js"></script>
<script src="<%=path %>/js/echarts.min.js"></script>
<style>
	body,html{
		width: 100%;
		height: 100%;
		padding: 0;
		margin: 0;
	}
	#tabL,#tabR,#pieL,#pieR{
		float: left;
	}
	.tableCss{
		font-weight:800;
		font-size:18px
	}
	.imgtable th{
		text-indent:0px;
		text-align:center;
	}
	#hdScrollmyTableR::-webkit-scrollbar {
		 width: 8px;
		 height: 8px;
	 }
	/*正常情况下滑块的样式*/
	#hdScrollmyTableR::-webkit-scrollbar-thumb {
		background-color: rgba(30, 144, 255,.5);
		border-radius: 10px;
		-webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
	}
	/*鼠标悬浮在该类指向的控件上时滑块的样式*/
	#hdScrollmyTableR:hover::-webkit-scrollbar-thumb {
		background-color: rgba(30, 144, 255,.9);
		border-radius: 10px;
		-webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
	}
	/*鼠标悬浮在滑块上时滑块的样式*/
	#hdScrollmyTableR::-webkit-scrollbar-thumb:hover {
		background-color: rgba(30, 144, 255,.9);
		-webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
	}
	/*正常时候的主干部分*/
	#hdScrollmyTableR::-webkit-scrollbar-track {
		border-radius: 10px;
		-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0);
		background-color: white;
	}
	/*鼠标悬浮在滚动条上的主干部分*/
	#hdScrollmyTableR::-webkit-scrollbar-track:hover {
		-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.4);
		background-color: rgba(0,0,0,.01);
	}
	#hdScrollmyTableL::-webkit-scrollbar {
		width: 8px;
		height: 8px;
	}
	/*正常情况下滑块的样式*/
	#hdScrollmyTableL::-webkit-scrollbar-thumb {
		background-color: rgba(30, 144, 255,.5);
		border-radius: 10px;
		-webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
	}
	/*鼠标悬浮在该类指向的控件上时滑块的样式*/
	#hdScrollmyTableL:hover::-webkit-scrollbar-thumb {
		background-color: rgba(30, 144, 255,.9);
		border-radius: 10px;
		-webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
	}
	/*鼠标悬浮在滑块上时滑块的样式*/
	#hdScrollmyTableL::-webkit-scrollbar-thumb:hover {
		background-color: rgba(30, 144, 255,.9);
		-webkit-box-shadow: inset 1px 1px 0 rgba(0,0,0,.1);
	}
	/*正常时候的主干部分*/
	#hdScrollmyTableL::-webkit-scrollbar-track {
		border-radius: 10px;
		-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0);
		background-color: white;
	}
	/*鼠标悬浮在滚动条上的主干部分*/
	#hdScrollmyTableL::-webkit-scrollbar-track:hover {
		-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.4);
		background-color: rgba(0,0,0,.01);
	}
</style>

<script type="text/javascript">
	$(function() {
        /*var height = $(window).height()-100;
        var width = $(window).width();
        $("#tabL").css("height", height*0.555+"px");
        $("#tabL").css("width", width*0.5+"px");
        $("#tabR").css("height", height*0.555+"px");
        $("#tabR").css("width", width*0.495+"px");
        $("#pieL").css("height", height*0.442+"px");
        $("#pieL").css("width", width*0.5+"px");
        $("#pieR").css("height", height*0.442+"px");
        $("#pieR").css("width", width*0.495+"px");*/

        var mapYearMonth = new Map();  // 年份、月份 下拉框列表
        var toDay = new Date();
        var toYear = toDay.getFullYear(); //获取完整的年份(4位,1970-????)
        var toMonth = toDay.getMonth()+1; //获取当前月份(0-11,0代表1月)

        var selectTime = JSON.parse('${selectTime}');
        var yearHtml = "";
        var monthHtml = "";
        $.each(selectTime, function (key, value) {
            mapYearMonth.set(key,value);
            if(key == toYear){
                yearHtml += "<option value='"+key+"'  selected>"+key+"</option>";
                $.each(value,function(keyM,valueM){
                    if(valueM == toMonth){
                        monthHtml += "<option value='"+valueM+"'  selected>"+valueM+"</option>";
					}else{
                        monthHtml += "<option value='"+valueM+"' >"+valueM+"</option>";
					}
				})
			}else{
                yearHtml += "<option value='"+key+"'  >"+key+"</option>";
			}
		})
        $("#year").html(yearHtml);
        $("#month").html(monthHtml);

        //年份下拉框改变事件
        $("#year").change(function(){
            var tempMonth = "";
            $.each(mapYearMonth.get($("#year").val()),function(keyM,valueM){
                tempMonth += "<option value='"+valueM+"' >"+valueM+"</option>";
            })
            $("#month").html(tempMonth);
		})

        $("#search").click(function (){
            var year = $("#year").val();
            var month = $("#month").val();
            var top = $("#top").val();
			$.ajax({
				url:"bigDataAnalysis.action",
				type:"get",
				data:{year:year,month:month,top:top},
				dataType:"json",
				success:function(data){
					if(data.error != undefined){
                        bootbox.alert({
                            buttons: {
                                ok: {
                                    label: "确定",
                                    className: 'btn-primary'
                                }
                            },
                            message: data.error
                        });
					}else{
                        $(".yearFont").text($("#year").val());
                        $(".monthFont").text($("#month").val());
                        $(".topFont").text($("#top").val());
                        //左表格
                        var myTabL = "";
                        var count = 0;
                        $.each(data.wTopData, function (index, water) {
                            myTabL += "<tr><td>" +
                                ++count +
                                "</td> <td>" +
                                water.uid +
                                "</td> <td>" +
                                water.uname +
                                "</td> <td>" +
                                water.thisData +
                                "</td> <td>" +
                                water.lastData +
                                "</td> <td>";
                            if(water.link == undefined){
                                myTabL += '/';
                            }else{
                                myTabL += water.link;
                            }
                            myTabL += "</td></tr>";
                        });
                        $("#myTabL").html(myTabL);
                        $("#myTableL").freezeHeader({ 'height': '100%'});
                        //右表格
                        var myTabR = "";
                        count = 0;
                        $.each(data.mTopData, function (index, water) {
                            myTabR += "<tr><td>" +
                                ++count +
                                "</td> <td>" +
                                water.uid +
                                "</td> <td>" +
                                water.uname +
                                "</td> <td>" +
                                water.thisData +
                                "</td> <td>" +
                                water.lastData +
                                "</td> <td>" ;
                            if(water.link == undefined){
                                myTabR += '/';
                            }else{
                                myTabR += water.link;
                            }
                            myTabL += "</td></tr>";
                        });
                        $("#myTabR").html(myTabR);
                        $("#myTableR").freezeHeader({ 'height': '100%'});
                        //左饼图   基于准备好的dom，初始化echarts实例
                        var myChartL = echarts.init(document.getElementById('pieL'));
                        var option = {
                            title : {
                                text: year+'年'+month+'月前'+top+'位用户用水量占比',
                                x:'center'
                            },
                            tooltip : {
                                trigger: 'item',
                                formatter: "{b} : {c}吨 ({d}%)"
                            },
                            legend: {
                                orient: 'vertical',
                                left: 'left',
                                data: ['前'+top+'位用户用水量','剩下用户用水量']
                            },
                            series : [
                                {
                                    type: 'pie',
                                    radius : '55%',
                                    center: ['50%', '60%'],
                                    data:[
                                        {value:data.wPieData[0], name:'前'+top+'位用户用水量'},
                                        {value:data.wPieData[1]-data.wPieData[0], name:'剩下用户用水量'}
                                    ],
                                    itemStyle: {
                                        emphasis: {
                                            shadowBlur: 10,
                                            shadowOffsetX: 0,
                                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                                        }
                                    }
                                }
                            ]
                        };
                        // 使用刚指定的配置项和数据显示图表。
                        myChartL.setOption(option);
                        //右饼图
                        var myChartR = echarts.init(document.getElementById('pieR'));
                        var option = {
                            title : {
                                text: year+'年'+month+'月前'+top+'位用户水费占比',
                                x:'center'
                            },
                            tooltip : {
                                trigger: 'item',
                                formatter: "{b} : {c}吨 ({d}%)"
                            },
                            legend: {
                                orient: 'vertical',
                                left: 'left',
                                data: ['前'+top+'位用户水费','剩下用户水费']
                            },
                            series : [
                                {
                                    type: 'pie',
                                    radius : '55%',
                                    center: ['50%', '60%'],
                                    data:[
                                        {value:data.mPieData[0], name:'前'+top+'位用户水费'},
                                        {value:data.mPieData[1]-data.mPieData[0], name:'剩下用户水费'}
                                    ],
                                    itemStyle: {
                                        emphasis: {
                                            shadowBlur: 10,
                                            shadowOffsetX: 0,
                                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                                        }
                                    }
                                }
                            ]
                        };
                        // 使用刚指定的配置项和数据显示图表。
                        myChartR.setOption(option);
					}
				}
			});
		})
        //进入该页面时自动点击一次查询按钮；
        $("#search").click();
	});
</script>
</head>

<body >
	<div class="place">
		<span>位置：</span>
		<ul class="placeul">
			<li><a href="#">首页</a></li>
			<li><a href="#">相关数据报表</a></li>
			<li><a href="#">大数据分析</a></li>
		</ul>
	</div>
	
	<div style="margin: 10px 20px;">
		<a class="colorF">年份：</a>
		<select id="year" style="width: 70px; height: 32px;" name="year">

		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="colorF">月份：</a>
		<select id="month" style="width: 70px; height: 32px;" name="month">

		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<a class="colorF">Top：</a>
		<select id="top" style="width: 100px; height: 32px;" name="type">
			<option value="5"  >前五</option>
			<option value="10" >前十</option>
			<option value="15" selected>前十五</option>
			<option value="20" >前二十</option>
		</select> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" class="btn btn-primary" value="查询"  id="search" />
	</div>

	<div id="tabL" style="border:1px solid #D4E7F0;width: 50%;height: 50%;">
		<table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" id="myTableL">
			<caption style="text-align:center;font-size:18px"><a class="tableCss yearFont">xxx</a>年<a class="tableCss monthFont">xxx</a>月前<a class="tableCss topFont">x</a>位用户用水量排名</caption>
			<thead>
				<tr>
					<th>排名</th>
					<th>用户编号</th>
					<th>用户姓名</th>
					<th>当月用水量</th>
					<th>上月用水量</th>
					<th>环比增长率</th>
				</tr>
			</thead>
			<tbody id="myTabL">

			</tbody>
		</table>
	</div>
	<div id="tabR" style="border:1px solid #D4E7F0;width: 50%;height: 50%;">
		<table class="imgtable"  border="1" cellpadding="0" cellspacing="0" align="center" id="myTableR">
			<caption style="text-align:center;font-size:18px"><a class="tableCss yearFont">xxx</a>年<a class="tableCss monthFont">xxx</a>月前<a class="tableCss topFont">x</a>位用户水费排名</caption>
			<thead>
				<tr>
					<th>排名</th>
					<th>用户编号</th>
					<th>用户姓名</th>
					<th>当月水费</th>
					<th>上月水费</th>
					<th>环比增长率</th>
				</tr>
			</thead>
			<tbody id="myTabR">

			</tbody>
		</table>
	</div>
	<div id="pieL" style="width: 50%;height: 35.7%;">

	</div>
	<div id="pieR" style="width: 50%;height: 35.7%;">

	</div>
</body>
</html>
