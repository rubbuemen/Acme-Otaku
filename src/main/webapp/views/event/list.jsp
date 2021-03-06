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

<security:authorize access="hasRole('VISITOR')">
	<form name="singleKeyWord" id="singleKeyWord" action="event/visitor/list.do" method="POST" >
		<spring:message code="event.searchBySingleKeyWord" />: 
		<input type="text" name="singleKeyWord" required>
			
		<spring:message code="button.search" var="search" />
		<input type="submit" name="search" value="${search}" />
	</form>
	<br />
</security:authorize>

<display:table pagesize="5" class="displaytag" name="events" requestURI="${requestURI}" id="row">

	<spring:message code="event.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="event.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="event.address" var="address" />
	<display:column property="address" title="${address}" />
	
	<spring:message code="event.moment" var="moment" />
	<display:column title="${moment}">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/yyyy HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="event.tags" var="tags" />
	<display:column property="tags" title="${tags}" />
	
	<security:authorize access="hasRole('MEMBER')">
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
	</security:authorize>
	<security:authorize access="hasRole('VISITOR')">
		<spring:message code="event.days" var="daysH" />
		<display:column title="${daysH}" >
			<acme:button url="day/visitor/list.do?eventId=${row.id}" code="button.show" />
		</display:column>
		<spring:message code="event.activities" var="activitiesH" />
		<display:column title="${activitiesH}" >
			<acme:button url="activity/visitor/list.do?eventId=${row.id}" code="button.show" />
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('SELLER')">
		<spring:message code="event.days" var="daysH" />
		<display:column title="${daysH}" >
			<acme:button url="day/seller/list.do?eventId=${row.id}" code="button.show" />
		</display:column>
	</security:authorize>
	
	<spring:message code="event.sponsorship" var="sponsorship" />
	<display:column title="${sponsorship}" >
		<jstl:if test="${randomSponsorship.containsKey(row)}">
			<jstl:set var="banner" value="${randomSponsorship.get(row).banner}"/>
			<img src="<jstl:out value='${banner}'/>" width="200px" height="100px" />
		</jstl:if>
	</display:column>
			
</display:table>

<security:authorize access="hasRole('MEMBER')">
	<acme:button url="event/member/create.do" code="button.create" />
</security:authorize>