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
    <title>House Keep Main page</title>
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


<table class="table" style="width: 80%; margin: auto">
    <caption>ZOHO基本的操作</caption>
    <thead>
    <tr>
        <th>Environment Auto detection</th>
        <th>Normal House Keep</th>
        <th>Delete ALL Module Records</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td> <button type="button" class="btn btn-primary" id="env">Submit</button></td>
        <td> <button type="button" class="btn btn-success" id="hs">Submit</button></td>
        <td> <button type="button" class="btn btn-info" id="del">Submit</button></td>
    </tr>
    </tbody>
</table>



<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Result Display Title
                </h4>
            </div>
            <div class="modal-body" id="content">
                在这里添加一些文本
            </div>
            <%--<div class="modal-footer">--%>
                <%--<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
                <%--<button type="button" class="btn btn-primary">--%>
                    <%--提交更改--%>
                <%--</button>--%>
            <%--</div>--%>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>

</html>
<script>
    //var $j = jQuery.noConflict();
    $(function () {
        $("#env").click(function(){
            $.ajax({
                url:"<%=request.getContextPath()%>/houseKeep/1",
                method:"POST",
                dataType:"json",
                success: function(result){
                    if(result[0] == true){
                        //alert("Success");
                        //$('#myModal').modal(options);
                        $("#content").html("<strong>Congratulations! Environment Auto detection Successfully.  </strong> <br>");
                        $('#myModal').modal({
                            keyboard: true
                        })
                    }else {
                        $("#content").html("<strong>So bad! There are some problems, pls find the root Reason and fixed ,  then you can do next step : </strong>" +
                                "<br> <font color='red'>"+result[1]+"</font>");
                        $('#myModal').modal({
                            keyboard: true
                        })
                    }

                }
            })
        })
        $("#hs").click(function(){
            $.ajax({
                url:"<%=request.getContextPath()%>/houseKeep/2",
                method:"POST",
                dataType:"json",
                success: function(result){
                    if(result[0] == true){
                        //alert("Success");
                        //$('#myModal').modal(options);
                        $("#content").html("<strong>Congratulations! Normal House Keep Successfully.  </strong> <br>");
                        $('#myModal').modal({
                            keyboard: true
                        })
                    }else {
                        $("#content").html("<strong>So bad! There are some problems, pls find the root Reason and fixed ,  then you can do next step : </strong>" +
                                "<br> <font color='red'>"+result[1]+"</font>");
                        $('#myModal').modal({
                            keyboard: true
                        })
                    }
                }
            })
        })
        $("#del").click(function(){
            $.ajax({
                url:"<%=request.getContextPath()%>/houseKeep/3",
                method:"POST",
                dataType:"json",
                success: function(result){
                    if(result[0] == true){
                        //alert("Success");
                        //$('#myModal').modal(options);
                        $("#content").html("<strong>Congratulations! Delete ALL Module Records Successfully.</strong>  ");
                        $('#myModal').modal({
                            keyboard: true
                        })
                    }else {
                        $("#content").html("<strong>So bad! There are some problems, pls find the root Reason and fixed ,  then you can do next step :</strong> " +
                                "<br> <font color='red'>"+result[1]+"</font>");
                        $('#myModal').modal({
                            keyboard: true
                        })
                    }
                }
            })
        })
    })

</script>