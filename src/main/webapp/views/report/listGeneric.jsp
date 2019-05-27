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

<display:table pagesize="5" class="displaytag" name="reports" requestURI="${requestURI}" id="row">

	<spring:message code="report.text" var="text" />
	<display:column property="text" title="${text}" />
	
	<spring:message code="report.score" var="score" />
	<display:column property="score" title="${score}" />
	
	<spring:message code="report.photos" var="photos" />
	<display:column title="${photos}" >
	<jstl:forEach items="${row.photos}" var="photo">
		<img src="<jstl:out value="${photo}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	
	<spring:message code="report.moment" var="moment" />
	<display:column title="${moment}">
			<fmt:formatDate var="format" value="${row.moment}" pattern="dd/MM/yyyy HH:mm" />
			<jstl:out value="${format}" />
	</display:column>

	<spring:message code="report.summary" var="summary" />
	<display:column property="summary" title="${summary}" />
	
	<spring:message code="report.stand" var="stand" />
	<display:column property="stand.brandName" title="${stand}" />
</display:table>
