<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>登入入口</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    .myShadow:hover {
      box-shadow: 0 14px 42px 7px #E7E7E7;
    }
  </style>
</head>
<body style="text-align: center;"><!--【鸣】text-align乃inherited属性-->
  <div id="messageContainer" style="height: 7em;"><sys:message content="${message}"/></div>
  <form method="post" id="loginForm" class="card myShadow" style="width: 22em; margin: auto;"><!--【扣】加了margin:auto能居中，大概左右margin平均分配了-->
    <div class="card-header"><h3>${fns:getConfig("brandName")}</h3></div>
    <div class="card-body">
      <div class="form-group">
        <label for="username">登录名</label>
        <input id="username" name="username" type="text" class="form-control" required>
      </div>
      <div class="form-group">
        <label for="password">密码</label>
        <input id="password" name="password" type="password" class="form-control" required>
      </div>
      <input type="submit" class="btn btn-primary" value="登 入">
    </div>
  </form>

<script type="text/javascript">
    "use strict";

    $("#loginForm").validate();
</script>
</body>
</html>
