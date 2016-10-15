<%@ page import="darlen.crm.util.ModuleNameKeys" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.ArrayList" %>
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
    <title>Show Module(ModuleName) List</title>
    <%--<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">--%>
    <%--<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>--%>
    <%--<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>--%>
    <%--<link rel="stylesheet" href="vendor/css/bootstrap.min.css">--%>
    <%--<script src="vendor/js/jquery.min.js"></script>--%>
    <%--<script src="vendor/js/bootstrap.min.js"></script>--%>


</head>
<body>
<jsp:include page="menu.jsp"></jsp:include>
<div id="success"style="width: 50%; margin: auto;display: none" class="alert alert-success">You Already Registrate the ZOHO Batch Job</div>
<div id="fail"style="width: 50%; margin: auto;display: none" class="alert alert-info">You Already STOP the ZOHO Batch Job</div>
<%

    List list = null;
    String moduleName=(String) request.getAttribute("name");
    //list = (List) request.getAttribute(ModuleNameKeys.Accounts.toString());
    //System.out.println( "@@@@@@@@@@@"+request.getAttribute("moduleResultList"));
    list =  (List)request.getAttribute("moduleResultList");
    List<String> fieldNameList = ( List<String>)request.getAttribute("fieldNames");


%>


<table class="table" style="width: 80%; margin: auto">
    <caption>ZOHO基本的操作</caption>
    <thead>
    <tr>
        <th>Show Result As String</th>
        <th>Show Result As Table</th>
        <th>Customer</th>
        <th>Products</th>
        <th>Quotes</th>
        <th>SO</th>
        <th>Invoices</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td> <button type="button" class="btn btn-default" id="resultStr">Show String</button></td>
        <td> <button type="button" class="btn btn-default" id="resultTable">Show Table</button></td>
        <td> <button type="button" class="btn btn-primary" id="accounts">Fetch Customer Records</button></td>
        <td> <button type="button" class="btn btn-success" id="prod">Fetch Products Records</button></td>
        <td> <button type="button" class="btn btn-info" id="quotes">Fetch Quotes Records</button></td>
        <td> <button type="button" class="btn btn-warning" id="so">Fetch SO Records</button></td>
        <td> <button type="button" class="btn btn-danger" id="invoices">Fetch Invoices Records</button></td>
    </tr>
    </tbody>
</table>
<% if(null == list || list.size() > 0) {%>
<div id="showTable">
<table class="table table-bordered" style=" margin: 10px">
    <caption>Show Module(<%=moduleName%>)
        <%--<span><button type="button" class="btn btn-default" id="resultStr">Show</button></span>--%>
        <%--<span><button type="button" class="btn btn-default" id="resultStr">Show</button></span>--%>
    </caption>
    <thead>
    <tr>
        <th>NO</th>
        <%
        for(String fieldName : fieldNameList){
            %>
                 <th><%=fieldName%></th>
            <%
        }
        %>
    </tr>
    </thead>
    <tbody id="result">
        <%
            for(int i = 0; i < list.size(); i++){
            Map<String,Object> map = (Map<String,Object> ) list.get(i);

            %>
                <tr>
                    <th><%=i+1%></th>
                    <%

                        for(Map.Entry<String,Object> entry : map.entrySet()){
                            String fieldName = entry.getKey();
                            if(!"RemarkBin".equalsIgnoreCase(fieldName)){
                            Object fieldNameVal = entry.getValue();
                            %><td><%=fieldNameVal%></td><%
                             }

                        }
                     %>
                </tr>
            <%
            }
        %>
    </tbody>
</table>
</div>
<%}%>
<div style="display: none" id="showStr">
    <%   out.print(request.getAttribute("moduleResultList"));%>
</div>


</body>

</html>
<script>
    $(function(){

        var item = $("#result  tr");
        for(var i=0;i<item.length;i++){
            if(i%2==0){
                //item[i].style.backgroundColor="#888";
                $(item[i]).addClass("active");
            }else{
                $(item[i]).addClass("success");
            }
        }

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
        $("#accounts").click(function(){
            <%--$.post("<%=request.getContextPath()%>/module/Accounts");--%>
            window.location.href = "<%=request.getContextPath()%>/module/Accounts";
        })
        $("#prod").click(function(){
            <%--$.post("<%=request.getContextPath()%>/module/Products");--%>
            window.location.href = "<%=request.getContextPath()%>/module/Products";
        })
        $("#quotes").click(function(){
            <%--$.post("<%=request.getContextPath()%>/module/Quotes");--%>
            window.location.href = "<%=request.getContextPath()%>/module/Quotes";
        })
        $("#so").click(function(){
            <%--$.post("<%=request.getContextPath()%>/module/SalesOrders");--%>
            window.location.href = "<%=request.getContextPath()%>/module/SalesOrders";
        })
        $("#invoices").click(function(){
            <%--$.post("<%=request.getContextPath()%>/module/Invoices");--%>
            window.location.href = "<%=request.getContextPath()%>/module/Invoices";
        })
        $("#resultStr").click(function(){
            $("#showStr").toggle();
        })
        $("#resultTable").click(function(){
            $("#showTable").toggle();
        })



    })


</script>