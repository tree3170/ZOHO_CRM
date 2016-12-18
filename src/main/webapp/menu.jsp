
<link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="http://cdn.static.runoob.com/libs/jquery/2.1.0/jquery.min.js"></script>
<script src="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<nav class="navbar navbar-inverse" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">ZOHO Operation Menu</a>
        </div>
        <div>
            <ul class="nav navbar-nav">
                <%--<li class="active"><a href="<%=request.getContextPath()%>/quartzMain.jsp">Start/Stop Service</a></li>--%>
                <li ><a href="<%=request.getContextPath()%>/quartzMain.jsp">Start/Stop Service</a></li>
                <li class="dropdown">
                    <a href="<%=request.getContextPath()%>/module/index" class="dropdown-toggle" data-toggle="dropdown">
                        Show Database Data <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="<%=request.getContextPath()%>/module/Accounts">Show Customer</a></li>
                        <li><a href="<%=request.getContextPath()%>/module/Products">Show Product</a></li>
                        <li><a href="<%=request.getContextPath()%>/module/Quotes">Show Quotes</a></li>
                        <%--<li class="divider"></li>--%>
                        <%--<li><a href="#">分离的链接</a></li>--%>
                        <%--<li class="divider"></li>--%>
                        <li><a href="<%=request.getContextPath()%>/module/SalesOrders">Show SalesOrders</a></li>
                        <li><a href="<%=request.getContextPath()%>/module/Invoices">Show Invoices</a></li>
                    </ul>
                </li>
                <li><a href="<%=request.getContextPath()%>/report">Show Report</a></li>
                <li><a href="<%=request.getContextPath()%>/houseKeepMain.jsp">HouseKeep</a></li>
                <li><a href="<%=request.getContextPath()%>/sepRunFunction.jsp">Separate Run Manually</a></li>
            </ul>
        </div>
    </div>
</nav>
