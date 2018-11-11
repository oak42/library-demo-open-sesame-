<%--@elvariable id="purOrder" type="com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrder"--%>
<html>
<head>
  <title>采购单 查看</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    #header, #footer {
      height: 2.5rem;
    }

/*
    table tr {
      height: 5rem;
    }
*/
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
        订单状态：<c:if test="${purOrder.orderState.equals('acquiring')}">尚未入库</c:if>
                 <c:if test="${purOrder.orderState.equals('inLib')}">已验收入库</c:if>
      </th>
    </tr>
    <tr id="header" class="" style="background-color: #c82333;">
      <th style="min-width: 2em;">#</th>
      <th>ISBN13</th>
      <th style="min-width: 14em;">标题</th>
      <th>作者</th>
      <th>译者</th>
      <th style="min-width: 7em;">出版商</th>
      <th style="min-width: 7em;">出版时间</th>
      <th style="min-width: 9em;">计划采购本/套数</th>
      <c:if test="${purOrder.orderState.equals('inLib')}"><th style="min-width: 9em;">实际验收入库数</th></c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="item" items="${purOrder.list}" varStatus="sts">
      <tr class="itemRow">
        <td>${sts.index + 1}</td>
        <td class="ISBN13">
            ${item.ISBN13}
        </td>
        <td class="title"></td>
        <td class="author"></td>
        <td class="translator"></td>
        <td class="publisher"></td>
        <td class="pubdate"></td>
        <td>
            ${item.orderQty}
        </td>
        <c:if test="${purOrder.orderState.equals('inLib')}"><td>${item.inboundQty}</td></c:if>
      </tr>
    </c:forEach>
    </tbody>
    <tfoot class="" style="background-color: gray;">
    <tr id="footer">
      <td colspan="99">
        <a class="btn btn-primary" href="${ctx}/priorCircu/purOrder/">返回</a>
      </td>
    </tr>
    </tfoot>
  </table>

<script>
    "use strict";

    $(function () {
        var urlForepart = "https://api.douban.com/v2/book/isbn/";
        $("td.title").each(function () {
            var $itemRow = $(this).parents(".itemRow");
            $.ajax({
                url: urlForepart + $.trim($(".ISBN13", $itemRow).text()),
                type: "get",
                dataType: "jsonp",
                success: function (data) {
                    $(".title", $itemRow).text(data.title);
                    $(".author", $itemRow).text(data.author);
                    $(".translator", $itemRow).text(data.translator);
                    $(".publisher", $itemRow).text(data.publisher);
                    $(".pubdate", $itemRow).text(data.pubdate);
                }
            });
        })
    });

</script>
</body>
</html>
