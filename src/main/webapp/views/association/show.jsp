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

<display:table pagesize="5" class="displaytag" name="association" requestURI="${requestURI}" id="row">

	<spring:message code="association.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="association.description" var="description" />
	<display:column property="description" title="${description}" />
	
	<spring:message code="association.slogan" var="slogan" />
	<display:column property="slogan" title="${slogan}" />
	
	<spring:message code="association.logo" var="logo" />
	<display:column title="${logo}" >
		<img src="<jstl:out value="${row.logo}"/>" width="150px" height="150px" />
	</display:column>
	
	<spring:message code="association.representativeColor" var="representativeColor" />
	<display:column title="${representativeColor}" >
		<jstl:out value="${row.representativeColor}"/>
		<div style="background-color: <jstl:out value="${row.representativeColor}"/>; width: 15px; height: 15px;" ></div>
	</display:column>
	
	<jstl:if test="${memberLogged.role eq 'PRESIDENT'}">
		<spring:message code="association.edit" var="editH" />
		<display:column title="${editH}" >
			<acme:button url="association/member/edit.do?associationId=${row.id}" code="button.edit" />
		</display:column>
		
		<spring:message code="association.notAllowToJoin" var="notAllowToJoinH" />
		<display:column title="${notAllowToJoinH}" >
			<jstl:if test="${row.isAllowedToJoin}">
				<acme:button url="association/member/notAllowToJoin.do?associationId=${row.id}" code="button.notAllow" />
			</jstl:if>	
		</display:column>
		
		<spring:message code="association.allowToJoin" var="allowToJoinH" />
		<display:column title="${allowToJoinH}" >
			<jstl:if test="${!row.isAllowedToJoin}">
				<acme:button url="association/member/allowToJoin.do?associationId=${row.id}" code="button.allow" />
			</jstl:if>	
		</display:column>
	</jstl:if>
</display:table>

<jstl:if test="${memberLogged.association eq null}">
	<acme:button url="association/member/create.do" code="button.create" />
</jstl:if>
<jstl:if test="${memberLogged.association != null}">
	<acme:button url="association/member/leave.do" code="button.leave" />
</jstl:if>