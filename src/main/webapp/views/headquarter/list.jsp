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

<display:table pagesize="5" class="displaytag" name="headquarters" requestURI="${requestURI}" id="row">

	<spring:message code="headquarter.name" var="name" />
	<display:column property="name" title="${name}" />
	
	<spring:message code="headquarter.address" var="address" />
	<display:column property="address" title="${address}" />
	
	<spring:message code="headquarter.photos" var="photos" />
	<display:column title="${photos}" >
	<jstl:forEach items="${row.photos}" var="photo">
		<img src="<jstl:out value="${photo}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	
	<spring:message code="headquarter.edit" var="editH" />
	<display:column title="${editH}" >
		<acme:button url="headquarter/member/edit.do?headquarterId=${row.id}" code="button.edit" />
	</display:column>
	
</display:table>

<acme:button url="headquarter/member/create.do" code="button.create" />