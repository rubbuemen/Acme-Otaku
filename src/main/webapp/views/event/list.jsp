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

<display:table pagesize="5" class="displaytag" name="events" requestURI="${requestURI}" id="row">

	<spring:message code="event.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="event.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="event.address" var="address" />
	<display:column property="address" title="${address}" />
	
	<spring:message code="event.moment" var="moment" />
	<display:column title="${moment}">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="event.tags" var="tags" />
	<display:column property="tags" title="${tags}" />
	
	<spring:message code="event.days" var="daysH" />
		<display:column title="${daysH}" >
			<acme:button url="day/member/list.do?eventId=${row.id}" code="button.show" />
		</display:column>
	
	<spring:message code="event.edit" var="editH" />
	<display:column title="${editH}" >
		<jstl:if test="${!row.isFinalMode}">
			<acme:button url="event/member/edit.do?eventId=${row.id}" code="button.edit" />
		</jstl:if>	
	</display:column>
	
	<spring:message code="event.delete" var="deleteH" />
	<display:column title="${deleteH}" >
		<jstl:if test="${!row.isFinalMode}">
			<acme:button url="event/member/delete.do?eventId=${row.id}" code="button.delete" />
		</jstl:if>	
	</display:column>
	
	<spring:message code="event.changeFinalMode" var="changeFinalModeH" />
	<display:column title="${changeFinalModeH}" >
		<jstl:choose>
			<jstl:when test="${!row.isFinalMode}">
				<acme:button url="event/member/change.do?eventId=${row.id}" code="button.change" />
			</jstl:when>
			<jstl:when test="${row.isFinalMode}">
				<spring:message code="event.isFinalMode" />
			</jstl:when>
		</jstl:choose>
	</display:column>
			
</display:table>

<acme:button url="event/member/create.do" code="button.create" />