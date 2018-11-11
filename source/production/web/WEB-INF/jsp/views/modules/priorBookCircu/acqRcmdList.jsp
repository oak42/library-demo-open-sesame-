<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo<com.ackerley.library.modules.priorBookCircu.PBCProcInstc>"--%>
<%--@elvariable id="rcmd" type="com.ackerley.library.modules.priorBookCircu.PBCProcInstc"--%>
<%--@elvariable id="actnRecord" type="com.ackerley.library.modules.priorBookCircu.entity.PBCActnRecord"--%>
<html>
<head>
  <title>建议采编 进度</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    .rcmdItemRow {
      margin: 3px;
      border: groove paleturquoise 1px;
    }

    .rcmdItemRow .colImg img {
      width: 100%;
      /*display: block;*/
    }

    div.colBookDetails {
      background-color: #E7E7E7;
    }

    div.colStageAndActionInfo {
      background-color: gainsboro;
    }

    .colBookDetails table th {
      width: 5em;
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>
<a class="btn btn-primary m-4" href="${ctx}/priorCircu/rcmd/create">发起新采编建议</a>

<div id="rcmdListContainer">
  <h3>您提交过的历次建议：</h3>
<c:forEach var="rcmd" items="${pageInfo.list}">
  <div class="row rcmdItemRow"> <!--每一条记录的容器，一行-->
    <div class="ISBN13" hidden>${rcmd.ISBN13}</div>
    <div class="col-2 colImg">
      <img src="" style="max-height: 14em"/>
    </div>
    <div class="col-4 colBookDetails">
      <table>
      </table>
    </div>
    <div class="col-6 colStageAndActionInfo">
      <table>
        <caption style="caption-side: top;">建议状态</caption>
        <thead>
        <tr>
          <th>时间</th><th>流程节点</th><th>备注</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="actnRecord" items="${rcmd.actnRecordList}">
          <tr>
            <td>${actnRecord.actionTime}</td><td>${actnRecord.action}</td><td>${actnRecord.remarks}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
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
                    for (var key in textualBookDetailKeywords) {      //这里实际上是用的object index？对于一个js的array，其properties是什么？或许还是用number index来索引比较好...
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
