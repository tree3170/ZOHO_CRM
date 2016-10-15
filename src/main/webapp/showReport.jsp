<%@ page import="darlen.crm.util.ModuleNameKeys" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="darlen.crm.model.result.Report" %>
<%--
    Desc:
    User:     Darlen liu
    Date:     16-10-9 09：50
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Show Report(Latest 1 month)</title>
    <%--<link rel="stylesheet"href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">--%>
    <%--<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>--%>
    <%--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>

    <%--<link rel="stylesheet" href="vendor/css/bootstrap.min.css">--%>
    <%--<script src="vendor/js/jquery.min.js"></script>--%>
    <%--<script src="vendor/js/bootstrap.min.js"></script>--%>


</head>
<body>
<jsp:include page="menu.jsp"></jsp:include>
<div id="success" style="width: 50%; margin: auto;display: none" class="alert alert-success">You
    Already Registrate the ZOHO Batch Job
</div>
<div id="fail" style="width: 50%; margin: auto;display: none" class="alert alert-info">You Already
    STOP the ZOHO Batch Job
</div>




<form class="form-horizontal" role="form" method="post" action="<%=request.getContextPath()%>/report" style="border: 1px solid #eee; width: 50%;margin: auto">
    <div class="form-group">
        <label for="datetimepicker_1" class="col-sm-2 control-label">Start Time</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="datetimepicker_1" style="width: 200px" name ="startTime"
                   placeholder="Please Enter Start Time" value="<%=request.getAttribute("startTime")%>">
        </div>
    </div>
    <div class="form-group">
        <label for="datetimepicker_2" class="col-sm-2 control-label">End Time</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="datetimepicker_2" style="width: 200px" name ="endTime"
                    placeholder="Please Enter End Time" value="<%=request.getAttribute("endTime")%>">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-success">Submit</button>
            <button type="reset" class="btn btn-warning">Reset</button>
        </div>
    </div>
</form>

<%
    List<Report> reports = (List<Report>)request.getAttribute("reports");
%>
<table class="table table-bordered" style="width: 80%;margin: auto;">
    <caption>SHOW REPORT AS TABLE
    </caption>
    <thead>
    <tr>
        <th>NO</th>
        <th>REPORTID</th>
        <th>START_TIME</th>
        <th>END_TIME</th>
        <th>INS_FAILED</th>
        <th>UPD_FAILED</th>
        <th>DEL_FAILED</th>
        <th>WHOLEFAIL</th>
    </tr>
    </thead>
    <tbody id="result">
    <%
        for(int i = 0; i < reports.size(); i++){
            Report report =  reports.get(i);

    %>
    <tr>
        <th><%=i+1%></th>
        <th><%=report.getREPORTID()%></th>
        <th><%=report.getSTART_TIME()%></th>
        <th><%=report.getEND_TIME()%></th>
        <th><%=report.getINS_FAILED()%></th>
        <th><%=report.getUPD_FAILED()%></th>
        <th><%=report.getDEL_FAILED()%></th>
        <th><%=report.getWHOLEFAIL()%></th>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

</body>

<link href="//cdn.bootcss.com/jquery-datetimepicker/2.5.4/jquery.datetimepicker.css" rel="stylesheet">
<script src="http://www.jq22.com/demo/datetimepicker-master20160419/build/jquery.datetimepicker.full.js"></script>
</html>
<script>
    //var $j = jQuery.noConflict();
    $(function () {
        var item = $("#result  tr");
        for (var i = 0; i < item.length; i++) {
            if (i % 2 == 0) {
                //item[i].style.backgroundColor="#888";
                $(item[i]).addClass("active");
            } else {
                $(item[i]).addClass("success");
            }
        }

        $('#datetimepicker_1').datetimepicker({
            lang:"ch",           //语言选择中文
            format:"Y-m-d H:m:s",      //格式化日期
            timepicker:true,  //关闭时间选项
            Mask:true,
            yearStart:1900,     //设置最小年份
            yearEnd:2050,        //设置最大年份
            todayButton:false    //关闭选择今天按钮
        });
        $('#datetimepicker_2').datetimepicker({
            lang:"ch",           //语言选择中文
            format:"Y-m-d H:m:s",      //格式化日期
            timepicker:true,  //关闭时间选项
            Mask:true,
            yearStart:1900,     //设置最小年份
            yearEnd:2050,        //设置最大年份
            todayButton:false    //关闭选择今天按钮
        });

    })

</script>