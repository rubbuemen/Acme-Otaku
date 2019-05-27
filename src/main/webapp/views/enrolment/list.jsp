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

	<jstl:choose>
	  <jstl:when test="${row.status eq 'PENDING'}">
	  	<jstl:choose>
		  	<jstl:when test="${row.activity.deadline < currentMoment}">
		  		<jstl:set var="color" value="grey"/>
		  	</jstl:when>
		  	<jstl:otherwise>
		  		<jstl:set var="color" value="yellow"/>
		  	</jstl:otherwise>
	  	</jstl:choose>
	  </jstl:when>
	  <jstl:when test="${row.status eq 'ACCEPTED'}">
	    <jstl:set var="color" value="green"/>
	  </jstl:when>
	  <jstl:when test="${row.status eq 'DECLINED'}">
	    <jstl:set var="color" value="orange"/>
	  </jstl:when>
	   <jstl:when test="${row.status eq 'CANCELLED'}">
	    <jstl:set var="color" value="red"/>
	  </jstl:when>
	  <jstl:otherwise>
	    <jstl:set var="color" value="inherit"/>
	  </jstl:otherwise>
	</jstl:choose>

	<spring:message code="enrolment.comments" var="comments" />
	<display:column property="comments" title="${comments}"  style="background-color: ${color};"/>
	
	<spring:message code="enrolment.status" var="status" />
	<display:column property="status" title="${status}"  style="background-color: ${color};"/>
	
	<spring:message code="enrolment.moment" var="moment" />
	<display:column title="${moment}" style="background-color: ${color};">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/yyyy HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="enrolment.decideEnrolment" var="decideEnrolment" />
		<display:column title="${decideEnrolment}" style="background-color: ${color};">
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
	
	<security:authorize access="hasRole('VISITOR')">
		<spring:message code="enrolment.decideEnrolment" var="decideEnrolment" />
		<display:column title="${decideEnrolment}" style="background-color: ${color};">
			<jstl:if test="${row.status eq 'PENDING' or row.status eq 'ACCEPTED'}">
				<jstl:choose>
				<jstl:when test="${row.activity.deadline < currentMoment}">
					<spring:message code="enrolment.deadlineElapsed" />
				</jstl:when>
				<jstl:otherwise>
					<acme:button url="enrolment/visitor/cancel.do?enrolmentId=${row.id}" code="button.cancel" />
				</jstl:otherwise>
				</jstl:choose>	
			</jstl:if>
		</display:column>
	</security:authorize>
	
</display:table>
<security:authorize access="hasRole('VISITOR')">
	<acme:button url="enrolment/visitor/create.do" code="button.create" />
</security:authorize>