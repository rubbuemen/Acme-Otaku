<%--
 * index.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" name="products" requestURI="${requestURI}" id="row">

	<spring:message code="product.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="product.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="product.price" var="price" />
	<display:column title="${price}">
			<fmt:formatNumber var="priceVat" type="number" maxFractionDigits="2" value="${row.price + row.price * (vatPercentage / 100)}"/>
			<jstl:set var="priceVat" value="${priceVat} (${vatPercentage} %)"/>
			<jstl:out value="${priceVat}" />
	</display:column>
	
	<spring:message code="product.photo" var="photo" />
	<display:column title="${photo}">
		<img src="<jstl:out value='${row.photo}' />"  width="200px" height="200px" />
	</display:column>

</display:table>

<acme:button url="stand/listGeneric.do" code="button.back" />