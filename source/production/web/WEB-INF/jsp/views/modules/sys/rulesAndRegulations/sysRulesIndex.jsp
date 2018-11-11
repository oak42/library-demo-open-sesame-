<%--@elvariable id="sysRuleList" type="java.util.List<com.ackerley.library.modules.sys.entity.SysRule>"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html id="bookshelfIndex"><!--【扣】便于捕获iframe的ready事件，非得这样吗？-->
<head>
  <title>Title</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    td, th {
      text-align: center;
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>
<table class="table table-bordered table-striped table-hover ">
  <thead class="thead-dark">
  <tr>
    <th>规则参数名</th>
    <th>规则参数值</th>
    <th>操作</th>
  </tr>
  </thead>
  <tbody>
  <c:forEach var="sysRule" items="${sysRuleList}">
    <tr class="ruleRow">
      <td class="parmName">${sysRule.parmName}<input type="hidden" class="ruleID" name="ID" value="${sysRule.ID}"/></td>
      <td class="parmValue">${sysRule.parmValue}</td>
      <td style="text-align: center;">
        <button type="button" class="btn btn-primary ruleEditBtn" data-toggle="modal" data-target="#sysRuleEditModal">修改</button>
      </td>
    </tr>
  </c:forEach>
  </tbody>
  <tfoot>
    <tr>
    </tr>
  </tfoot>
</table>

<div id="sysRuleEditModal" class="modal fade" tabindex="-1">
  <div class="modal-dialog">
    <div class="modal-content">
      <%--<div class="modal-header"></div>--%>
      <div class="modal-body border radious">
        <form id="sysRuleEditForm">
          <label for="parmValue" id="parmName"></label>
          <input id="parmValue" name="parmValue" />
        </form>
      </div>
      <div class="modal-footer">
        <button type="submit" form="sysRuleEditForm">确认提交修改</button>
      </div>
    </div>
  </div>
</div>

<script type="text/javascript">
    "use strict";
    $(".ruleEditBtn").click(function () {
        var $ruleRow = $(this).parents(".ruleRow");
//        $("input.ruleID", $ruleRow).appendTo("#sysRuleEditForm"); //有下一句↓就都在里面了...
        $("#parmName").html($(".parmName", $ruleRow).html());
        $("#parmValue").val($(".parmValue", $ruleRow).text());
    });

    $("#sysRuleEditForm").validate({
        rules: {
            parmValue: {
                required: true,
                number: true
            }
        }
    });
</script>
</body>
</html>
