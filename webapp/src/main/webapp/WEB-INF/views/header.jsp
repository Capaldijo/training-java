<div class="container">
    <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
    <div class="navbar-btn pull-right">
        <c:url var="logoutUrl" value="/logout"/>
        <form class="form-inline" action="${logoutUrl}" method="post">
            <input type="submit" value="<spring:message code="login.logout" />" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
    </div>
    <div class="pull-left col-xs-5">
        <a class="flag-icon flag-icon-fr" href="?lang=fr"></a>
        <a class="flag-icon flag-icon-gb" href="?lang=en"></a>
        <a class="flag-icon flag-icon-it" href="?lang=it"></a>
    </div>
</div>