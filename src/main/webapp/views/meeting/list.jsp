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

<display:table pagesize="5" class="displaytag" name="meetings" requestURI="${requestURI}" id="row">

	<jstl:choose>
	  <jstl:when test="${row.type eq 'PUBLIC'}">
	    <jstl:set var="color" value="SkyBlue"/>
	  </jstl:when>
	  <jstl:when test="${row.type eq 'PRIVATE'}">
	    <jstl:set var="color" value="SandyBrown"/>
	  </jstl:when>
	  <jstl:otherwise>
	    <jstl:set var="color" value="inherit"/>
	  </jstl:otherwise>
	</jstl:choose>

	<spring:message code="meeting.name" var="name" />
	<display:column property="name" title="${name}" style="background-color: ${color};"/>
	
	<spring:message code="meeting.description" var="description" />
	<display:column property="description" title="${description}" style="background-color: ${color};"/>
	
	<spring:message code="meeting.type" var="type" />
	<display:column property="type" title="${type}" style="background-color: ${color};" />
	
	<spring:message code="meeting.date" var="date" />
	<display:column title="${date}" style="background-color: ${color};">
			<fmt:formatDate var="format" value="${row.date}" pattern="dd/MM/yyyy HH:mm" />
			<jstl:out value="${format}" />
	</display:column>
	
	<spring:message code="meeting.headquarter" var="headquarter" />
	<display:column property="headquarter.name" title="${headquarter}" style="background-color: ${color};" />
	
	<spring:message code="meeting.edit" var="editH" />
	<display:column title="${editH}" style="background-color: ${color};">
		<acme:button url="meeting/member/edit.do?meetingId=${row.id}" code="button.edit" />
	</display:column>
	
	<spring:message code="meeting.delete" var="deleteH" />
	<display:column title="${deleteH}" style="background-color: ${color};">
		<acme:button url="meeting/member/delete.do?meetingId=${row.id}" code="button.delete" />	
	</display:column>
			
</display:table>

<acme:button url="meeting/member/create.do" code="button.create" />