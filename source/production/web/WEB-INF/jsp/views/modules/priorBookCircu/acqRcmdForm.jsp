<!DOCTYPE html>
<html>
<head>
  <title>建议采编 表单</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    .col, .col-2 {

    }

    .bookDetails input, .bookDetails textarea {
      width: 100%;
    }

    input[readonly], textarea[readonly] {
      border: none;
    }
  </style>
</head>
<body>
<sys:message content="${message}"/>
<div class="btn-group">
  <button id="queryDouban" class="btn btn-success">凭正确的13位ISBN号，联网(www.douban.com)参考待建议采编书目基本信息...</button>
  <button id="submitAndContinue" class="btn btn-dark" hidden>提交建议 并继续录入建议</button>
  <button id="submitAndList" class="btn btn-primary" hidden>提交建议 并返回已建议列表</button>
</div>
<hr/>

<div class="row no-gutters bookDetails">
  <div class="col-8 no-gutters">
    <div class="row no-gutters">
      <div class="col-2"><label for="ISBN13" class="">ISBN13:</label></div>
      <div class="col"><input id="ISBN13" name="ISBN13" type="text" class="form-control serializeOnSubmit" placeholder="请输入13位ISBN号" size="13" style="width: 10em"/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="title" class="">标题:</label></div>
      <div class="col"><input id="title" name="title" type="text" class="serializeOnSubmit" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="subtitle" class="">子标题:</label></div>
      <div class="col"><input id="subtitle" name="subtitle" type="text" class="" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="series" class="">系列:</label></div>
      <div class="col"><input id="series" name="series" type="text" class="" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="author" class="">作者:</label></div>
      <div class="col"><input id="author" name="author" type="text" class="" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="author_intro" class="">作者简介:</label></div>
      <div class="col"><textarea id="author_intro" name="author_intro" class="" style="height: 10em;" readonly></textarea>
      </div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="translator" class="">译者:</label></div>
      <div class="col"><input id="translator" name="translator" type="text" class="" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="pubdate" class="">出版日期:</label></div>
      <div class="col"><input id="pubdate" name="pubdate" type="text" class="" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="publisher" class="">出版商:</label></div>
      <div class="col"><input id="publisher" name="publisher" type="text" class="" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="pages" class="">页数:</label></div>
      <div class="col"><input id="pages" name="pages" type="text" class="" readonly/></div>
    </div>
    <div class="row no-gutters" hidden>
      <div class="col-2"><label for="price" class="">价格:</label></div>
      <div class="col"><input id="price" name="price" type="text" class="" readonly/></div>
    </div>

  </div>
  <div class="col-4 no-gutters">
    <img id="img" src="">
  </div>
</div>

<!--<pre style="width:100%; height: 100%"></pre>-->

<script>
    "use strict";
    $(function () {
        var bookDetailKeys = ["title", "subtitle", "series", "author", "author_intro", "translator", "pubdate", "publisher", "pages", "price"];
        $("#queryDouban").click(function () {
            var url = "https://api.douban.com/v2/book/isbn/" + $("input#ISBN13").val();
            $.ajax({
                url: url,
                dataType: 'JSONP', /*【扣】用JSON就是没返回...一定得用JSONP实现跨域请求？*/
                success: function (data) {
                    $("#img").attr("src", data.images.small);

                    for (var key in bookDetailKeys) {
                        if (data.hasOwnProperty(bookDetailKeys[key]) && data[bookDetailKeys[key]] != "") {
                            $("#" + bookDetailKeys[key]).val(data[bookDetailKeys[key]]).parent().parent().removeAttr("hidden");
                        } else {
                            $("#" + bookDetailKeys[key]).parent().parent().attr("hidden", "");
                        }
                    }
//                    var formated = JSON.stringify(data, null, 4);
//                    $("pre").html(formated);
                    $("#submitAndContinue").removeAttr("hidden").click(function () {
                        location.href = "${ctx}/priorCircu/rcmd/save?" + $(".serializeOnSubmit").serialize() + "&continue=";
                    });
                    $("#submitAndList").removeAttr("hidden").click(function () {
                        location.href = "${ctx}/priorCircu/rcmd/save?" + $(".serializeOnSubmit").serialize() + "&list=";
                    });
                }
            });
        });


/*  非form内的form control能利用jquery validator么？
        $(".bookDetails").validate({
            rules: {
                ISBN13: {
                    required: true,
                    rangelength: [13,13]
                }
            }
        });
*/

    });
</script>
</body>
</html>