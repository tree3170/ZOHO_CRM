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
    <title>Separate Run Manually</title>
<style >
    .table-horizontal{float:left;}
    .table-width{width:100%}
</style>

</head>
<body>
<jsp:include page="menu.jsp"></jsp:include>
<div id="success" style="width: 50%; margin: auto;display: none" class="alert alert-success">You
    Already Registrate the ZOHO Batch Job
</div>
<div id="fail" style="width: 50%; margin: auto;display: none" class="alert alert-info">You Already
    STOP the ZOHO Batch Job
</div>

<form class="form-inline"  role="form" method="post" action="<%=request.getContextPath()%>/sepRun/search" style="border: 1px solid #eee; margin: auto">
    <span class="help-block">Module is required ,one of  Start Time and Erp ID list is optional  </span>
    <div class="form-group table-width" style="display: block;margin-bottom: 5px;" >
        <label class="col-sm-2 control-label" for="module">Module<span style="color: red">*</span></label>
        <select class="form-control" name="module" id="module" style="width: 200px;margin-left: 15px;">
            <option value ="<%=ModuleNameKeys.Accounts.toString()%>">Accounts</option>
            <option value ="<%=ModuleNameKeys.Products.toString()%>">Products</option>
            <option value ="<%=ModuleNameKeys.Quotes.toString()%>">Quotes</option>
            <option value ="<%=ModuleNameKeys.SalesOrders.toString()%>">SOs</option>
            <option value ="<%=ModuleNameKeys.Invoices.toString()%>">Invoices</option>
        </select>
    </div>
    <div class="form-group table-width" style="margin-bottom: 5px;">
        <label for="datetimepicker_1" class="col-sm-2 control-label">Latest Edit Time</label>
        <div class="col-sm-10 table-horizontal" style="width: 200px">
            <input type="text" class="form-control" id="datetimepicker_1" style="width: 200px" name ="latestEditTime"
                   placeholder="Please Enter Start Time" value="">
        </div>
    </div>
    <div class="form-group table-width" style="margin-bottom: 5px;">
        <label for="erpIDs" class="col-sm-2 control-label">ERP ID List(aplit as comma)</label>
        <div class="col-sm-10 table-horizontal" style="width: 200px">
            <textarea class="form-control" rows="5" cols="100" name="erpIDs" id="erpIDs" ></textarea>

        </div>
    </div>

    <div class="form-group table-width" style="display: block; margin-bottom: 5px;">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="button" class="btn btn-success" id="search">Search(by time/erpid)</button>
            <button type="button" id="send" class="btn btn-success">Send(by erpid)</button>
            <button type="reset" class="btn btn-warning">Reset</button>
        </div>
    </div>
</form>

</br>
</br>
<div>
<span class="help-block" style="color: red">display valid ERP IDs :</span>
<span id="validErpIDs"></span>
</div>

<!--for display request/response url when send button clicked-->
<div class="col-sm-10 table-horizontal" style="width: 100%;margin-top: 50px;">
    <table>
        <tr>
            <td><textarea class="form-control" rows="20" cols="350" name="erpIDs" id="requestURL" style="display: inline;width: 400px;"></textarea></td>
            <td><span>>></span></td>
            <td><textarea class="form-control" rows="20" cols="350" name="erpIDs" id="responseURL" style="display: inline;width: 400px;"></textarea></td>
        </tr>
    </table>
</div>

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
        this.getData =  function(){
           return  $data = {
                "module":$("#module").val(),
                "latestEditTime":$("#datetimepicker_1").val(),
                "erpIDs":$("#erpIDs").val()
            };
        }

        $("#search").click(function(){
            //disableAllBtn();
            var erpids = $.trim($("#erpIDs").val());
            var regRule = /^[0-9,]*$/;
            if(erpids != "" && !regRule.test(erpids)){
                alert("每个ERP ID必需是数字，并且如果有多个ERP ID，必需以',' 分隔，比如：1,2,3");
                return false;
            }
            var $data = //this.getData();
            {
                "module":$("#module").val(),
                "latestEditTime":$("#datetimepicker_1").val(),
                "erpIDs":$("#erpIDs").val()
                };
            $.ajax({
                url:"<%=request.getContextPath()%>/sepRun/1",
                method:"POST",
                data: $data,
                dataType:"json",
                success: function(result){
                    //alert(result)

                    if(result[0] == true){
                        $("#validErpIDs").text(result[2])
                        //alert("Success");
                        //$('#myModal').modal(options);
                        //$("#content").html("<strong>Congratulations! Environment Auto detection Successfully.  </strong> <br>");
                        //$('#myModal').modal({
                        //    keyboard: true
                        //})
                    }else {
                        //$("#content").html("<strong>So bad! There are some problems, pls find the root Reason and fixed ,  then you can do next step : </strong>" +
                        //        "<br> <font color='red'>"+result[1]+"</font>");
                        //$('#myModal').modal({
                        //    keyboard: true
                        //})
                        alert("出现错误："+result[1])
                    }
                    //enableAllBtm()
                }
            })
        })

        //强制增加或者更新
        $("#send").click(function(){
            var erpids = $.trim($("#erpIDs").val());
            var regRule = /^[0-9,]*$/;
            if(erpids != "" && !regRule.test(erpids)){
                alert("每个ERP ID必需是数字，并且如果有多个ERP ID，必需以',' 分隔，比如：1,2,3");
                return false;
            }
            var $data = //this.getData();
            {
                "module":$("#module").val(),
                "erpIDs":$("#erpIDs").val()
            };
            $.ajax({
                url:"<%=request.getContextPath()%>/sepRun/2",
                method:"POST",
                data: $data,
                dataType:"json",
                success: function(result){
                    if(result[0] == true){
                        alert("Success:"+result[2])
                    }else {
                        //$("#content").html("<strong>So bad! There are some problems, pls find the root Reason and fixed ,  then you can do next step : </strong>" +
                        //        "<br> <font color='red'>"+result[1]+"</font>");
                        //$('#myModal').modal({
                        //    keyboard: true
                        //})
                        alert("出现错误："+result[1])
                    }
                    //enableAllBtm()
                }
            })
        })


    })

</script>