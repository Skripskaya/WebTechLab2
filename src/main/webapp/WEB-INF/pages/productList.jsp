<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page isELIgnored="false"%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="messages"/>
<tags:master pageTitle="Gummie List">
  <fmt:setLocale value="${sessionScope.lang}"/>
  <fmt:setBundle basename="messages"/>

<jsp:useBean id="gummies" scope="request" type="java.util.List"/>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<table>

    <c:forEach var="gummie" items="${gummies}" >
    <tr>
    <td class="align-middle">
      <form id="productFormBrand" action="/" method="GET">
        <input type="hidden" name="command" value="product_details">
        <input type="hidden" name="gummie_id" value="${gummie.id}">
        <button type="submit">
            ${gummie.name}
        </button>
      </form>
    </td>
    <td class="align-middle">
          <c:choose>
            <c:when test="${not empty sessionScope.login}">
                <form action="/productlist" method="post">
                  <input type="hidden" name="command" value="cart_add">
            </c:when>
            <c:otherwise>
                <form action="/productlist" method="get">
                  <input type="hidden" name="command" value="authorisation">
            </c:otherwise>
          </c:choose>
        <input type="hidden" name="id" value="${phone.id}">
        <input type="hidden" name="page_type" value="productList">
        <input type="number" name="quantity" id="quantity${gummie.id}" min="1" required>
        <button class="btn btn-lg btn-outline-light text-dark border-dark float-right" type="submit" style="font-size: 14px"><fmt:message key="button_add" /></button>
      </form>
    </td>
    </tr>
    </c:forEach>
</table>
</tags:master>