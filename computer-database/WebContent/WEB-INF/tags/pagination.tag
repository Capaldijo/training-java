<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ attribute name="target" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="numPage" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="nbComputer" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="nbLine" required="false" type="java.lang.String" description="Text to use in the second cell." %>
<%@ attribute name="search" required="false" type="java.lang.String" description="Text to use in the second cell." %>

<fmt:parseNumber var="lastPage" type="number" integerOnly="true" value="${Math.floor(nbComputer/nbLine)*nbLine}"/>

<c:set var="nbPages" value="${Math.ceil(nbComputer/nbLine)}"/>

<c:choose>
	<c:when test="${numPage != 0}">
		<c:set var="currentPage" value="${numPage/nbLine+1}"/>
	</c:when>
	<c:otherwise>
		<c:set var="currentPage" value="1"/>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${nbPages >= 4}">
		<c:set var="iteration" value="4" />
	</c:when>
	<c:otherwise>
		<c:set var="iteration" value="${nbPages-1}" />
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${currentPage == 1}">
		<c:set var="loopPagination" value="1" />
	</c:when>
	<c:when test="${currentPage == nbPages}">
		<c:set var="loopPagination" value="${nbPages-iteration}" />
	</c:when>
	<c:otherwise>
		<c:set var="loopPagination" value="${currentPage-(iteration/2)}" />
	</c:otherwise>
</c:choose>

<ul class="pagination">
	<li>
	    <a href="${target}?search=${search}&numPage=0&nbLine=${nbLine}" aria-label="Previous">
	        <span aria-hidden="true">&laquo;</span>
	    </a>
	</li>
	
	<c:forEach begin="${loopPagination}" end="${loopPagination+iteration}" varStatus="loop">
		<c:choose>
			<c:when test="${(loopPagination) == 1}">
				<li><a href="${target}?search=${search}&numPage=${(loop.index-1)*nbLine}&nbLine=${nbLine}">${loop.index}</a></li>
			</c:when>
			<c:when test="${(currentPage-1) <= 1}">
				<li><a href="${target}?search=${search}&numPage=${(loop.index)*nbLine}&nbLine=${nbLine}">${loop.index+1}</a></li>
			</c:when>
			<c:when test="${(loopPagination+iteration) >= nbPages+1}">
				<li><a href="${target}?search=${search}&numPage=${(loop.index-2)*nbLine}&nbLine=${nbLine}">${loop.index-1}</a></li>
			</c:when> 
			<c:otherwise>
				<li><a href="${target}?search=${search}&numPage=${(loop.index-1)*nbLine}&nbLine=${nbLine}">${loop.index}</a></li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	
	<li>
		<a href="${target}?search=${search}&numPage=${lastPage}&nbLine=${nbLine}" aria-label="Next">
		    <span aria-hidden="true">&raquo;</span>
		</a>
	</li>

</ul>

<div class="btn-group btn-group-sm pull-right" role="group" >
    <a href="${target}?search=${search}&numPage=${numPage}&nbLine=10" class="btn btn-default">10</a>
    <a href="${target}?search=${search}&numPage=${numPage}&nbLine=50" class="btn btn-default">50</a>
    <a href="${target}?search=${search}&numPage=${numPage}&nbLine=100" class="btn btn-default">100</a>
</div>