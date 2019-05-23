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

<display:table pagesize="5" class="displaytag" name="members" requestURI="${requestURI}" id="row">

	<spring:message code="actor.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="actor.middleName" var="middleName" />
	<display:column property="middleName" title="${middleName}" />
	
	<spring:message code="actor.surname" var="surname" />
	<display:column property="surname" title="${surname}" />
	
	<spring:message code="actor.email" var="email" />
	<display:column property="email" title="${email}" />
	
	<spring:message code="actor.phoneNumber" var="phoneNumber" />
	<display:column property="phoneNumber" title="${phoneNumber}" />
	
	<spring:message code="member.role" var="role" />
	<display:column property="role" title="${role}" />
	
	<spring:message code="member.changeRol" var="changeH" />
	<display:column title="${changeH}" >
		<acme:button url="member/member/edit.do?memberId=${row.id}" code="button.edit" />
	</display:column>
			
</display:table>