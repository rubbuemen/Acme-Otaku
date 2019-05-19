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

<display:table pagesize="5" class="displaytag" name="entidadsss" requestURI="${requestURI}" id="row">

	<spring:message code="entidad.atributo" var="atributo" />
	<display:column property="atributo" title="${atributo}" />
	
	
	<spring:message code="entidad.edit" var="editH" />
	<display:column title="${editH}" >
		<acme:button url="entidad/rol/edit.do?entidadId=${row.id}" code="button.edit" />
	</display:column>
	
	<spring:message code="entidad.delete" var="deleteH" />
	<display:column title="${deleteH}" >
		<acme:button url="entidad/rol/delete.do?entidadId=${row.id}" code="button.delete" />	
	</display:column>
			
</display:table>

<acme:button url="entidad/rol/create.do" code="button.create" />