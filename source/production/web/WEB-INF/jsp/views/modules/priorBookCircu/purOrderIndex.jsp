<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo<com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrder>"--%>
<html>
<head>
  <title>采购单</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    .listHeaderRow, .listItemRow, .qty {
      display: flex;
      align-items: center; /* flex垂直居中 */
    }

    .purOrderRow {
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
<a class="btn btn-primary m-4" href="${ctx}/priorCircu/purOrder/create">新建采购单</a>

<div id="purOrderListContainer" class="container-fluid">
  <c:forEach var="purOrder" items="${pageInfo.list}">
  <div class="row no-gutters purOrderRow"> <!--每一条purOrder的容器，一行-->
    <div class="col-4 ">
      ${purOrder.creationTime}
    </div>
    <div class="col-4 ">
      ${purOrder.name}
    </div>
    <div class="col-3 ">
      <div class="btn-group">
        <c:if test="${purOrder.orderState.equals('acquiring')}"><!--'done'(已验收入库)的单子不允许删、改-->
          <a class="btn btn-danger" href="${ctx}/priorCircu/purOrder/delete?ID=${purOrder.ID}">删</a>
          <%--<a class="btn btn-warning" href="">改</a>--%>
        </c:if>
        <a class="btn btn-primary" href="${ctx}/priorCircu/purOrder/items?ID=${purOrder.ID}">详</a>
      </div>
    </div>
    <div class="col-1 ">
      <c:if test="${purOrder.orderState.equals('acquiring')}"><a class="btn btn-primary btn-sm" href="${ctx}/priorCircu/purOrder/inboundReview?ID=${purOrder.ID}">入库验收</a></c:if>
      <c:if test="${purOrder.orderState.equals('inLib')}"><div class="badge badge-pill badge-success">已验收、核对数目</div></c:if>
    </div>
  </div>
  </c:forEach>
  
  <sys:paginationNav pageInfo="${pageInfo}" filterStr=""/>
</div>


<script type="text/javascript">
    "use strict";
    //还未采编入库的书目信息都是 从豆瓣API 凭isbn13 即时获取...
    $(function () {
        var urlForepart = "https://api.douban.com/v2/book/isbn/";
        var textualBookDetailKeywords = [
            {
                jsonProperty: "isbn13",
                thLabel: "ISBN13"
            },
            {
                jsonProperty: "title",
                thLabel: "标题"
            },
            {
                jsonProperty: "subtitle",
                thLabel: "副标题"
            },
            {
                jsonProperty: "author",
                thLabel: "作者"
            },
            {
                jsonProperty: "translator",
                thLabel: "译者"
            },
            {
                jsonProperty: "translator",
                thLabel: "译者"
            },
            {
                jsonProperty: "publisher",
                thLabel: "出版商"
            },
            {
                jsonProperty: "pubdate",
                thLabel: "出版日期"
            }
        ];

        $(".rcmdItemRow").each(function () {
            var rcmdItemRow = this;
            $.ajax({
                url: urlForepart + $(".ISBN13", rcmdItemRow).text(),
                type: "get",
                dataType: "jsonp",
                success: function (data) {
                    $(".colImg img", rcmdItemRow).attr("src", data.images.small);
                    for (var key in textualBookDetailKeywords) {
                        if (data.hasOwnProperty(textualBookDetailKeywords[key].jsonProperty)) {
                            $(".colBookDetails table", rcmdItemRow).append("<tr><th>" +
                                textualBookDetailKeywords[key].thLabel + ": </th><td>" +
                                data[textualBookDetailKeywords[key].jsonProperty] + "</td></tr>");
                        }
                    }
                }
            });
        });
    });
</script>
</body>
</html>
