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
    <title>Bootstrap 实例 - 基本的表格</title>
    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <script>
        $(function(){
            $("#start").click(function(){
                $.ajax({
                    url:"<%=request.getContextPath()%>/start",
                    method:"GET",
                    dataType:"json",
                    success: function(result){
                        var retVal =result;
                        //alert("Success");
                        if(result == "0"){
                            $("success").show();
                        }else{
                            alert("");
                        }
                    }
                })
            })

            $("#stop").click(function(){
                $.ajax({
                    url:"<%=request.getContextPath()%>/stop",
                    method:"GET",
                    dataType:"json",
                    success: function(result){
                        var retVal =result;
                        alert("Success");
                    }
                })
            })

        })


    </script>
</head>
<body>
<div id="success"style="width: 50%; margin: auto;display: none" class="alert alert-success">You Already Registrate the ZOHO Batch Job</div>
<div id="fail"style="width: 50%; margin: auto;display: none" class="alert alert-info">You Already STOP the ZOHO Batch Job</div>
<table class="table" style="width: 50%; margin: auto">
    <caption>ZOHO基本的操作</caption>
    <thead>
    <tr>
        <th>名称</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>启动ZOHO操作</td>
        <td><!-- 表示一个成功的或积极的动作 -->
            <button type="button" class="btn btn-success" id="start">Start</button></td>
    </tr>
    <tr>
        <td>停止ZOHO操作</td>
        <td><!-- 表示应谨慎采取的动作 -->
            <button type="button" class="btn btn-warning" id="stop">Stop</button></td>
    </tr>
    <tr>
        <td>发送邮件</td><!--以后这边要加个功能，发送人接受人附件，主题-->
        <td><!-- 表示应谨慎采取的动作 -->
            <button type="button" class="btn btn-warning" id="mail">SendMail</button></td>
    </tr>
    <tr>
        <td>生成Report</td>
        <td><!-- 表示应谨慎采取的动作 -->
            <button type="button" class="btn btn-warning" id="report">Generate Report</button></td>
    </tr>
    </tbody>
</table>

</body>
</html>