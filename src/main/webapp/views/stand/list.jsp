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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" name="stands" requestURI="${requestURI}" id="row">

	<spring:message code="stand.type" var="type" />
	<display:column property="type" title="${type}" />
	
	<spring:message code="stand.brandName" var="brandName" />
	<display:column property="brandName" title="${brandName}" />
	
	<spring:message code="stand.banner" var="banner" />
	<display:column title="${banner}">
		<img src="<jstl:out value='${row.banner}' />" width="200px" height="200px" />
	</display:column>
	
	<spring:message code="stand.events" var="events" />
	<display:column title="${events}" >
	<ul>
		<jstl:forEach items="${row.events}" var="event">
			<li><jstl:out value="${event.name}"/></li>
		</jstl:forEach>
	</ul>
	</display:column>
	
	<spring:message code="stand.products" var="products" />
	<display:column title="${products}">
		<acme:button url="product/seller/list.do?standId=${row.id}" code="button.show" />
	</display:column>

	<spring:message code="stand.edit" var="editH" />
	<display:column title="${editH}" >
		<jstl:if test="${empty row.events}">
			<acme:button url="stand/seller/edit.do?standId=${row.id}" code="button.edit" />
		</jstl:if>
	</display:column>
	
	<spring:message code="stand.delete" var="deleteH" />
	<display:column title="${deleteH}" >
		<jstl:if test="${empty row.events}">
			<acme:button url="stand/seller/delete.do?standId=${row.id}" code="button.delete" />	
		</jstl:if>
	</display:column>
			
</display:table>

<acme:button url="stand/seller/create.do" code="button.create" />