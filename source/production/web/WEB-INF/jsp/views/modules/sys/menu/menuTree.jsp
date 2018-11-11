<%--@elvariable id="menuList" type="java.util.List<com.ackerley.library.modules.sys.entity.Menu>"--%>   <%--用户的menu total--%>
<%--@elvariable id="_2ndLvlMenu" type="com.ackerley.library.modules.sys.entity.Menu"--%>
<%--@elvariable id="_3rdLevelMenu" type="com.ackerley.library.modules.sys.entity.Menu"--%>
<div id="subMenusFor-${param.parentID}" class="accordion" style="min-height: 700px; overflow-y: auto;">
  <c:set var="menuList" value="${fns:getMenuList()}"/>
  <c:set var="first2ndLvlMenuItemFlag" value="true"/>
  <c:set var="first3rdLvlMenuItemFlag" value="true"/>
  <c:forEach var="_2ndLvlMenu" items="${menuList}">
    <c:if test="${_2ndLvlMenu.parentID == param.parentID && _2ndLvlMenu.presenceFlag == true}">
      <div class="card text-center">  <%--card的样式 + collapse的动作 = accordion【bootstrap - collapse组件 - Accordion example: Using the card component, you can extend the default collapse behavior to create an accordion.】--%>
        <div class="card-header p-0">
          <button class="btn btn-light m-0 h5" type="button" data-toggle="collapse" data-target="#collapse-${_2ndLvlMenu.ID}" style="width: 100%; height: 3rem;">${_2ndLvlMenu.name}</button>
        </div>

        <div id="collapse-${_2ndLvlMenu.ID}" class="collapse ${first2ndLvlMenuItemFlag ? "" : ""}" data-parent="#subMenus">
          <div class="card-body">
            <ul class="nav flex-column nav-pills">
              <c:forEach var="_3rdLevelMenu" items="${menuList}">
                <c:if test="${_3rdLevelMenu.parentID == _2ndLvlMenu.ID && _3rdLevelMenu.presenceFlag}">
                  <li class="nav-item rounded">
                    <a class="nav-link ${first3rdLvlMenuItemFlag ? "" : ""}" href="${ctx}${_3rdLevelMenu.href}" target="contentFrame" ><span>${_3rdLevelMenu.name}</span></a><%--【猜】空href的行为是？--%>
                  </li>
                  <c:if test="${first3rdLvlMenuItemFlag}"><c:set var="first3rdLvlMenuItemFlag" value="false"/></c:if><%--【bootstrap css】仅首个三级菜单项是active的pill...--%>
                </c:if>
              </c:forEach>
            </ul>

          </div>
        </div>
      </div>

      <c:if test="${first2ndLvlMenuItemFlag}"><c:set var="first2ndLvlMenuItemFlag" value="false"/></c:if><%--【bootstrap css】仅首个二级菜单项是show的collapse...--%>
    </c:if>

  </c:forEach>
</div>
