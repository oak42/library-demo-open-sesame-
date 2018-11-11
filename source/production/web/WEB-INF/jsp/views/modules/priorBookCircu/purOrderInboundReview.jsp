<%--@elvariable id="purOrder" type="com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrder"--%>
<html>
<head>
  <title>采购单 入库验收</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    #header, #footer {
      height: 2.5rem;
    }

    th {
      text-align: center;
    }

    td, th {
      word-break: keep-all;
      max-width: 25em;
    }
  </style>
</head>
<body>
<sf:form modelAttribute="purOrder" method="post" id="theOnlyForm">
  <sf:hidden path="orderState" value="inLib"/>
  <table class="table table-striped table-bordered ">
    <thead>
    <tr>
      <th colspan="99" style="text-align: center;">
        <h4>${purOrder.name}</h4>
      </th>
    </tr>
    <tr>
      <th colspan="99" style="text-align: center;">
        创建人:${purOrder.creatorID}&nbsp;&nbsp;&nbsp;<%--后面得改为凭ID找name，可使用static method，封为tag--%>
        创建时间:${purOrder.creationTime}&nbsp;&nbsp;&nbsp;
      </th>
    </tr>
    <tr id="header" class="" style="background-color: #c82333;">
      <th style="min-width: 2em;">#</th>
      <th>ISBN13</th>
      <th style="min-width: 14em;">标题</th>
      <th style="min-width: 9em;">计划采购本/套数</th>
      <th style="min-width: 9em;">实际验收入库数</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="index" begin="0" end="${purOrder.list.size() - 1}">
      <sf:hidden path="list[${index}].ID"/>
      <tr class="itemRow">
        <td>${index + 1}</td>
        <td class="ISBN13">
          <sf:input path="list[${index}].ISBN13" cssStyle="border: none; background-color: transparent;" readonly="true"/>
        </td>
        <td class="title"></td>
        <td>
            ${purOrder.list[index].orderQty}
        </td>
        <td>
          <sf:input path="list[${index}].inboundQty" digits="true" min="0"/>
        </td>
      </tr>
    </c:forEach>
    </tbody>
    <tfoot class="" style="background-color: gray;">
    <tr id="footer">
      <td colspan="99">
        <a class="btn btn-warning" href="${ctx}/priorCircu/purOrder/">退出验收 返回</a>
        <button type="submit" class="btn btn-success">确认验收 提交</button>
      </td>
    </tr>
    </tfoot>
  </table>
</sf:form>


<script>
    "use strict";

    $(function () {
        var urlForepart = "https://api.douban.com/v2/book/isbn/";
        $("td.title").each(function () {
            var $itemRow = $(this).parents(".itemRow");
            $.ajax({
                url: urlForepart + $.trim($(".ISBN13 input", $itemRow).val()) + "?fields=title",/* 可以通过 fields 参数指定返回数据中的信息项的字段，以减少返回数据中开发者并不关心的部分。"?fields=id,title,url" */
                type: "get",
                dataType: "jsonp",
                success: function (data) {
                    $(".title", $itemRow).text(data.title + " : " + data.subtitle);
                }
            });
        })
    });

    //前端校验，
    $("#theOnlyForm").validate();

</script>
</body>
</html>
