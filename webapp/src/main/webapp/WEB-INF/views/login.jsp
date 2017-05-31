<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
    <title>Computer Database</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">
    <link href="../../css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../../css/datepicker.min.css" rel="stylesheet" media="screen">
    <link href="../../css/font-awesome.css" rel="stylesheet" media="screen">
    <link href="../../css/main.css" rel="stylesheet" media="screen">
</head>
<body>
<header class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <a class="navbar-brand" href="/"> Application -
            Computer Database </a>
    </div>
</header>
<section id="main">
    <div class="container">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><spring:message code="login.title"/></h3>
                </div>
                <div class="panel-body">
                    <form method="POST" action="/login" name="loginForm">
                        <fieldset>
                            <div class="form-group">
                                <label for="username"><spring:message code="login.username"/></label>
                                <input type="text" name="username" class="form-control" id="username">
                            </div>
                            <div class="form-group">
                                <label for="password"><spring:message code="login.password"/></label>
                                <input type="password" name="password" class="form-control" id="password">
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </fieldset>
                        <div class="actions pull-right">
                            <input id="submit" type="submit" value="<spring:message code="login.submit" />"
                                   class="btn btn-sm btn-success">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>
<script src="../../js/jquery.min.js"></script>
<script src=../../js/bootstrap.min.js"></script>
</body>
</html>