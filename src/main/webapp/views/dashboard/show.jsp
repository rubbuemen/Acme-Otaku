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

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC1"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryC1 == \"null\" ? 0 : avgQueryC1}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryC1 == \"null\" ? 0 : minQueryC1}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryC1 == \"null\" ? 0 : maxQueryC1}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryC1 == \"null\" ? 0 : stddevQueryC1}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC2"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryC2 == \"null\" ? 0 : avgQueryC2}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryC2 == \"null\" ? 0 : minQueryC2}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryC2 == \"null\" ? 0 : maxQueryC2}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryC2 == \"null\" ? 0 : stddevQueryC2}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC3"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryC3 == \"null\" ? 0 : avgQueryC3}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryC3 == \"null\" ? 0 : minQueryC3}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryC3 == \"null\" ? 0 : maxQueryC3}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryC3 == \"null\" ? 0 : stddevQueryC3}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC4"/></summary>
<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryC4 == \"null\" ? 0 : ratioQueryC4}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC5"/></summary>
<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryC5 == \"null\" ? 0 : ratioQueryC5}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC6"/></summary>
<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryC6 == \"null\" ? 0 : ratioQueryC6}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC7"/></summary>
<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryC7 == \"null\" ? 0 : ratioQueryC7}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryC8"/></summary>
<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryC8 == \"null\" ? 0 : ratioQueryC8}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryB1"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryB1 == \"null\" ? 0 : avgQueryB1}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryB1 == \"null\" ? 0 : minQueryB1}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryB1 == \"null\" ? 0 : maxQueryB1}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryB1 == \"null\" ? 0 : stddevQueryB1}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryB2"/></summary>

<ul>
<li><b><spring:message code="dashboard.avg"/>:</b> <jstl:out value="${avgQueryB2 == \"null\" ? 0 : avgQueryB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.min"/>:</b> <jstl:out value="${minQueryB2 == \"null\" ? 0 : minQueryB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.max"/>:</b> <jstl:out value="${maxQueryB2 == \"null\" ? 0 : maxQueryB2}"></jstl:out></li>
<li><b><spring:message code="dashboard.stddev"/>:</b> <jstl:out value="${stddevQueryB2 == \"null\" ? 0 : stddevQueryB2}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryB3"/></summary>
<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryB3 == \"null\" ? 0 : ratioQueryB3}"></jstl:out></li>
</ul>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryB4"/></summary>
<display:table class="displaytag" name="queryB4" id="row1">
	<spring:message code="actor.name" var="name" />
	<display:column property="name" title="${name}" />
	<spring:message code="actor.surname" var="surname" />
	<display:column property="surname" title="${surname}" />
	<spring:message code="actor.email" var="email" />
	<display:column property="email" title="${email}" />
	<spring:message code="actor.phoneNumber" var="phoneNumber" />
	<display:column property="phoneNumber" title="${phoneNumber}" />
	<spring:message code="actor.address" var="address" />
	<display:column property="address" title="${address}" />
</display:table>
</details><br/>


<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryA1"/></summary>
<display:table class="displaytag" name="queryA1" id="row2">
	<spring:message code="stand.type" var="type" />
	<display:column property="type" title="${type}" />
	<spring:message code="stand.brandName" var="brandName" />
	<display:column property="brandName" title="${brandName}" />
		<spring:message code="stand.banner" var="banner" />
	<display:column title="${banner}">
	<jstl:if test="${not empty row2.banner}">
		<img src="<jstl:out value='${row2.banner}' />"  width="200px" height="200px" />
	</jstl:if>	
	</display:column>
</display:table>
</details><br/>

<details>
<summary style="font-size: 26px; cursor:pointer;"><spring:message code="dashboard.queryA2"/></summary>
<ul>
<li><b><spring:message code="dashboard.ratio"/>:</b> <jstl:out value="${ratioQueryA2 == \"null\" ? 0 : ratioQueryA2}"></jstl:out></li>
</ul>
</details><br/>