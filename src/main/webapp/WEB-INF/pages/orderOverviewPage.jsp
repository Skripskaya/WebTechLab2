<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="order" scope="request" type="com.bsuir.skripskaya.gummie.model.entities.order.Order"/>
<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<tags:master pageTitle="Order overview">
  <p></p>
  <div id="statusMessage" class="container"><span></span></div>
  <div class="container">
    <h1><fmt:message key="order_overview_title" /></h1>
    <h2><fmt:message key="order_status" />: ${order.status}</h2>
  </div>
  <div class="panel"></div>
  <div class="row">
    <div class="col-2"></div>

    <div class="col-8">
      <c:if test="${empty order}">
        <h1 class="text-center"><fmt:message key="order_not_found" /></h1>
      </c:if>
      <c:if test="${not empty order}">

        <table class="table table-bordered text-center">
          <thead>
          <tr class="bg-light">
            <td>
              <fmt:message key="item_brand" />
            </td>
            <td>
              <fmt:message key="item_model" />
            </td>
            <td>
              <fmt:message key="item_Color" />
            </td>
            <td>
              <fmt:message key="item_Display_Size" />
            </td>
            <td>
              <fmt:message key="item_quantity" />
            </td>
            <td>
              <fmt:message key="item.price" />
            </td>
          </tr>
          </thead>
          <c:forEach var="item" items="${order.orderItems}">
            <tr>
              <td class="align-middle">
                  ${item.gummie.name}
              </td>
              <td class="align-middle">
                  ${item.quantity}
              </td>
            </tr>
          </c:forEach>
          <tr>
            <td class="border-white"></td><td class="border-white"></td><td class="border-white"></td><td class="border-white"></td>
            <td>
              <fmt:message key="order_delivery" />
            </td>
            <td>
                ${order.Price}
            </td>
          </tr>
        </table>
        <p>${order.additionalInformation}</p>
      </c:if>
    </div>

    <div class="col-2"></div>
  </div>
</tags:master>