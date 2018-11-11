<%--@elvariable id="_1stLevelMenu" type="com.ackerley.library.modules.sys.entity.Menu"--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>管理员</title>
  <%@ include file="/WEB-INF/jsp/views/common_head.jsp" %>
  <style type="text/css">
    .nav-pills .active span {
      color: white;
    }
  </style>

</head>
<body style="margin: 0;/* background-color: #aaaaaa;*/">
<nav id="navbar" class="navbar navbar-expand-sm navbar-light bg-light"> <!--fixed-top已取消-->
  <a class="navbar-brand" href="<%--#homepage--%>"><span id="brandName">${fns:getConfig("brandName")}</span></a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarToggle">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="navbarToggle">
    <ul class="navbar-nav nav-pills mr-auto"><!--【鸣】mr-auto 将右侧的单位'推'往右边-->
      <c:set var="firstMenuItemFlag" value="true"/><%--【扣】c:set 怎么知道true是boolean的true而不是String的true？是否String literal要写成"\"literal\""这种？--%>
      <c:forEach var="_1stLevelMenu" items="${fns:getMenuList()}">
        <c:if test="${_1stLevelMenu.parentID == '0' && _1stLevelMenu.presenceFlag == true}"><%--【扣】href="javascript:"这样写有必要么？--%>
          <li class="nav-item rounded">
            <a class="nav-link rounded px-3 ${firstMenuItemFlag ? " active" : ""}" href="javascript:"
               data-href="${ctx}/sys/menu/tree?parentID=${_1stLevelMenu.ID}" data-id="${_1stLevelMenu.ID}">
              <span>${_1stLevelMenu.name}</span>
            </a>
          </li>

          <c:if test="${firstMenuItemFlag}">
            <c:set var="firstMenuItemFlag" value="false"/><%--【bootstrap css】仅首个一级菜单项是active的pill...--%>
          </c:if>
        </c:if>

      </c:forEach>
    </ul>
    <div class="mx-3"><a href="${ctx}/logout" class="btn btn-danger" title="登出">退出登录</a></div>
  </div>
</nav>

<div id="main" class="" style="">
  <div class="row no-gutters" style="height: 100%; "><!--no-gutters去掉内容的padding...【扣】height为非inherited属性，但可通过百分比形式传导？-->
    <div id="subMenus" class="" style="width: 200px; overflow: hidden;"></div>
    <div id="openClose" class="bg-light" style="width: 11px; height: 100%;"><div id="openCloseIconContainer" style="position: relative; top: 49%; width: 100%;">&lt;</div></div>
    <div class="col" style="">
      <iframe id="iframeTheOne" name="contentFrame" class="" frameborder="no" style="width: 100%; height: 100%; overflow: scroll;"></iframe>
    </div>
    <%--a的target指向iframe的name，而非id!--%>
  </div>
</div>

<script type="text/javascript">
    "use strict";
    $(function () {
        //绑 页header的一级菜单项click handler
        $("#navbar a.nav-link").click(function () {
            //焦点重设...
            $("#navbar a.nav-link").removeClass("active");
            $(this).addClass("active");
            var correspondingSubMenuSlctr = "#subMenusFor-" + $(this).data("id");

            if ($(correspondingSubMenuSlctr).length == 1) {  //能选到(length > 0)，说明本一级菜单项下的subMenus已load且append，只不过可能被hide...
                $("#subMenus .accordion").hide();
                $(correspondingSubMenuSlctr).show();
            }
            else {                                         //说明此受击一级菜单项下的二级subMenus还未存在于dom tree...
                //ajax获取受click一级菜单项下的subMenus
                $.get($(this).data("href"), function (data) {//【扣】牢记，异步，立即return？
                    //未登陆或超时的检查，待...
                    $("#subMenus").append(data);

                    //绑 二级菜单项event handler？单就accordion效果的话，靠遵守bootstrap规则，享受bootstrap提供的js而无需自己写了...其他效果如上下箭头(icon-chevron-)等等还需自己实现...

                    //绑 三级菜单项的click handler UI逻辑，active属性的显隐...
                    $(correspondingSubMenuSlctr + " .nav-link").click(function () {
                        $(".nav-link", "#subMenus").removeClass("active");    //去掉所有三级菜单项焦点，
                        $(this).addClass("active");
                    });

                    $("#subMenus .accordion").hide();
                    $(correspondingSubMenuSlctr).show();

                });
            }
        });//一级菜单项的click handler到此结束...

        //首次访问sysIndex时...
        $("#navbar a.nav-link:first").click();

        //openClose对左侧二三级菜单区域subMenus的显隐控制
        $("#openClose").click(function () {
            if($("#subMenus").css("width") == "0px") {
                $("#subMenus").animate({width: 200, opacity: 1}, "fast");
                $("#openCloseIconContainer").html("&lt;")
            } else {
                $("#subMenus").animate({width: 0, opacity: 0}, "fast");
                $("#openCloseIconContainer").html("&gt;")
            }
        });
        $("#openClose").hover(function () {
            if($("#subMenus").css("width") == "0px") {
                $("#subMenus").animate({width: 200, opacity: 1}, "fast");
                $("#openCloseIconContainer").html("&lt;")
            }
        });

        //为了达到右侧只有一条vertical scrollbar的效果：即iframe有vertical scrollbar，而document body没有的，
        // 所以 要保证body的总height永远正好为viewport的height，
        // 又由于header的height定了，即 要保证其余部分(#main)的height随viewport的height变动而同步变动...【扣】虽然思路如此，但始终一团乱麻，勉强乱试出，【待】基础学习...真实屏幕分辨率px与...
        $("#main").height(window.innerHeight - $("#navbar").height() - 17);    <!--【扣】不减去16px的话，就是差一点，就是有vertical scrollbar？另：底部一条窄边是什么？-->
//        console.log("$(window).height(): " + $(window).height());
//        console.log("window.innerHeight: " + window.innerHeight);
//        console.log("window.outerHeight: " + window.outerHeight);
//        console.log("-------------------------------------------");
        $(window).resize(function () {
            $("#main").height(window.innerHeight - $("#navbar").height() - 17);<!--【扣】不减去16px的话，就是差一点，就是有vertical scrollbar？另：底部一条窄边是什么？-->
//            console.log("navbarContainer height: " + $("#navbarContainer").height());
//            console.log("$(window).height(): " + $(window).height());
//            console.log("window.innerHeight: " + window.innerHeight);
//            console.log("window.outerHeight: " + window.outerHeight);
//            console.log("-------------------------------------------");
        });

    });//document的ready handler到此结束...
</script>
</body>
</html>
