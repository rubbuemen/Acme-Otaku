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

<display:table pagesize="5" class="displaytag" name="enrolments" requestURI="${requestURI}" id="row">

	<spring:message code="enrolment.comments" var="comments" />
	<display:column property="comments" title="${comments}" />
	
	<spring:message code="enrolment.status" var="status" />
	<display:column property="status" title="${status}" />
	
	<spring:message code="enrolment.moment" var="moment" />
	<display:column title="${moment}">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="enrolment.decideEnrolment" var="decideEnrolment" />
		<display:column title="${decideEnrolment}">
			<jstl:if test="${row.status eq 'PENDING'}">
				<jstl:choose>
				<jstl:when test="${row.activity.deadline < currentMoment}">
					<spring:message code="enrolment.deadlineElapsed" />
				</jstl:when>
				<jstl:otherwise>
					<acme:button url="enrolment/member/accept.do?enrolmentId=${row.id}" code="button.accept" />
					<acme:button url="enrolment/member/decline.do?enrolmentId=${row.id}" code="button.decline" />
				</jstl:otherwise>
				</jstl:choose>	
			</jstl:if>
			
		</display:column>
	</security:authorize>
	
</display:table>
<security:authorize access="hasRole('VISITOR')">
	<acme:button url="enrolment/visitor/create.do" code="button.create" />
</security:authorize>