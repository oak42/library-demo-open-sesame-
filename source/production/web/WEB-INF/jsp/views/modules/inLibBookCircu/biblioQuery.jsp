<%--@elvariable id="biblioList" type="java.util.List<com.ackerley.library.modules.inLibBookCircu.entity.Biblio>"--%>
<%--@elvariable id="pageInfo" type="com.github.pagehelper.PageInfo<com.ackerley.library.modules.inLibBookCircu.entity.Biblio>"--%>
<html>
<head>
  <title>馆藏书目查询</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <%@ include file="/WEB-INF/jsp/views/zTree_head.jsp" %>
  <style type="text/css">
    #listHeaderPlaceholder, #listHeaderFixedTop {
      height: 2.6rem;
      /*vertical-align: middle;没作用？*/
    }
    .myShadow {
      box-shadow: 0 0 7px 2px #E7E7E7;
    }
    .biblioField {
      /*white-space: normal;!*【鸣】bootstrap的.btn有white-space: nowrap，导致不会折行;*!*/
      overflow: hidden;
    }
    th {
      text-align: center;
    }
  </style>
</head>
<body style="overflow-y: scroll;">
  <div id="listHeaderPlaceholder" class="">
    <%--【扣】bootstrap的model，若放在listHeaderFixedTop内层，显示时处于backdrop之下，why？--%>
    <div class="modal fade" id="modalBiblioClsTreeSelect" tabindex="-1">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">点选图书分类</h5>
            <button type="button" class="close" data-dismiss="modal"><span>&times;</span></button>
          </div>
          <div class="modal-body">
            <ul id="biblioClsTree" class="ztree"></ul>
          </div>
          <div class="modal-footer">
            <button type="button" id="biblioClsConfirm" class="btn btn-primary" style="display: none;">确认</button>
          </div>
        </div>
      </div>
    </div>

    <div id="listHeaderFixedTop" class="fixed-top bg-light">
      <div class="row no-gutters listHeaderRow" >
        <sf:form cssClass="form-inline" modelAttribute="filterBiblio" cssStyle="width: 100%;">
        <div class="rounded myShadow" style="width: 14%; margin-right:0.3%;">
          <sf:input path="ISBN13" placeholder="ISBN13:" cssClass="form-control" cssStyle="width: 100%;"/>
        </div>
        <div class="input-group rounded myShadow" style="width: 14%; margin-right:0.3%;">
          <sf:input path="cls.name" placeholder="分类:" readonly="true" cssClass="form-control bg-light"/><sf:hidden path="cls.ID"/>
          <div class="input-group-append">
            <button id="biblioClsSel" type="button" class="btn btn-secondary" data-toggle="modal" data-target="#modalBiblioClsTreeSelect">🔍</button><%--【扣】type="button" 若不加，modal一闪而过，效果好似model所在form直接submit了，与event传播有关的吧？--%>
          </div>
        </div>

        <div class="rounded myShadow" style="width: 37%; margin-right:0.3%;">
          <sf:input path="title" placeholder="书名/标题:" cssClass="form-control" cssStyle="width: 100%;"/>
        </div>
        <div class="rounded myShadow" style="width: 14%; margin-right:0.3%;">
          <sf:input path="authors" placeholder="作者:" cssClass="form-control" cssStyle="width: 100%;"/>
        </div>
        <div class="rounded myShadow" style="width: 14%; margin-right:0.3%;">
          <sf:input path="translators" placeholder="译者:" cssClass="form-control" cssStyle="width: 100%;"/>
        </div>
        <div class="rounded myShadow" style="width: 5.4%;">
          <button type="submit" class="btn btn-outline-success" name="doFilter" value="doFilter" style="width: 100%;">筛选</button>
        </div>
        </sf:form>
      </div>
    </div>
  </div>

  <sys:message content="${message}"/>
  
  <c:if test="${pageInfo != null && pageInfo.list.size() == 0}">
    <sys:message content="此筛选条件下，未查询到任何结果..." type="warning"/>
  </c:if>
  <c:if test="${pageInfo != null && pageInfo.list.size() > 0}">
  <div id="overallAccordion" style="">
    <c:forEach var="biblio" items="${pageInfo.list}" varStatus="sts">
    <div class="row no-gutters listItemRow border rounded">
      <div class="col-12">
        <div class="btn btn-light px-1 collapseTrigger " data-toggle="collapse" data-target="#collapse-${sts.index}" title="点击查看本书馆藏详细" data-biblioID="${biblio.ID}" style="width: 100%;">
          <div class="row no-gutters">
            <div class="biblioField" style="width: 14%; margin-right:0.3%;"><p>${biblio.ISBN13}</p></div>
            <div class="biblioField" style="width: 14%; margin-right:0.3%;"><p>${biblio.cls.notation} : ${biblio.cls.name}</p></div>
            <div class="biblioField" style="width: 37%; margin-right:0.3%;"><p>${biblio.title}<c:if test="${biblio.subtitle != null && biblio.subtitle != ''}"> : ${biblio.subtitle}</c:if></p></div>
            <div class="biblioField" style="width: 14%; margin-right:0.3%;"><p>${biblio.authors}</p></div>
            <div class="biblioField" style="width: 14%; margin-right:0.3%;"><p>${biblio.translators}</p></div>
            <div class="" style="width: 5.4%;"></div>
          </div>
        </div>

        <div id="collapse-${sts.index}" class="collapse" data-parent="#overallAccordion">
          <div class="row no-gutters"><%--【本来想row直接和collapse并为一个的，试下来其下的col们将不会在一行，遂又拆开】--%>
            <div class="col-4 p-5 border-left border-right">
              <img src="${ctxStatic}${biblio.hrefCoverImg}" style="width: 100%;">
            </div>
          </div>
        </div>

      </div>

    </div>
    </c:forEach>
  </div>

    <sys:paginationNav pageInfo="${pageInfo}" filterStr="${filterStr}"/><!--【扣】能有获取、使用 相对地址 的 方法么？【扣】只能用js比较好？【扣】//TODO 写一个通用反射工具类ReflectUtil，一method将pojo能toString为non-null field=value，...的形式，或许可参考spring的databinder-->

  </c:if>
  

