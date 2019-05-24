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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form name="leave" id="leave" action="${actionURL}" method="POST" >

<jstl:choose>
<jstl:when test="${fn:length(members) == 0}">
	<spring:message code="association.leave.lastMember" />
</jstl:when>
<jstl:when test="${role != 'PRESIDENT'}">
	<spring:message code="association.leave.standarMember" />
</jstl:when>
<jstl:otherwise>
	<spring:message code="association.selectMember" />:<br />
	<select name="memberId">
		<jstl:forEach var="m" items="${members}">
			<option value="${m.id}"><jstl:out value="${m.name}"/></option>
		</jstl:forEach>
	</select>
</jstl:otherwise>
</jstl:choose>
<br /><br />
	<spring:message code="button.leave" var="leave" />
	<input type="submit" name="leave" value="${leave}" />
	
	<button type="button" onclick="javascript: relativeRedir('association/member/show.do')" >
		<spring:message code="button.cancel" />
	</button>
</form>