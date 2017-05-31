<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ attribute name="numPage" required="false" type="java.lang.Integer" description="The current page." %>
<%@ attribute name="nbLine" required="false" type="java.lang.Integer" description="The number of computers to print by page." %>
<%@ attribute name="search" required="false" type="java.lang.String" description="The research the user done." %>
<%@ attribute name="orderBy" required="false" type="java.lang.Integer" description="The research the user done." %>
<%@ attribute name="asc" required="false" type="java.lang.String" description="The research the user done." %>

<th>
	<c:choose>
		<c:when test="${orderBy == 1 && asc == 'true'}">
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=1&asc=false">
                <spring:message code="computer.name" />
				<i class="fa fa-caret-down" aria-hidden="true"></i>
			</a>
		</c:when>
		<c:otherwise>
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=1&asc=true">
                <spring:message code="computer.name" />
				<i class="fa fa-caret-up" aria-hidden="true"></i>
			</a>
		</c:otherwise>
	</c:choose>
</th>
<th>
	<c:choose>
		<c:when test="${orderBy == 2 && asc == true}">
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=2&asc=false">
                <spring:message code="computer.introDate" />
				<i class="fa fa-caret-down" aria-hidden="true"></i>
			</a>
		</c:when>
		<c:otherwise>
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=2&asc=true">
                <spring:message code="computer.introDate" />
				<i class="fa fa-caret-up" aria-hidden="true"></i>
			</a>
		</c:otherwise>
	</c:choose>
</th>
<!-- Table header for Discontinued Date -->
<th>
	<c:choose>
		<c:when test="${orderBy == 3 && asc == true}">
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=3&asc=false">
                <spring:message code="computer.disconDate" />
				<i class="fa fa-caret-down" aria-hidden="true"></i>
			</a>
		</c:when>
		<c:otherwise>
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=3&asc=true">
                <spring:message code="computer.disconDate" />
				<i class="fa fa-caret-up" aria-hidden="true"></i>
			</a>
		</c:otherwise>
	</c:choose>
</th>
<!-- Table header for Company -->
<th>
	<c:choose>
		<c:when test="${orderBy == 4 && asc == true}">
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=4&asc=false">
                <spring:message code="computer.company" />
				<i class="fa fa-caret-down" aria-hidden="true"></i>
			</a>
		</c:when>
		<c:otherwise>
			<a href="?search=${search}&numPage=${numPage}&nbLine=${nbLine}&orderBy=4&asc=true">
                <spring:message code="computer.company" />
				<i class="fa fa-caret-up" aria-hidden="true"></i>
			</a>
		</c:otherwise>
	</c:choose>
</th>

