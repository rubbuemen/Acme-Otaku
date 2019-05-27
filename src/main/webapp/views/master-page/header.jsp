<%--
 * header.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>

<div>
	<a href="welcome/index.do"><img src="${bannerUrl}" alt="${nameSystem} Co., Inc." /></a>
</div>


<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->		
		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="stand/listGeneric.do"><spring:message code="master.page.stands" /></a></li>
			<li><a class="fNiv" href="report/listGeneric.do"><spring:message code="master.page.reports" /></a></li>
			<li><a class="fNiv"><spring:message code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/register-visitor.do"><spring:message code="master.page.register.visitor" /></a></li>
					<li><a href="actor/register-member.do"><spring:message code="master.page.register.member" /></a></li>
					<li><a href="actor/register-seller.do"><spring:message code="master.page.register.seller" /></a></li>
					<li><a href="actor/register-sponsor.do"><spring:message code="master.page.register.sponsor" /></a></li>
				</ul>
			</li>
			<li><a class="fNiv" href="security/login.do"><spring:message code="master.page.login" /></a></li>
		</security:authorize>

		<security:authorize access="hasRole('MEMBER')">
		<jstl:if test="${role eq 'PRESIDENT'}">
			<li style="background-color: ${association.representativeColor}">
				<a class="fNiv"><spring:message code="master.page.president" /></a>
				<ul>
					<li class="arrow" style="background-color: ${association.representativeColor}"></li>
					<jstl:if test="${role eq 'PRESIDENT'}">
						<li style="background-color: ${association.representativeColor}"><a href="application/member/listPresident.do"><spring:message code="master.page.applicationsAssociation" /></a></li>
						<li style="background-color: ${association.representativeColor}"><a href="member/member/list.do"><spring:message code="master.page.membersAssociation" /></a></li>
						<li style="background-color: ${association.representativeColor}"><a href="meeting/member/list.do"><spring:message code="master.page.meetings" /></a></li>
						<li style="background-color: ${association.representativeColor}"><a href="headquarter/member/list.do"><spring:message code="master.page.headquarters" /></a></li>
					</jstl:if>
				</ul>
			</li>
		</jstl:if>	
		</security:authorize>
		
		<security:authorize access="hasRole('MEMBER')">
			<li style="background-color: ${association.representativeColor}">
				<a class="fNiv"><spring:message code="master.page.member" /></a>
				<ul>
					<li class="arrow" style="background-color: ${association.representativeColor}"></li>
					<li style="background-color: ${association.representativeColor}"><a href="association/member/show.do"><spring:message code="master.page.association" /></a></li>
					<li style="background-color: ${association.representativeColor}"><a href="application/member/list.do"><spring:message code="master.page.applications" /></a></li>
					<jstl:if test="${association != null}">
						<li style="background-color: ${association.representativeColor}"><a href="event/member/list.do"><spring:message code="master.page.events" /></a></li>
						<li style="background-color: ${association.representativeColor}"><a href="activity/member/list.do"><spring:message code="master.page.activities" /></a></li>
						<li style="background-color: ${association.representativeColor}"><a href="enrolment/member/list.do"><spring:message code="master.page.enrolments" /></a></li>
					</jstl:if>
					
				</ul>
			</li>
		</security:authorize>
						
		<security:authorize access="hasRole('VISITOR')">
			<li>
				<a class="fNiv"><spring:message code="master.page.visitor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="event/visitor/list.do"><spring:message code="master.page.events" /></a></li>
					<li><a href="enrolment/visitor/list.do"><spring:message code="master.page.enrolments" /></a></li>
					<li><a href="activity/visitor/list.do"><spring:message code="master.page.activitiesForScore" /></a></li>
					<li><a href="report/visitor/list.do"><spring:message code="master.page.reports" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('SELLER')">
			<li>
				<a class="fNiv"><spring:message code="master.page.seller" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="stand/seller/list.do"><spring:message code="master.page.stands" /></a></li>
					<li><a href="event/seller/list.do"><spring:message code="master.page.events" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="hasRole('SPONSOR')">
			<li>
				<a class="fNiv"><spring:message code="master.page.sponsor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="sponsorship/sponsor/list.do"><spring:message code="master.page.sponsorships" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		
		<security:authorize access="hasRole('ADMIN')">
			<li>
				<a class="fNiv"><spring:message code="master.page.admin" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/administrator/register-administrator.do"><spring:message code="master.page.register.admin" /></a></li>
					<li><a href="category/administrator/list.do"><spring:message code="master.page.categories" /></a></li>
					<li><a href="dashboard/administrator/show.do"><spring:message code="master.page.dashboard" /></a></li>	
					<li><a href="systemConfiguration/administrator/show.do"><spring:message code="master.page.systemConfiguration" /></a></li>	
					<li><a href="systemConfiguration/administrator/actorsList.do"><spring:message code="master.page.actorsList" /></a></li>
				</ul>
			</li>
		</security:authorize>
		
		<security:authorize access="isAuthenticated()">
		<li style="background-color: ${association.representativeColor}"><a class="fNiv" href="stand/listGeneric.do"><spring:message code="master.page.stands" /></a></li>
		<li style="background-color: ${association.representativeColor}"><a class="fNiv" href="report/listGeneric.do"><spring:message code="master.page.reports" /></a></li>
		<li style="background-color: ${association.representativeColor}"><a class="fNiv" href="socialProfile/list.do"><spring:message code="master.page.socialProfiles" /></a></li>
		<li style="background-color: ${association.representativeColor}"><a class="fNiv" href="box/list.do"><spring:message code="master.page.boxes" /></a></li>
			<li style="background-color: ${association.representativeColor}">
				<a class="fNiv"> 
					<spring:message code="master.page.profile" /> 
			        (<security:authentication property="principal.username" />)
				</a>
				<ul>
					<li class="arrow" style="background-color: ${association.representativeColor}"></li>
					<security:authorize access="hasRole('VISITOR')">
						<li><a href="actor/visitor/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('MEMBER')">
						<li style="background-color: ${association.representativeColor}"><a href="actor/member/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('SELLER')">
						<li><a href="actor/seller/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('SPONSOR')">
						<li><a href="actor/sponsor/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<security:authorize access="hasRole('ADMIN')">
						<li><a href="actor/administrator/edit.do"><spring:message code="master.page.edit.profile" /></a></li>
					</security:authorize>
					<spring:message code="master.page.delete.account.confirm" var="confirm" />
					<security:authorize access="!hasRole('ADMIN')">				
						<li style="background-color: ${association.representativeColor}"><a href="actor/delete.do" onClick="return checkPosting('${confirm}');" ><spring:message code="master.page.delete.account" /> </a></li>
					</security:authorize>	
					<li style="background-color: ${association.representativeColor}"><a href="actor/export.do" ><spring:message code="master.page.export.data" /> </a></li>
					<li style="background-color: ${association.representativeColor}"><a href="j_spring_security_logout"><spring:message code="master.page.logout" /> </a></li>
				</ul>
			</li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>