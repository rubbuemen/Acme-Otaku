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

<display:table pagesize="5" class="displaytag" name="days" requestURI="${requestURI}" id="row">

	<spring:message code="day.date" var="date" />
	<display:column title="${date}">
			<fmt:formatDate var="format" value="${row.date}" pattern="dd/MM/yyyy" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="day.price" var="price" />
	<display:column title="${price}">
			<fmt:formatNumber var="priceVat" type="number" maxFractionDigits="2" value="${row.price + row.price * (vatPercentage / 100)}"/>
			<jstl:set var="priceVat" value="${priceVat} (${vatPercentage} %)"/>
			<jstl:out value="${priceVat}" />
	</display:column>
	
	<security:authorize access="hasRole('MEMBER')">
		<jstl:if test="${!event.isFinalMode}">
			<spring:message code="day.editH" var="editH" />
			<display:column title="${editH}">
				<acme:button url="day/member/edit.do?eventId=${event.id}&dayId=${row.id}" code="button.edit" />
			</display:column>
			
			<spring:message code="day.deleteH" var="deleteH" />
			<display:column title="${deleteH}">
				<acme:button url="day/member/delete.do?eventId=${event.id}&dayId=${row.id}" code="button.delete" />
			</display:column>
		</jstl:if>
	</security:authorize>
			
</display:table>

<security:authorize access="hasRole('MEMBER')">
	<jstl:if test="${!event.isFinalMode}">
		<acme:button url="day/member/create.do?eventId=${event.id}" code="button.create" />
	</jstl:if>
	<acme:button url="event/member/list.do" code="button.back" />
</security:authorize>

<security:authorize access="hasRole('VISITOR')">
	<acme:button url="event/visitor/list.do" code="button.back" />
</security:authorize>

<security:authorize access="hasRole('SELLER')">
	<acme:button url="event/seller/list.do" code="button.back" />
</security:authorize>

