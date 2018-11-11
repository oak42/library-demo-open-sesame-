<%@ tag language="java" pageEncoding="UTF-8" %><!--【扣，待】不加这句，本页的'成功'、'注意'、'警告'、'失败'、等 中文字符的编码就是有问题，在IDE中设置好像也没用...-->

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容" %>
<%@ attribute name="type" type="java.lang.String" description="消息类型：bootstrap alert类型，如danger、warning、success、info...可依赖简单的关键词(成功、失败)自动决定type" %>

<c:if test="${not empty content}">

  <c:if test="${not empty type}"><c:set var="contentType" value="${type}"/></c:if>
  <c:if test="${empty type}">
    <c:set var="contentType" value="info"/>
    <c:if test="${fn:indexOf(content, '成功') ne -1 }"><c:set var="contentType" value="success"/></c:if>
    <c:if test="${fn:indexOf(content, '注意') ne -1 or
                  fn:indexOf(content, '警告') ne -1}"><c:set var="contentType" value="warning"/></c:if>
    <c:if test="${fn:indexOf(content, '失败') ne -1 or
                  fn:indexOf(content, '错误') ne -1 }"><c:set var="contentType" value="danger"/></c:if>
  </c:if>

  <div id="messageBox" class="alert alert-${contentType}">
      ${content}
    <button type="button" class="close" data-dismiss="alert">&times;</button><%-- &times; 即 × --%>
  </div>

</c:if>

