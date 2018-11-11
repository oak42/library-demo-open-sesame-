<%--@elvariable id="toPurOrderItemList" type="java.util.List<com.ackerley.library.modules.priorBookCircu.entity.PBCPurOrderItem>"--%>
<!--【待】每一条待选item的右上角可以做个×，点击用js remove或hide(并disable，保证不传值)-->
<html>
<head>
  <title>采购单 生成</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    #header, #footer {
      height: 2.5rem;
    }

    table tr {
      height: 5rem;
    }

    input[type="radio"].in:checked ~ .qtyGroup {
      display: inherit !important;
    }

  </style>
</head>
<body>
<sys:message content="${message}"/>
<c:if test="${toPurOrderItemList.size() == 0}">
  <sys:message content="已无更多待选采购项，请审核员、复审员查看待审核项 并及时审核！" type="warning"/>
  <a class="btn btn-primary" href="${ctx}/priorCircu/purOrder/">返回</a>
</c:if>
<c:if test="${toPurOrderItemList.size() > 0}">
<form method="post" action="save">

  <table class="table border">
    <thead>
    <tr id="header" class="" style="background-color: #c82333;">
      <th>ISBN13</th>
      <th>标题</th>
      <th style="width: 28em;">是否纳入</th>
    </tr>
    </thead>
    <tbody>
      <c:forEach var="index" begin="0" end="${toPurOrderItemList.size() - 1}"><!--【】要做成 物理分页 的话这里逻辑上略复杂点；做 逻辑(内存)分页？先不做-->
        <tr class="itemRow">
          <td>
            <input class="isbn13" type="hidden" name="list[${index}].ISBN13" value="${toPurOrderItemList[index].ISBN13}" disabled>
              ${toPurOrderItemList[index].ISBN13}
          </td>
          <td>
              ${toPurOrderItemList[index].title}
          </td>
          <td>
            <div class="adjudicate">
              <label class="badge badge-danger" style="font-size: inherit;">out
                <input name="adjudicate-${index}" type="radio" class="out" />
              </label>
              <label class="badge badge-success" style="font-size: inherit;">in
                <input name="adjudicate-${index}" type="radio" class="in"/>
                <span class="qtyGroup" style="display: none">&nbsp;&nbsp;
                <button class="badge badge-secondary decrement">-</button><!--【扣】【严重】为何此button会触发submit？-->
                <input type="number" name="list[${index}].orderQty" class="qty" placeholder="拟采购数量" min="1" style="width: 7rem;" disabled/>
                <button class="badge badge-secondary increment">+</button><!--【扣】【严重】为何此button会触发submit？-->
              </span>
              </label>
            </div>
          </td>
        </tr>
      </c:forEach>
    </tbody>
    <tfoot class="" style="background-color: gray;">
    <tr id="footer">
      <td colspan="3">
        <button id="purOrderGen" type="button" class="btn btn-primary" data-toggle="modal" data-target="#purOrderInfo" style="display: none;">生成采购单</button>
      </td>
    </tr>
    </tfoot>
  </table>


  <!-- Modal -->
  <div class="modal fade" id="purOrderInfo" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">采购单 信息填写</h5>
<%--      <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>      --%>
        </div>
        <div class="modal-body">
          <table>
            <tr>
              <th>采购单名：</th><td><input type="text" id="name" name="name"/></td>
            </tr>
            <tr>
              <th>备注：</th><td><input type="text" id="remarks" name="remarks"/></td>
            </tr>
          </table>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-dismiss="modal">取消并返回</button>
          <button type="submit" class="btn btn-primary">保存采购单</button>
        </div>
      </div>
    </div>
  </div>


</form>
</c:if>
<script>
    "use strict";
    $(function () {

        //若选择out，则 不显示 采购数量输入框...
        $(".out").click(function () {
            var $adjudicate = $(this).parents("div.adjudicate");
            var $itemRow = $(this).parents("tr.itemRow");
            if(!$(this).hasClass("checked")) {                        //If the attribute exists as a built-in property but it’s a Boolean, the value isn’t synchronized. jQuery in action P82，所以改用自己定义的状态标记，比如class...
                $(this).addClass("checked");
                $(".in", $adjudicate).removeClass("checked");

                $(".isbn13", $itemRow).attr("disabled", true);    //【鸣】unsuccessful的form element在submit时是不会传值的...
                $(".qty", $adjudicate).attr("disabled", true);    //【鸣】unsuccessful的form element在submit时是不会传值的...
            }
        });
        $(".in").click(function () {
            var $adjudicate = $(this).parents("div.adjudicate");
            var $itemRow = $(this).parents("tr.itemRow");
            if(!$(this).hasClass("checked")) {
                $(this).addClass("checked");
                $(".out", $adjudicate).removeClass("checked");

                $(".qty", $adjudicate).attr("disabled", false);
                $(".isbn13", $itemRow).attr("disabled", false);
//            $(".isbn13", $itemRow).removeAttr("disabled");
//            $(".qty", $adjudicate).removeAttr("disabled");
            }
        });

        //【扣】【严重】为何点击这 - + 也会导致form submit？？？？？？？神奇......
        $("button.increment").click(function () {
            var $qtyGroup = $(this).parents(".qtyGroup");
            $(".qty", $qtyGroup).val(parseInt($(".qty", $qtyGroup).val()) + 1);
        });
        $("button.decrement").click(function () {
            var $qtyGroup = $(this).parents(".qtyGroup");
            $(".qty", $qtyGroup).val(parseInt($(".qty", $qtyGroup).val()) - 1);
        });


        //若无任何纳入(无in单选按钮选中)，则 不显示 生成采购单purOrderGen按钮...
        $(".in, .out").change(function () {
            if($(".in:checked").length === 0) {
               $("#purOrderGen").hide();
            } else {
                $("#purOrderGen").show();
            }
        });
    });



</script>
</body>
</html>
