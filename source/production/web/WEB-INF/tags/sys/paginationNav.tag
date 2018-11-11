<%--耦合分页插件pagehelper，耦合bootstrap，--%>
<%@ tag language="java" pageEncoding="UTF-8" %><!--【扣，待】不加这句，本页的'成功'、'注意'、'警告'、'失败'、等 中文字符的编码就是有问题，在IDE中设置好像也没用...-->

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="pageInfo" type="com.github.pagehelper.PageInfo" required="true" description="" %>
<%--<%@ attribute name="requestURL" type="java.lang.String" required="true" description="请求路径，或包含query string" %>一般性，普适性，自动化考虑，弃用--%>
<%@ attribute name="filterStr" type="java.lang.String" required="true" description="查询对象筛选的query string，每个query parameter pair前都已带了&了" %>

<style><%--【扣】把style放在body并非best practice，但这里有点模块化的念想？--%>
  .page-item {
    font-weight: bolder;
  }
</style>

<c:if test="${pageInfo != null}"><%--【鸣】pageInfo != null 不行，可能传入空串--%>
<div id="paginationNavBar" style="text-align: center;">
  <div class="pagination my-3" style="justify-content: center;">

    <div class="page-item <c:if test='${!pageInfo.hasPreviousPage}'>disabled</c:if>">
      <a href="javascript:void('保留除分页信息外的其余原query string部分')" data-page="${pageInfo.prePage}" data-rows="${pageInfo.pageSize}" class="page-link"> 《 </a>
    </div>

    <c:if test="${pageInfo.navigateFirstPage - 1 > 0}">
    <div class="page-item">
      <a href="javascript:void('保留除分页信息外的其余原query string部分')" data-page="1" data-rows="${pageInfo.pageSize}" class="page-link">1</a><%--第一页--%>
    </div>
    </c:if>

    <c:if test="${pageInfo.navigateFirstPage -1 > 1}">
    <div>
      <span class="px-2" style="">...</span>
    </div>
    </c:if>

    <c:forEach items="${pageInfo.navigatepageNums}" var="nav">
    <c:if test="${nav == pageInfo.pageNum}">
    <div class="page-item active">
      <span class="page-link">${nav}</span>
    </div>
    </c:if>
    <c:if test="${nav != pageInfo.pageNum}">
    <div class="page-item">
      <a href="javascript:void('保留除分页信息外的其余原query string部分')" data-page="${nav}" data-rows="${pageInfo.pageSize}" class="page-link">${nav}</a>
    </div>
    </c:if>
    </c:forEach>

    <c:if test="${pageInfo.pages - pageInfo.navigateLastPage > 1}">
    <div>
      <span class="px-2" style="">...</span>
    </div>
    </c:if>

    <c:if test="${pageInfo.pages - pageInfo.navigateLastPage > 0}">
    <div class="">
      <a href="javascript:void('保留除分页信息外的其余原query string部分')" data-page="${pageInfo.pages}" data-rows="${pageInfo.pageSize}" class="page-link">${pageInfo.pages}</a><%--最后一页--%>
    </div>
    </c:if>

    <div class="page-item <c:if test='${!pageInfo.hasNextPage}'>disabled</c:if>">
      <a href="javascript:void('保留除分页信息外的其余原query string部分')" data-page="${pageInfo.nextPage}" data-rows="${pageInfo.pageSize}" class="page-link"> 》 </a>
    </div><span style="padding: 0.5em 0.75em; line-height: 1.25; color: dodgerblue;"> 共( ${pageInfo.total} )条记录 </span>

  </div>
</div>

<script><%--【扣】把script到处散落，并非best practice，但这里有点模块化的念想？--%>
  "use strict";

  $("a.page-link").click(function () {
      location.href = location.href.split("?")[0] + "?page=" + $(this).data("page") + "&rows=" + $(this).data("rows") + "${filterStr}";


  });
</script>
</c:if>

