<%--@elvariable id="procInstc" type="com.ackerley.library.modules.priorBookCircu.entity.PBCProcInstc"--%>
<%--@elvariable id="bookshelfList" type="java.util.List<com.ackerley.library.modules.sys.entity.Bookshelf>"--%>
<!DOCTYPE html>
<html>
<head>
  <title>建议采编 表单</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <%@ include file="/WEB-INF/jsp/views/zTree_head.jsp" %>
  <style type="text/css">
    .form-group {
      width: 100%;
    }
    .error {/*这段在global.css也有，为何就是不起作用？【扣】妥协下，先在这里加了...*/
      border-color: red;
      color: red;
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>
<div class="btn-group">
  <button id="queryDouban" class="btn btn-success" title="注意❕<br/>当前已填入的<br/>相关书目信息<br/>将被覆盖..."
          data-toggle="tooltip" data-placement="bottom" data-html="true">参考豆瓣 填入基本信息</button>
  <%--<button id="submitAndContinue" class="btn btn-dark">提交编目结果 并继续编目下一条</button>--%>
  <button id="submitAndList" class="btn btn-primary" type="submit" form="catalogingForm">提交编目结果 并返回待编目列表</button>
</div>
<hr/>

<form id="catalogingForm" name="theOnlyForm" method="post">

  <input type="hidden" name="ID" value="${procInstc.ID}"/>
  <input type="hidden" name="stage" value="${fns:getConfig("PCS_IN_LIB")}"/><!--【扣】EL中，有获取static field的办法么？-->
  <div class="cataloging">
    <div class="row no-gutters biblioInfo">
      <div class="col-7">
        <div class="row no-gutters">
          <div class="form-group">
            <label for="hrefCoverImg" class="">封图链接:</label>
            <div class="input-group mb-3">
              <input id="hrefCoverImg" name="hrefCoverImg" type="text" class="form-control" />
              <div class="input-group-append">
                <button type="button" id="coverImgPreviewBtn" class="btn btn-outline-secondary" data-toggle="modal" data-target="#coverImgPreview">预览</button>
              </div>
            </div>
            <div class="modal fade" id="coverImgPreview" tabindex="-1">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <button type="button" class="badge badge-danger" data-dismiss="modal"><span style="width: 2em; ">&times;</span></button>
                  </div>
                  <div class="modal-content"><img id="coverImg" src="" style="width: 100%;"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="row no-gutters">
          <div class="form-group">
            <label for="hrefDouban" class="">豆瓣书目链接:</label>
            <input id="hrefDouban" name="hrefDouban" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters">
          <div class="form-group">
            <label for="ISBN13" class="">ISBN13:</label>
            <input id="ISBN13" name="ISBN13" type="text" class="form-control" value="${procInstc.ISBN13}" readonly/>
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="cls_notationAndName" class="">书目分类:</label>
            <div class="input-group mb-3">
              <input type="hidden" name="cls.ID" id="cls_ID"/>
              <input type="text" id="cls_notationAndName" name="cls.name" class="form-control" placeholder="请点选书目分类 → " disabled/>
              <div class="input-group-append">
                <button type="button" id="modalBiblioClsTreeSelectTrigger" class="btn btn-secondary" data-toggle="modal" data-target="#modalBiblioClsTreeSelect">🔍</button>
              </div>
            </div>
            <div class="modal fade" id="modalBiblioClsTreeSelect" tabindex="-1">
              <div class="modal-dialog">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title">请点选书目分类</h5>
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
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="title" class="">标题:</label>
            <input id="title" name="title" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="subtitle" class="">子标题:</label>
            <input id="subtitle" name="subtitle" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="originalTitle" class="">原标题:</label>
            <input id="originalTitle" name="originalTitle" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="authors" class="">作者:</label>
            <input id="authors" name="authors" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="authorsIntro" class="">作者简介:</label>
            <textarea id="authorsIntro" name="authorsIntro" class="form-control" style="height: 10em;" ></textarea>
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="translators" class="">译者:</label>
            <input id="translators" name="translators" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="pages" class="">页数:</label>
            <input id="pages" name="pages" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="summary" class="">书目简介:</label>
            <input id="summary" name="summary" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="publisher" class="">出版商:</label>
            <input id="publisher" name="publisher" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="pubDate" class="">出版日期:</label>
            <input id="pubDate" name="pubDate" type="date" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="price" class="">价格<span style="color: gold;">(只能输入纯数值)</span>:</label>
            <input id="price" name="price" type="text" class="form-control" />
          </div>
        </div>
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="priceUnit" class="">价格单位:</label>
            <div id="priceUnit" class="btn-group btn-group-toggle form-control" data-toggle="buttons">
              <label for="RMB" class="btn btn-light active"><input id="RMB" name="priceUnit" value="RMB" type="radio" class="" checked/>RMB</label>
              <label for="USD" class="btn btn-light"><input id="USD" name="priceUnit" value="USD" type="radio" class="" />USD</label>
              <label for="GBP" class="btn btn-light"><input id="GBP" name="priceUnit" value="GBP" type="radio" class="" />GBP</label>
              <label for="JPY" class="btn btn-light"><input id="JPY" name="priceUnit" value="JPY" type="radio" class="" />JPY</label>
            </div>
          </div>
        </div>
      </div>
      <div class="row no-gutters bookInfo">

      </div>
    </div>

    <div class="row no-gutters bookInfo">
      <div class="col-7">
        <div class="row no-gutters" >
          <div class="form-group">
            <label for="bookshelfID" class="">上架位置：</label>
            <select id="bookshelfID" name="bookshelfID" type="text" class="form-control">
              <option value="">------请选择书架(也需前、后端校验)------</option>
              <c:forEach var="opt" items="${bookshelfList}">
                <option value="${opt.ID}">${opt.label}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="row no-gutters" >
          <fieldset class="pl-2 pr-2 border rounded">
            <legend class="pl-1 pr-1" style="width: inherit;">条形码</legend>
            <div class="form-row">
              <div class="col-8 mb-3">
                <label for="startingBarCode" class="">起始编号：</label>
                <div class="input-group mb-3">
                  <div class="input-group-prepend"><span class="input-group-text">${procInstc.ISBN13}</span></div>
                  <input id="startingBarCode" name="startingBarCode" type="number" class="form-control" size="3" value="1"/>
                </div>
              </div>
              <div class="col-4 mb-3">
                <label for="quantity" class="">数量：</label>
                <input id="quantity" name="quantity" type="number" class="form-control" size="3" value="${inboundQty}"/>
              </div>
            </div>
          </fieldset>
        </div>
      </div>
    </div>

  </div>
</form>

<script>
    "use strict";
    //
    $("#queryDouban").click(function () {
        var bookDetailKeys = [
            {objectField:"hrefCoverImg", dataProperty:"images.small"},
            {objectField:"hrefDouban", dataProperty:"alt"},
            {objectField:"title", dataProperty:"title"},
            {objectField:"subtitle", dataProperty:"subtitle"},
            {objectField:"originalTitle", dataProperty:"origin_title"},
            {objectField:"authors", dataProperty:"author"},
            {objectField:"authorsIntro", dataProperty:"author_intro"},
            {objectField:"translators", dataProperty:"translator"},
            {objectField:"pages", dataProperty:"pages"},
            {objectField:"summary", dataProperty:"summary"},
            {objectField:"publisher", dataProperty:"publisher"},
            {objectField:"pubDate", dataProperty:"pubdate"},
            {objectField:"price", dataProperty:"price"}
            ];

        var url = "https://api.douban.com/v2/book/isbn/" + $("#ISBN13").val();
        $.ajax({
            url: url,
            dataType: 'JSONP', /*【扣】用JSON就是没返回...一定得用JSONP实现跨域请求？*/
            success: function (data) {
                for (var key in bookDetailKeys) {
                    if(bookDetailKeys[key].objectField == "pubDate") {  //日期型data string特别处理...单位数月、日前添"0"...
                        var originalDateString = eval("data."+bookDetailKeys[key].dataProperty),
                            processedDateString = "";
                        if(originalDateString !== null && originalDateString.length > 0) {
                            var yearMonthDate = originalDateString.split("-");
                            if (yearMonthDate.length > 0) {     //废话，但为了同下面保持代码结构一致...
                                processedDateString = processedDateString.concat(yearMonthDate[0]);
                            }
                            if (yearMonthDate.length > 1) {     //有 月 数据...
                                processedDateString = processedDateString.concat("-");
                                if (yearMonthDate[1].length === 1) {
                                    processedDateString = processedDateString.concat("0");
                                }
                                processedDateString = processedDateString.concat(yearMonthDate[1]);
                            } else {
                                processedDateString = processedDateString.concat("-01");  //缺 月 数据，则补齐，"-00"试过不行，就...
                            }
                            if (yearMonthDate.length > 2) {     //有 日 数据...
                                processedDateString = processedDateString.concat("-");
                                if (yearMonthDate[2].length === 1) {
                                    processedDateString = processedDateString.concat("0");
                                }
                                processedDateString = processedDateString.concat(yearMonthDate[2]);
                            } else {
                                processedDateString = processedDateString.concat("-01");  //缺 日 数据，则补齐，"-00"试过不行，就...
                            }

                            $("#" + bookDetailKeys[key].objectField).val(processedDateString);
                        }
                    } else {
                        $("#" + bookDetailKeys[key].objectField).val(eval("data."+bookDetailKeys[key].dataProperty));
                    }
                }
                console.log($("#pubDate").val());
            }
        });
    });

    //提交
    <%--$("#submitAndContinue").click(function () {--%>
        <%--location.href = "${ctx}/inLibCircu/ctlg/doCataloging?" + $("#catalogingForm").serialize() + "&continue="; //使用jquery validator校验不能这样啊？--%>
    <%--});--%>
    <%--$("#submitAndList").click(function () {--%>
        <%--&lt;%&ndash;location.href = "${ctx}/inLibCircu/ctlg/doCataloging?" + $("#catalogingForm").serialize() + "&index=";&ndash;%&gt;--%>
        <%--document.forms["theOnlyForm"].submit();   //js submit也不行？绕过jquery validator了？--%>
    <%--});--%>

    //book cover href 预览测试按钮点击
    $("#coverImgPreviewBtn").click(function () {
        $("#coverImg").attr("src", $("#hrefCoverImg").val());
    });

    //初始化bootstrap内的tooltip...[]
    $(function () {
        $('[data-toggle="tooltip"]').tooltip();
      /*Tooltips are opt-in for performance reasons, so you must initialize them yourself.*/
    });

    //bootstrap modal内zTree控件 选定biblio classification...
    $("#modalBiblioClsTreeSelectTrigger").click(function () {
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
    //biblioCls tree选择好了，点击确认提交后的逻辑：将所选项的 id传给hidden的id input，name传给name
    $("#biblioClsConfirm").click(function () {
        var tree = $.fn.zTree.getZTreeObj("biblioClsTree");
        $("#cls_ID").val(tree.getSelectedNodes()[0].id);
        $("#cls_notationAndName").val(tree.getSelectedNodes()[0].name);

        $("#modalBiblioClsTreeSelect").modal("hide");
    });

    //前端校验
    $("#catalogingForm").validate({
        rules: {
            hrefCoverImg: "url",
            hrefDouban: "url",
            "cls.name": "required",   //【扣】不能校验disabled input？
            "cls.ID": "required",     //【扣】不能校验hidden input？
            title: "required",
            authors: "required",
            pages: "digits",
            pubDate: "dateISO",
            price: {required: true, number: true},
            priceUnit: "required",
            bookshelfID: "required",
            startingBarCode: "required",
            quantity: "required"
        }
    });
</script>
</body>
</html>