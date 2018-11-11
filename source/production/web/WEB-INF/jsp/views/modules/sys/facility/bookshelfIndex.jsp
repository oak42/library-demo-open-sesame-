<%--@elvariable id="filterBookshelf" type="com.ackerley.library.modules.sys.entity.Bookshelf"--%>
<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo<com.ackerley.library.modules.sys.entity.Bookshelf>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html id="bookshelfIndex"><!--【扣】便于捕获iframe的ready事件，非得这样吗？-->
<head>
  <title>Title</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    th, td {
      vertical-align: middle;/*【扣】为何无效果？*/
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>

<a class="btn btn-primary m-4" href="${ctx}/sys/fclt/bookshelf/create">新建书架</a>

<table class="table table-sm table-bordered table-striped table-hover ">
  <thead class="thead-dark">
  <tr>
    <th>标签: <input type="text" name="label" value="${filterBookshelf.label}" class="form-control filterInput" style="width: 7em; display: inline;"/></th>
    <th>地点: <input type="text" name="location" value="${filterBookshelf.location}" class="form-control filterInput" style="width: 7em; display: inline;"/></th>
    <th>
      每页 <input type="number" name="rows" value="${pageInfo.pageSize}" class="form-control paginationInput" style="width: 4em; display: inline; vertical-align: inherit;"> 条
      <button id="filter" class="btn btn-light">筛选</button>
    </th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="bookshelf" items="${pageInfo.list}">
    <tr>
      <td>${bookshelf.label}</td>
      <td>${bookshelf.location}</td>
      <td>
        <div class="btn-group" style="vertical-align: inherit;">
          <a class="btn btn-info" href="${ctx}/sys/fclt/bookshelf/update?ID=${bookshelf.ID}">改</a>
          <a class="btn btn-danger" href="${ctx}/sys/fclt/bookshelf/delete?ID=${bookshelf.ID}">删</a>
        </div>
      </td>
    </tr>
  </c:forEach>
  
  </tbody>
</table>

<sys:paginationNav pageInfo="${pageInfo}" filterStr="&label=${filterBookshelf.label}&location=${filterBookshelf.location}"/>

<script type="text/javascript">
    "use strict";
    $("#bookshelfIndex").ready(function () {      //【扣】iframe的ready handler？直接document的ready handler有时出问题...

        $("#filter").click(function () {
            location.href = location.href.split("?")[0] + "?" + $(".filterInput").serialize() + "&" + $(".paginationInput").serialize();  /*【鸣】每个frame都有自己的window */
        });

    });
</script>
</body>
</html>
