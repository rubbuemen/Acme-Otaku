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

<display:table pagesize="5" class="displaytag" name="activities" requestURI="${requestURI}" id="row">

	<spring:message code="activity.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="activity.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="activity.photo" var="photo" />
	<display:column title="${photo}">
	<jstl:if test="${not empty row.photo}">
		<img src="<jstl:out value='${row.photo}' />"  width="200px" height="200px" />
	</jstl:if>	
	</display:column>
	
	<spring:message code="activity.rules" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="activity.deadline" var="deadline" />
	<display:column title="${deadline}">
			<fmt:formatDate var="format" value="${row.deadline}" pattern="dd/MM/yyyy HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="activity.category" var="category" />
	<display:column title="${category}">
	<jstl:if test="${language eq 'en'}">
		<jstl:out value="${row.category.nameEnglish}" />
	</jstl:if>
	<jstl:if test="${language eq 'es'}">
		<jstl:out value="${row.category.nameSpanish}" />
	</jstl:if>
	</display:column>
	
	<security:authorize access="hasRole('VISITOR')">
			<jstl:if test="${row.isFinished}">
				<spring:message code="activity.scores" var="scoresH" />
				<display:column title="${scoresH}" >
					<acme:button url="score/visitor/list.do?activityId=${row.id}" code="button.show" />
				</display:column>
			</jstl:if>
	</security:authorize>
	
	<security:authorize access="hasRole('MEMBER')">
		<spring:message code="activity.edit" var="editH" />
		<display:column title="${editH}" >
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="activity/member/edit.do?activityId=${row.id}" code="button.edit" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="activity.delete" var="deleteH" />
		<display:column title="${deleteH}" >
			<jstl:if test="${!row.isFinalMode}">
				<acme:button url="activity/member/delete.do?activityId=${row.id}" code="button.delete" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="activity.changeFinalMode" var="changeFinalModeH" />
		<display:column title="${changeFinalModeH}" >
			<jstl:choose>
				<jstl:when test="${!row.isFinalMode}">
					<acme:button url="activity/member/change.do?activityId=${row.id}" code="button.change" />
				</jstl:when>
				<jstl:when test="${row.isFinalMode}">
					<spring:message code="activity.isFinalMode" />
				</jstl:when>
			</jstl:choose>
		</display:column>
		
		<spring:message code="activity.finish" var="finishH" />
		<display:column title="${finishH}" >
			<jstl:choose>
				<jstl:when test="${row.isFinalMode and !row.isFinished}">
					<acme:button url="activity/member/finish.do?activityId=${row.id}" code="button.finish" />
				</jstl:when>
				<jstl:when test="${row.isFinished}">
					<spring:message code="activity.isFinished" />
				</jstl:when>
			</jstl:choose>
		</display:column>
	</security:authorize>
			
</display:table>

<security:authorize access="hasRole('MEMBER')">
	<acme:button url="activity/member/create.do" code="button.create" />
</security:authorize>

<security:authorize access="hasRole('VISITOR')">
	<acme:button url="event/visitor/list.do" code="button.back" />
</security:authorize>