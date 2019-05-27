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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" name="categories" requestURI="${requestURI}" id="row">

	<spring:message code="category.nameEnglish" var="nameEnglish" />
	<display:column property="nameEnglish" title="${nameEnglish}" />
	
	<spring:message code="category.nameSpanish" var="nameSpanish" />
	<display:column property="nameSpanish" title="${nameSpanish}" />
	
	
	<spring:message code="category.edit" var="editH" />
	<display:column title="${editH}">
		<acme:button url="category/administrator/edit.do?categoryId=${row.id}" code="button.edit" />
	</display:column>
	
	<spring:message code="category.delete" var="deleteH" />
	<jstl:set var = "categorysUsed" value = "${categoriesUsed}"/>
	<jstl:set var = "category" value = "${row}"/>
	<display:column title="${deleteH}">
		<jstl:if test="${!fn:contains(categorysUsed, category)}">
			<acme:button url="category/administrator/delete.do?categoryId=${row.id}" code="button.delete" />
		</jstl:if>
	</display:column>
			
</display:table>

<acme:button url="category/administrator/create.do" code="button.create" />