<!DOCTYPE html>
<html>
<head>
  <title>还书 登记</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    .myShadow {
      box-shadow: 0 14px 42px 7px #E7E7E7;
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>

<div class="container-fluid" style="height: 500px;">
  <div class="scanner row no-gutters mt-5 mb-3" style="position: relative; top: 20%;">
    <div class="input-group myShadow">
      <input id="bookBarCodeInput" name="bookBarCode" placeholder="图书条形码 → " class="form-control"/>
      <div class="input-group-append">
        <button id="bookBarCodeScanner" type="button" class="btn btn-outline-secondary">⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸⎸</button>
      </div>
    </div>
  </div>
</div>



<script>
    "use strict";
    //
    $("#bookBarCodeScanner").click(function () {
        location.href = "${ctx}/inLibCircu/returnReg/" + encodeURIComponent($("#bookBarCodeInput").val());
    });

</script>
</body>
</html>