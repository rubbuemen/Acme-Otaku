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

<form:form action="${actionURL}" modelAttribute="report">
	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<acme:textarea code="report.text" path="text" placeholder="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean at auctor massa"/>
	<br />
	
	<acme:textbox code="report.score" path="score" placeholder="N" type="number" min="1" max="5" />
	<br />
	
	<acme:textarea code="report.photos" path="photos" placeholder="http://LoremIpsum.com, http://LoremIpsum.com, http://LoremIpsum.com, ..."/>
	<br />
	
	<acme:textbox code="report.summary" path="summary" placeholder="Lorem Ipsum"/>
	<br />
	
	<jstl:if test="${report.id == 0}">
		<acme:select items="${stands}" itemLabel="brandName" code="report.stand" path="stand"/>
		<br />
	</jstl:if>
	
	<jstl:choose>
		<jstl:when test="${report.id == 0}">
			<acme:submit name="save" code="button.register" />
		</jstl:when>
		<jstl:otherwise>
			<acme:submit name="save" code="button.save" />
		</jstl:otherwise>
	</jstl:choose>
	
	<acme:cancel url="report/visitor/list.do" code="button.cancel" />
</form:form>