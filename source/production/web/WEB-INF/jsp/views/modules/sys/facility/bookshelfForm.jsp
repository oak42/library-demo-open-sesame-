<%--@elvariable id="bs" type="com.ackerley.library.modules.sys.entity.Bookshelf"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Title</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
</head>
<body>
<sys:message content="${message}"/>

<sf:form id="form" modelAttribute="bookshelf" method="post" action="/sys/fclt/bookshelf/save">
  <button type="button" class="btn btn-warning" data-toggle="tooltip" data-placement="right" title="当前编辑内容将被丢弃!" onclick="history.go(-1)">← 返回</button>
  <sf:hidden path="ID"/>
  <div class="form-group">
    <label for="label">标签:</label>
    <sf:input id="label" path="label"/>
  </div>
  <div class="form-group">
    <label for="location">地点:</label>
    <sf:input id="location" path="location"/>
  </div>
  <button type="submit" class="btn btn-primary">确认 提交</button>
</sf:form>

<script type="text/javascript">
    "use strict";
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
      /*Tooltips are opt-in for performance reasons, so you must initialize them yourself.*/
    });

    $("#form").validate({
        rules: {
            label: "required",
            location: "required"
        }
    });
</script>
</body>
</html>
