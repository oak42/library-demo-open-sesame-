<%--@elvariable id="bulkBookShelvingAid" type="com.ackerley.library.modules.inLibBookCircu.entity.BulkBookShelvingAid"--%>
<html>
<head>
  <title>图书上架</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    th {
      box-shadow: inset 0 0.14em 14px 0 #E7E7E7;
      text-align: center;
      vertical-align: middle;
    }
    input[type="checkbox"] {
      zoom: 200%;
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>

<c:if test="${bulkBookShelvingAid.list.size() == 0}">
  <sys:message content="已无可上架图书..." type="warning"/>
</c:if>
<c:if test="${bulkBookShelvingAid.list.size() > 0}">
<form method="post" action="${ctx}/inLibCircu/shelving/doShelving">
  <table class="table table-sm table-striped">
    <thead class="bg-light">
    <tr>
      <th>#</th><th>条码号</th><th>ISBN13</th><th>书名</th><th><label for="checkUncheckAll">上架<input type="checkbox" id="checkUncheckAll"/></label></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="bsrp" items="${bulkBookShelvingAid.list}" varStatus="sts">
      <tr class="toBeShelvedRow">
        <th class="">
            ${sts.index + 1}<input type="hidden" name="list[${sts.index}].subject.ID" value="${bsrp.subject.ID}"/>
        </th>
        <td class="">
            ${bsrp.subject.barCode}>
        </td>
        <td class="">
            ${bsrp.subject.biblio.ISBN13}
        </td>
        <td class="">
            ${bsrp.subject.biblio.title}<c:if test="${bsrp.subject.biblio.subtitle != null && bsrp.subject.biblio.subtitle != ''}"> : ${bsrp.subject.biblio.subtitle}</c:if>
        </td>
        <td style="text-align: center;"><input type="checkbox" class="result" name="list[${sts.index}].result"/></td>
      </tr>
    </c:forEach>
    </tbody>
    <tfoot>
    <tr><th colspan="99" style="text-align: right"><button type="submit" class="btn btn-primary mr-2">确认提交</button></th></tr>
    </tfoot>
  </table>
</form>
</c:if>

<script type="text/javascript">
    "use strict";

    //群check、群uncheck...
    $("#checkUncheckAll").change(function () {
        if($(this).is(":checked")) {
//            $(".result").attr("checked", true);   有问题...
            $(".result").prop("checked", true); //【待】【扣】attr、prop的区别，attribute、property的区别 jQuery in action 第四章...？猜是否prop作用于DOM tree，而attr作用于document，第二次点击document到DOM tree的解析罢工了？
        } else {
//            $(".result").attr("checked", false);  有问题...
            $(".result").prop("checked", false);
        }
    });
</script>
</body>
</html>
