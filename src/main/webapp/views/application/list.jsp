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

<display:table pagesize="5" class="displaytag" name="applications" requestURI="${requestURI}" id="row">

	<spring:message code="application.skills" var="skills" />
	<display:column property="skills" title="${skills}" />
	
	<spring:message code="application.reasonToJoin" var="reasonToJoin" />
	<display:column property="reasonToJoin" title="${reasonToJoin}" />
	
	<spring:message code="application.attachments" var="attachments" />
	<display:column property="attachments" title="${attachments}" />
	
	<spring:message code="application.moment" var="moment" />
	<display:column title="${moment}">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/YYYY HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="application.status" var="status" />
	<display:column property="status" title="${status}" />
	
	
	<jstl:choose>
		<jstl:when test="${memberLogged.role eq 'PRESIDENT'}">
			<spring:message code="application.decideApplication" var="decideApplication" />
			<display:column title="${decideApplication}">
				<jstl:if test="${row.status eq 'PENDING'}">
					<acme:button url="application/member/accept.do?applicationId=${row.id}" code="button.accept" />
					<acme:button url="application/member/decline.do?applicationId=${row.id}" code="button.decline" />
				</jstl:if>	
			</display:column>
		</jstl:when>
		<jstl:otherwise>
			<spring:message code="application.association" var="association" />
			<display:column property="association.name" title="${association}" />
		</jstl:otherwise>
	</jstl:choose>
</display:table>

<security:authorize access="hasRole('MEMBER')">
<jstl:if test="${memberLogged.role eq null}">
	<acme:button url="application/member/create.do" code="button.create" />
</jstl:if>
</security:authorize>