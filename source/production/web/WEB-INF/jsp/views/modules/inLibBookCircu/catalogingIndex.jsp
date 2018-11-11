<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo<com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc>"--%>
<html>
<head>
  <title>采购单</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    .listHeaderRow, .listItemRow, .qty {
      display: flex;
      align-items: center; /* flex垂直居中 */
    }

    .toBeCatalogedRow {
      margin: 3px;
      border: groove paleturquoise 1px;
    }

    .purOrderRow .colImg img {
      width: 100%;
      /*display: block;*/
    }

  </style>
</head>
<body>
<sys:message content="${message}"/>

<c:if test="${pageInfo.list.size() == 0}">
  <sys:message content="已无可编目书目..." type="warning"/>
</c:if>
<c:if test="${pageInfo.list.size() > 0}">
<div class="container-fluid">
  <c:forEach var="procInstc" items="${pageInfo.list}">
    <div class="row toBeCatalogedRow"> <!--每一条purOrder的容器，一行-->
      <div class="col-4 ">
        ${procInstc.ISBN13}
      </div>
      <div class="col-4 ">
        ${procInstc.title}
      </div>
      <div class="col-3 ">
        <a class="btn btn-primary" href="${ctx}/inLibCircu/ctlg/doCataloging?ID=${procInstc.ID}&ISBN13=${procInstc.ISBN13}">编目该项</a>
      </div>
    </div>
  </c:forEach>
</div>

  <sys:paginationNav pageInfo="${pageInfo}" filterStr=""/>
</c:if>

<script type="text/javascript">
    "use strict";

</script>
</body>
</html>