<script type="text/template" id="booksLocationInfo">
  <div class="col-4 p-5 border-left border-right">
    <table class="table table-sm table-striped">
      <thead>
      <tr>
        <th>#</th><th>位置</th>
      </tr>
      </thead>
      <tbody>
      {{#bookList}}
      <tr>
        <th>{{seqNbr}}</th><td>{{location}}</td>
      </tr>
      {{/bookList}}
      </tbody>
    </table>
  </div>
</script>
<script>
    "use strict";

    //bootstrap modal内zTree控件 选定biblio classification...第二次用了，应当考虑学习模仿jeesite将复用部分封为tag？
    $("#biblioClsSel").click(function () {
        $.ajax({
            url: "${ctx}/sys/biblioCls/jsonTreeData", <%--${ctx}加上，防止换application context名呢...--%>
            success: function (data) {
                var setting = {
                    view: {
                        selectedMulti: false,
                        showIcon: false
                    },
                    data: {
                        simpleData: {enable: true}
                    },
                    callback: {
                        onClick: function (event, treeId, treeNode, clickFlag) {
                            $("#biblioClsConfirm").show();  /*【扣】 tree加载成功后，用户有点选后才show*/
                        }
                    }
                };
                $.fn.zTree.init($("#biblioClsTree"), setting, data);
            }
        });
    });
    //biblioCls tree item选择好了，点击确认提交后的逻辑：将所选项的 id传给hidden的id input，name传给name
    $("#biblioClsConfirm").click(function () {
        var tree = $.fn.zTree.getZTreeObj("biblioClsTree");
        $("#cls\\.ID").val(tree.getSelectedNodes()[0].id);      //input id、name都是Spring form自己生成的，
        $("#cls\\.name").val(tree.getSelectedNodes()[0].name);  //input id、name都是Spring form自己生成的，

        $("#modalBiblioClsTreeSelect").modal("hide");
    });

    //collapse内的本biblio下的book明细信息，只加载一次，用one()...
    $(".collapseTrigger").one("click", function () {
        var $listItemRow = $(this).parents(".listItemRow");
        $.ajax({
            url: "${ctx}/inLibCircu/biblioQuery/books?biblioID=" + encodeURIComponent($(this).data("biblioid")),  //【扣】biblioID变成了biblioid，为何？
            type: "get",
            dataType: "json",
            success: function (data) {
                console.log(JSON.stringify(data));
                for(var i = 0; i < data.length; i++) {
                    data[i].seqNbr = i + 1;
                    data[i].location = function () {
                        if(this.state == "in_lib_book_state_in_stock") {
                            return this.bookshelf.location + "，" + this.bookshelf.label;
                        }
                        if(this.state == "in_lib_book_state_shelving") {
                            return "在库待上架";
                        }
                        if(this.state == "in_lib_book_state_lent_out") {
                            return "借出";
                        }
                        return "";
                    };
                }
                $("[id|='collapse'] > .row", $listItemRow).append(Mustache.render($("#booksLocationInfo").html(), {bookList : data}));
            }
        });
    });

    //accordion header部分的 多行-单行 切换，跟随其对应的collapse的动作
    $(".collapse").on("show.bs.collapse", function () { //This event fires immediately when the show instance method is called.
        var $listItemRow = $(this).parents(".listItemRow");
        $(".biblioField", $listItemRow).css("white-space", "normal");
    });
    $(".collapse").on("hidden.bs.collapse", function () { //This event is fired when a collapse element has been hidden from the user (will wait for CSS transitions to complete).
        var $listItemRow = $(this).parents(".listItemRow");
        $(".biblioField", $listItemRow).css("white-space", "nowrap");
    });

</script>
</body>
</html>
