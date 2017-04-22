<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="target" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="numPage" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="nbComputer" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="nbLine" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="search" required="false" type="java.lang.String" description="Text to use in the second cell." %>

<fmt:parseNumber var="lastLines" type="number" integerOnly="true" value="${Math.floor(nbComputer/nbLine)*nbLine}"/>

<c:set var="lastPage" value="${Math.ceil(nbComputer/nbLine)}"/>

<c:choose>
	<c:when test="${numPage != 0}">
		<c:set var="currentPage" value="${numPage/nbLine+1}"/>
	</c:when>
	<c:otherwise>
		<c:set var="currentPage" value="1"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${currentPage == 1}">
		<c:set var="loopPage" value="1" />
	</c:when>
	<c:when test="${currentPage == lastPage}">
		<c:set var="loopPage" value="${lastPage-4}" />
	</c:when>
	<c:otherwise>
		<c:set var="loopPage" value="${currentPage-2}" />
	</c:otherwise>
</c:choose>

<ul class="pagination">
	<li>
	    <a href="${target}?search=${search}&numPage=0&nbLine=${nbLine}" aria-label="Previous">
	        <span aria-hidden="true">&laquo;</span>
	    </a>
	</li>
	
	<c:forEach begin="${loopPage}" end="${loopPage+4}" varStatus="loop">
		<c:choose>
			<c:when test="${(loopPage) <= 0}">
				<li><a href="${target}?search=${search}&numPage=${(loop.index)*nbLine}&nbLine=${nbLine}">${loop.index+1}</a></li>
			</c:when>
			<c:when test="${(loopPage+4) >= lastPage+1}">
				<li><a href="${target}?search=${search}&numPage=${(loop.index-2)*nbLine}&nbLine=${nbLine}">${loop.index-1}</a></li>
			</c:when>
			<c:otherwise>
				<li><a href="${target}?search=${search}&numPage=${(loop.index-1)*nbLine}&nbLine=${nbLine}">${loop.index}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	
	<li>
		<a href="${target}?search=${search}&numPage=${lastLines}&nbLine=${nbLine}" aria-label="Next">
		    <span aria-hidden="true">&raquo;</span>
		</a>
	</li>

</ul>

<div class="btn-group btn-group-sm pull-right" role="group" >
    <a href="${target}?search=${search}&numPage=${numPage}&nbLine=10" class="btn btn-default">10</a>
    <a href="${target}?search=${search}&numPage=${numPage}&nbLine=50" class="btn btn-default">50</a>
    <a href="${target}?search=${search}&numPage=${numPage}&nbLine=100" class="btn btn-default">100</a>
</div>