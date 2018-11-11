<%--@elvariable id="reviewList" type="com.ackerley.library.modules.priorBookCircu.entity.BulkAuditingSFAid"--%><!--TODO 愈发感觉现在的审核辅助对象是个麻烦，现在想到可以在实体bean的继承链中加一层AuditableEntity-->
<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo"--%>
<html>
<head>
  <title>建议审核(一审)</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
/*
    * {
      border-style: dotted;
    }
*/
    #listHeaderPlaceholder, #listHeaderFixedTop, .listHeaderRow {
      height: 4rem;
    }

    .listHeaderRow, .listItemRow, .adjudicate {
      display: flex;
      align-items: center; /* flex垂直居中 */
    }

    .bookMajorDetailLabel {

    }

    input[type="radio"].reject:checked + label,
    input[type="radio"].reject:hover + label {
      color: red;
      text-decoration: underline;
    }

    input[type="radio"].approve:checked + label,
    input[type="radio"].approve:hover + label {
      color: green;
      text-decoration: underline;
    }
  </style>
</head>
<body>
<div id="overallAccordion">
  <div id="listHeaderPlaceholder" class="row no-gutters">
    <div id="listHeaderFixedTop" class="col-12 fixed-top" style="background-color: #c82333;">
      <div class="row no-gutters listHeaderRow">
        <div class="col-8 listHeader">--初审--</div>
        <div class="col-4 listHeader">
          <label for="rowsSelect">一页
            <select id="rowsSelect" class="form-control" style="width: 4em; display: inline; vertical-align: inherit;"><%--style="display: inline;"--%>
              <option value="5" <c:if test="${pageInfo.pageSize == 5}">selected</c:if>> 5 </option>
              <option value="10" <c:if test="${pageInfo.pageSize == 10}">selected</c:if>> 10 </option>
              <option value="20" <c:if test="${pageInfo.pageSize == 20}">selected</c:if>> 20 </option>
            </select> 条
          </label>

        </div>
      </div>
    </div>
  </div>

  <sys:message content="${message}"/>
  <c:if test="${reviewList.list.size() == 0}">
    <sys:message content="已无更多待审核项..." type="warning"/>
  </c:if>
  <c:if test="${reviewList.list.size() > 0}">
    <sf:form modelAttribute="reviewList" method="post" action="${ctx}/priorCircu/rvw/submit">

      <c:forEach var="index" begin="0" end="${reviewList.list.size() - 1}"><!--【扣】待试：若使用items而非begin、end来用index自己为path赋名，是否就能用上泛型版本的入参绑定了？-->
        <div class="row no-gutters listItemRow">

          <div class="col-12 border rounded">
            <div class=" row no-gutters">
              <div class="col-7 ">
                <button class="btn btn-outline-light collapseTrigger" type="button" data-toggle="collapse" data-target="#collapse-${index}" title="点击查看详细(参考豆瓣)...">
                  <sf:hidden path="list[${index}].subject.ID"/>
                  <label class="bookMajorDetailLabel" for="ISBN13-${index}">ISBN13：
                    <sf:input id="ISBN13-${index}" class="ISBN13" size="13" path="list[${index}].subject.ISBN13" readonly="true"/>
                  </label>
                  <label class="bookMajorDetailLabel" for="title-${index}">title：
                    <sf:input id="title-${index}" path="list[${index}].subject.title" readonly="true"/>
                  </label>
                </button>
              </div>
              <div class="col-3 adjudicate">
                <sf:radiobutton path="list[${index}].result" id="a-${index}" value="approve" class="approve"/>
                <label for="a-${index}"> approve</label>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <sf:radiobutton path="list[${index}].result" id="r-${index}" value="reject" class="reject"/>
                <label for="r-${index}"> reject</label>
              </div>
            </div>

            <div id="collapse-${index}" class="collapse" data-parent="#overallAccordion">
              <div class="">
                <div class="row"> <!--每一条记录的容器，一行-->
                  <div class="col-3 colImg">
                    <img src="" style="max-height: 14em"/>
                  </div>
                  <div class="col-5 colBookDetails">
                    <table>
                    </table>
                  </div>
                  <div class="col-4 reason" style="display: none;">
                    <label for="reason">驳回原因：<sf:textarea path="list[${index}].subject.remarks" id="reason" style="width: 100%; height: 11em;" disabled="true"/></label>
                  </div>
                </div>
              </div>
            </div>

          </div>

        </div>
      </c:forEach>

      <button class="btn btn-primary m-3" type="submit">提交审核结果</button>
    </sf:form>

    <sys:paginationNav pageInfo="${pageInfo}" filterStr=""/>
  </c:if>
</div>


<script>
    "use strict";

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

    //collapse内的明细信息 只加载一次，用one()...
    $(".collapseTrigger").one("click", function () {
        var $listItemRow = $(this).parents("div.listItemRow");
        $.ajax({
            url: urlForepart + $(".ISBN13", $listItemRow).val(),
            type: "get",
            dataType: "jsonp",
            success: function (data) {
                $(".colImg img", $listItemRow).attr("src", data.images.small);
                for (var key in textualBookDetailKeywords) {
                    if (data.hasOwnProperty(textualBookDetailKeywords[key].jsonProperty)) {
                        $(".colBookDetails table", $listItemRow).append("<tr><th>" +
                            textualBookDetailKeywords[key].thLabel + ": </th><td>" +
                            data[textualBookDetailKeywords[key].jsonProperty] + "</td></tr>");
                    }
                }
            }
        });
    });

    //若驳回，显示驳回原因textarea...
    $(".reject").click(function (event) {
        var $listItemRow = $(this).parents("div.listItemRow");
                                                            //【可以考虑利用js change event，jquery也有】
                                                            // 是否该用.is(":checked")【扣】见jQuery in action P82
        if(!$(this).attr("checked")) {                      //试出来的...【猜】radio button单击事件触发时，attr("checked")是获取得先前状态，当前单击对其状态的影响还未体现...错！If the attribute exists as a built-in property but it’s a Boolean, the value isn’t synchronized. 见jQuery in action P82
            $(".reason", $listItemRow).show();
            $(".reason textarea", $listItemRow).attr("disabled", false);
        }
        $(".collapse", $listItemRow).collapse("show");  //bootstrap的...遵守使用accordion的路子...
    });

    $(".approve").click(function () {
        var $listItemRow = $(this).parents("div.listItemRow");
        if(!$(this).attr("checked")) {
            $(".reason", $listItemRow).hide();
            $(".reason textarea", $listItemRow).attr("disabled", true); //【鸣】unsuccessful的form element在submit时是不会传值的...

        }
    });

    //设置分页单页条数
    $("#rowsSelect").change(function () {
        location.href = location.href.split("?")[0] + "?rows=" + $(this).val();
    });
</script>
</body>
</html>
