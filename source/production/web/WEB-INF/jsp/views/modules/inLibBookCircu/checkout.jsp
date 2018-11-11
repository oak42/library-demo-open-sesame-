<%--@elvariable id="libCrd" type="com.ackerley.library.modules.sys.entity.LibCrd"--%>
<%--@elvariable id="borrower" type="com.ackerley.library.modules.sys.entity.User"--%>
<%--@elvariable id="unpaidOverdueFineList" type="java.util.List<com.ackerley.library.modules.inLibBookCircu.entity.OverdueFine>"--%>
<%--@elvariable id="outstandingRecordList" type="java.util.List<com.ackerley.library.modules.inLibBookCircu.entity.BorrowReturnRecord>"--%>
<!DOCTYPE html>
<html>
<head>
  <title>借出 登记</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <%@ include file="/WEB-INF/jsp/views/zTree_head.jsp" %>
  <style type="text/css">
    .myShadow {
      box-shadow: 0 7px 21px 3px #E7E7E7;
    }

    th {
      box-shadow: inset 0 0.14em 14px 0 #E7E7E7;
      text-align: center;
    }

    .abolishOneBook {
      color: #c82333;
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>
<div class="container-fluid">
  <div class="row">
    <div class="col-6 border-right rounded borrowerInfo">

      <div class="scanner row no-gutters mt-3 mb-3">
        <div class="input-group myShadow">
          <input id="libCrdBarCodeInput" name="libCrdBarCode" placeholder="借书证条形码 → " class="form-control"/>
          <div class="input-group-append">
            <button id="libCrdBarCodeScanner" type="button" class="btn btn-outline-secondary">⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸</button>
          </div>
        </div>
      </div>
      <%--借阅者基本信息--%>
      <c:if test="${libCrd != null && borrower != null}">
      <input type="hidden" id="barCodePresent" name="barCodePrev" value="${libCrd.barCode}"/>               <%--...--%>
      <div class="row no-gutters border-top border-bottom mt-3 mb-3 myShadow generalLibCardAndUserInfo">
        <div class="col-3"><img src="${ctxStatic}${libCrd.hrefOwnerPhoto}" style="width: 100%;"></div>
        <div class="col-8 ml-1">
          <div class="row">
            <div class="col-5">姓名：</div>
            <div class="col-7">${borrower.realName}</div>
          </div>
          <div class="row">
            <div class="col-5">性别：</div>
            <div class="col-7">${borrower.gender}</div>
          </div>
          <div class="row">
            <div class="col-5">年龄：</div>
            <div class="col-7">${borrower.age}</div>
          </div>
        </div>
      </div>
      </c:if>
      <%--未缴罚金信息，有则显示--%>
      <c:if test="${unpaidOverdueFineList != null && unpaidOverdueFineList.size() > 0}">
      <div class="row no-gutters border-top border-bottom mt-3 myShadow unpaidFines">
        <table class="table table-sm mb-0">
          <thead>
            <tr><th colspan="99">未缴罚金 合计<span id="finesTotalAmount">${finesTotalAmount}</span>元&nbsp;&nbsp;<button id="payFines" class="btn btn-sm btn-warning">缴纳</button></th></tr>
            <tr><th>#</th><th>所系书名</th><th>产生罚金(元)</th></tr>
          </thead>
          <tbody>
            <c:forEach var="fine" items="${unpaidOverdueFineList}" varStatus="sts">
              <tr class="fineRow">
                <th>
                  ${sts.index + 1}
                  <input type="hidden" class="recordID" name="ID" value="${fine.borrowAndReturnRecordID}">
                  <input type="hidden" class="fineID" name="ID" value="${fine.ID}">
                </th>
                <td class="bookTitle"></td>
                <td class="fineAmount"></td>
              </tr>
            </c:forEach>
          </tbody>
        </table>

      </div>
      </c:if>
      <!--当前未还图书列表-->
      <c:if test="${outstandingRecordList != null && outstandingRecordList.size() > 0}">
      <div class="row no-gutters border-top border-bottom mt-3 myShadow outstandingRecordList">
        <table class="table table-sm mb-0">
          <thead>
            <tr><th colspan="99">当前在借未还</th></tr>
            <tr><th>#</th><th>书名</th><th>起借日</th><th>续借否</th><th>应还日</th><th>逾期否</th></tr>
          </thead>
          <tbody>
          <c:forEach var="record" items="${outstandingRecordList}" varStatus="sts">
            <tr class="outstandingBookRow">
              <th>${sts.index + 1}<input type="hidden" class="recordID" name="ID" value="${record.ID}"></th>
              <td class="title"></td>
              <td class="borrowTime"></td>
              <td class="isRenewed"></td>
              <td class="dueDay"></td>
              <td class="isOverdue" style="color: #c82333;"></td>
            </tr>
          </c:forEach>
          </tbody>
        </table>

      </div>
      </c:if>

    </div>

<!--右侧借书登记区域-->
    <div class="col-6 border-left rounded" id="booksToCheckOutContainer" style="display: none;">

      <div class="scanner row no-gutters mt-3 mb-3">
        <div class="input-group myShadow">
          <input id="inLibBookBarCodeInput" name="bookBarCode" placeholder="图书条形码 → " class="form-control"/>
          <div class="input-group-append">
            <button id="inLibBookBarCodeScanner" type="button" class="btn btn-outline-secondary">⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸</button>
          </div>
        </div>
      </div>

      <div id="booksToCheckOutStub"></div>
      <div style="text-align: right;"><button type="button" id="confirmCheckingOut" class="btn btn-primary" disabled>提交 办理登记借阅</button></div>
    </div>

  </div>
</div>

<script id="bookToCheckOutTemplate" type="text/template">
  <div class="row no-gutters border-bottom mt-3 mb-3 shadow bookToCheckOut myShadow">
    <input type="hidden" class="bookBarCode" name="bookBarCode" value="{{barCode}}">
    <div class="col-3 bookCover"><img src="${ctxStatic}{{biblio.hrefCoverImg}}" style="width: 100%;"/></div>
    <div class="col-8 ml-1 bookTextualInfo">
      <div class="row"><div class="col">{{biblio.ISBN13}}<button type="button" class="close abolishOneBook">&times;</button></div></div>
      <div class="row"><div class="col">{{biblio.title}}{{#biblio.subtitle}} : {{biblio.subtitle}}{{/biblio.subtitle}}</div></div>
      <div class="row"><div class="col">{{biblio.authors}}</div></div>
      <div class="row"><div class="col">{{biblio.publisher}}</div></div>
    </div>
  </div>
</script>
<script>
  "use strict";
  //提交lib card信息(模拟刷lib card bar code，手输bar code，点击按钮提交)
  $("#libCrdBarCodeScanner").click(function () {
      location.href = "${ctx}/inLibCircu/checkoutReg/switchLibCrd?" + $("#barCodePresent").serialize() + "&" + $("#libCrdBarCodeInput").serialize();
  });

  //无unpaidFines时，右侧booksToCheckOutContainer才显示...(即 无未缴罚金时，才提供新借access)
  $(function () {
      if($("#barCodePresent").length !== 0 && $(".unpaidFines").length === 0) {
          $("#booksToCheckOutContainer").show();
      }
  });

  //交罚金
  $("#payFines").click(function () {
      $.ajax({
          url: "${ctx}/inLibCircu/checkoutReg/payFines/" + encodeURIComponent($("#barCodePresent").val()) + "/" + encodeURIComponent($("#finesTotalAmount").text()) + "/",
          dataType: "json",
          success: function (data) {
              if(data.success == true) {
                  $(".unpaidFines").remove();
                  $("#booksToCheckOutContainer").show();
              } else if(data.success == false) {
                  alert(data+"缴罚金错误...");
              }
          },
          error: function (data) {
              alert(JSON.stringify(data));
          }
      });
  });

  //ajax加载未交罚金详细信息
  $(".fineRow").each(function () {
      var $this = $(this);
      $.ajax({
          url: "${ctx}/inLibCircu/borrowNRetrunRecord?" + $(".recordID", $this).serialize(),
          dataType: "json",
          success: function (record) {
              $.ajax({
                  url: "${ctx}/inLibCircu/InLibBook?ID=" + record.bookID,
                  dataType: "json",
                  success: function (book) {
                      $(".bookTitle", $this).html(book.biblio.title);
                  }
              });
          }
      });
      $.ajax({
          url: "${ctx}/inLibCircu/overdueFine?" + $(".fineID", $this).serialize(),
          dataType: "json",
          success: function (fine) {
              $(".fineAmount", $this).html(fine.amount);
          }
      });
  });

  //ajax加载未还图书(outstanding book)的详细借阅记录信息...【【待】date、time的时区与GMT格林尼治平太阳时间其中逻辑还不清楚，①jdk的②js的】关于8小时时间差...
  $(".outstandingBookRow").each(function () {
      var $this = $(this);
      $.ajax({
          url: "${ctx}/inLibCircu/borrowNRetrunRecord?" + $(".recordID", this).serialize(),
          dataType: "json",
          success: function (record) {
//              console.log(JSON.stringify(record));  //////////////////////////////debug//////////////////////////////////
              $.ajax({
                  url: "${ctx}/inLibCircu/InLibBook?ID=" + record.bookID,
                  dataType: "json",
                  success: function (book) {
//                      console.log(JSON.stringify(book));  /////////////////////////////debug/////////////////////////////////
                      $(".title", $this).html(book.biblio.title);
                  }
              });
//              console.log(record.borrowTime);
              var borrowTime = new Date(record.borrowTime);
              $(".borrowTime", $this).html(borrowTime.toLocaleDateString());
              //因为未启用BorrowReturnRecord的isOverdue【需要用到定时计划任务】，前端逾期与否的认定算法应与后端保持一致...
              var timeLimit = Number("${overdueTimeLimit}");  //外借时限。若未办理续借，则就是设定的超期天数；若已办理续借，则为超期天数+续借天数...
              if(record.isRenewed) {
                  $(".isRenewed", $this).html("已办续借");
                  timeLimit += Number("${renewTimeLimit}");
              }
              $(".dueDay", $this).html(new Date(Number(record.borrowTime)+Number(timeLimit*1000*60*60*24)).toLocaleDateString());
//              var overdue = Math.floor(Date.now()/(1000*60*60*24))-Math.floor(record.borrowTime/(1000*60*60*24)) - timeLimit;   可能由于时区的问题，此法行不通...
              var trimmedTimeNow = new Date(new Date(Date.now()).toLocaleDateString()).getTime();   //还不清楚原理，但这样截断尾部操作(trim)以后，time都变为中午12:00...不清楚原理，但最后能消去上法的时区影响...
              var trimmedTimeBorrowTime = new Date(borrowTime.toLocaleDateString()).getTime();
              var overdue = (trimmedTimeNow-trimmedTimeBorrowTime)/(1000*60*60*24) - timeLimit;
              console.log("overdue: "+overdue);
              if(overdue > 0) {
                  $(".isOverdue", $this).html("逾期");
              }
          }
      });
  });
  
  //增添 待借出book(模拟刷inlib book bar code，手输bar code，点击按钮提交)
  $("#inLibBookBarCodeScanner").click(function () {
      $.ajax({
          url: "${ctx}/inLibCircu/checkoutReg/addOneBook/" + encodeURIComponent($("#barCodePresent").val()) + "/" + encodeURIComponent($("#inLibBookBarCodeInput").val()),
          dataType: "json",
          success: function (data) {
              if(data.success == true) {
                  $("#booksToCheckOutStub").append(Mustache.render($("#bookToCheckOutTemplate").html(), data.bookInfo));
                  if(data.booksToCheckOutCount == 1) {  //0 → 1 的转变
                      $("#confirmCheckingOut").attr("disabled", false);
                  }
              }
              if(data.success == false) {
                  alert(data.failureInfo);
              }
          }
      });
  });
  
  //去除 待借出book(×)   //【鸣】事件委托，event delegation...因为页面加载时，还未有此event target，则可将handler注册(委托)在ancestor tree node...
  $("#booksToCheckOutStub").on("click", ".abolishOneBook", function () {
      var $bookToCheckOut = $(this).parents(".bookToCheckOut");
      $.ajax({
          url: "${ctx}/inLibCircu/checkoutReg/abolishOneBook/" + encodeURIComponent($("#barCodePresent").val()) + "/" + encodeURIComponent($(".bookBarCode", $bookToCheckOut).val()),
          dataType: "json",
          success: function (data) {
              if(data.success == true) {
                  $bookToCheckOut.remove();
                  if(data.booksToCheckOutCount == 0) {  //1 → 0 的转变
                      $("#confirmCheckingOut").attr("disabled", true);
                  }
              }
          }
      });
  });

  //提交本次借出登记
  $("#confirmCheckingOut").click(function () {
      location.href = "${ctx}/inLibCircu/checkoutReg/confirmCheckingOut/" + encodeURIComponent($("#barCodePresent").val());
  });

</script>
</body>
</html>