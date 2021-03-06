<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Computer Database</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../../css/font-awesome.css" rel="stylesheet" media="screen">
    <link href="../../css/main.css" rel="stylesheet" media="screen">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/css/bootstrapValidator.min.css"
          rel="stylesheet" media="screen">
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/2.3.1/css/flag-icon.min.css" rel="stylesheet"/>
</head>
<body>
<header class="navbar navbar-inverse navbar-fixed-top">
    <%@include file="header.jsp"%>
</header>

<section id="main">
    <div class="container">
        <div class="row">
            <div class="col-xs-8 col-xs-offset-2 box">
                <h1><spring:message code="computer.add.title" /></h1>
                <springForm:form modelAttribute="computer" action="add_computer" method="POST" id="addForm">
                    <fieldset>
                        <div class="form-group">
                            <label for="computerName"><spring:message code="computer.name" /></label>
                            <spring:message code="computer.name" var="name"/>
                            <springForm:input path="name" type="text" class="form-control" id="computerName"
                                              name="name" placeholder="${name}" maxlength="20"/>
                            <span><springForm:errors path="name" cssClass="error" /></span>
                        </div>
                        <div class="form-group">
                            <label for="introduced"><spring:message code="computer.introDate" /></label>
                            <spring:message code="computer.introDate" var="introduced"/>
                            <springForm:input path="introduced" type='text' class="form-control" id='introduced'
                                              name="introduced" placeholder="${introduced}"/>
                            <span><springForm:errors path="introduced" cssClass="error" /></span>
                        </div>
                        <div class="form-group">
                            <label for="discontinued"><spring:message code="computer.disconDate" /></label>
                            <spring:message code="computer.disconDate" var="discontinued"/>
                            <springForm:input path="discontinued" type="text" class="form-control" id="discontinued"
                                              name="discontinued" placeholder="${discontinued}"/>
                            <span><springForm:errors path="discontinued" cssClass="error" /></span>
                        </div>
                        <div class="form-group">
                            <label for="companyId"><spring:message code="computer.company" /></label>
                            <springForm:select path="company.id" class="form-control" id="companyId" name="companyId">
                                <springForm:option value="0">---</springForm:option>
                                <c:forEach var="company" items="${companies}">
                                    <springForm:option value="${company.id}">${company.name}</springForm:option>
                                </c:forEach>
                            </springForm:select>
                            <span><springForm:errors path="company.id" cssClass="error" /></span>
                        </div>
                    </fieldset>
                    <div class="actions pull-right">
                        <input type="submit" value="<spring:message code="computer.submit.add" />"
                               id="submitAdd" class="btn btn-primary"/>
                        or
                        <a href="dashboard" class="btn btn-default">
                            <spring:message code="computer.submit.cancel" />
                        </a>
                    </div>
                </springForm:form>
            </div>
        </div>
    </div>
</section>
<script src="../../js/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/js/bootstrapValidator.min.js"></script>
<script src="../../js/bootstrap.min.js"></script>
<script src="../../js/addComputer.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>
</body>
</html>