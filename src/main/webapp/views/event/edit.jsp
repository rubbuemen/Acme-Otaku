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

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${actionURL}" modelAttribute="event">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<fieldset>
		<legend><spring:message code="event.infoEvent"/></legend>
			<acme:textbox code="event.name" path="name" placeholder="Lorem Ipsum"/>
			<br />
			
			<acme:textbox code="event.description" path="description" placeholder="Lorem Ipsum"/>
			<br />
			
			<acme:textbox code="event.address" path="address" placeholder="Lorem Ipsum"/>
			<br />
			
			<acme:textarea code="event.tags" path="tags" placeholder="Lorem ipsum, Lorem ipsum, Lorem ipsum, Lorem ipsum" />
			<br />
			
			<acme:select items="${activities}" itemLabel="name" code="event.activities" path="activities" multiple="true" />
			<br />
	</fieldset>

	<jstl:choose>
		<jstl:when test="${event.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	<acme:cancel url="event/member/list.do" code="button.cancel" />
</form:form>