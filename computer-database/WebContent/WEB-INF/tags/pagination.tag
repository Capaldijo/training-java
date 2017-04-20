<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="list" required="true" type="java.lang.String" description="Text to use in the first cell." %>
<%@ attribute name="target" required="false" type="java.lang.String" description="Text to use in the second cell." %>

<li>
    <a href="dashboard?numPage=0" aria-label="Previous">
        <span aria-hidden="true">&laquo;</span>
    </a>
</li>

<li><a href="#">1</a></li>
<li><a href="#">2</a></li>
<li><a href="#">3</a></li>
<li><a href="#">4</a></li>
<li><a href="#">5</a></li>

<li>
	<a href="#" aria-label="Next">
	    <span aria-hidden="true">&raquo;</span>
	</a>
</li>

<!--<table>
 <tr>
  <td id="cell1">${cell1}</td>
  <td id="cell2">${cell2}</td>
 </tr>
</table>-->