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

<display:table pagesize="5" class="displaytag" name="scores" requestURI="${requestURI}" id="row">

	<spring:message code="score.ranking" var="ranking" />
	<display:column property="ranking" title="${ranking}" />
	
	<spring:message code="score.comments" var="comments" />
	<display:column property="comments" title="${comments}" />
	
	<spring:message code="score.photos" var="photos" />
	<display:column title="${photos}" >
	<jstl:forEach items="${row.photos}" var="photo">
		<img src="<jstl:out value="${photo}"/>" width="200px" height="200px" />
	</jstl:forEach>
	</display:column>
	
	<spring:message code="score.attachments" var="attachments" />
	<display:column property="attachments" title="${attachments}" />
	
</display:table>

<acme:button url="score/visitor/create.do?activityId=${activity.id}" code="button.create" />
<acme:button url="activity/visitor/list.do" code="button.back" />