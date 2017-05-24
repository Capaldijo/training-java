<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="springForm" uri="http://www.springframework.org/tags/form" %>
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
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/css/bootstrap-datetimepicker.min.css"
          media="screen"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/2.3.1/css/flag-icon.min.css" rel="stylesheet"/>
</head>
<body>
<header class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
    </div>
    <div class="i18n">
        <a class="flag-icon flag-icon-fr" href="?id=${computer.id}&lang=fr"></a>
        <a class="flag-icon flag-icon-gb" href="?id=${computer.id}&lang=en"></a>
    </div>
</header>
<section id="main">
    <div class="container">
        <div class="row">
            <div class="col-xs-8 col-xs-offset-2 box">
                <div class="label label-default pull-right">
                    id: ${computer.id}
                </div>
                <h1><spring:message code="computer.edit.title" /></h1>

                <springForm:form commandName="computer" action="edit_computer" method="POST" id="editForm">
                    <springForm:input path="id" type="hidden" value="${computer.id}" id="id" name="id"/>
                    <fieldset>
                        <div class="form-group">
                            <label for="computerName"><spring:message code="computer.name" /></label>
                            <spring:message code="computer.name" var="name"/>
                            <springForm:input path="name" type="text" class="form-control" id="computerName" name="name"
                                   value="${computer.name}" placeholder="${name}"
                                   maxlength="25" required="true"/>
                        </div>
                        <div class="form-group">
                            <label for="introduced"><spring:message code="computer.introDate" /></label>
                            <spring:message code="computer.introDate" var="introduced"/>
                            <springForm:input path="introduced" type="text" class="form-control" id="introduced" name="introduced"
                                   value="${computer.introduced}" placeholder="${introduced}"/>
                        </div>
                        <div class="form-group">
                            <label for="discontinued"><spring:message code="computer.disconDate" /></label>
                            <spring:message code="computer.disconDate" var="discontinued"/>
                            <springForm:input path="discontinued" type="text" class="form-control" id="discontinued" name="discontinued"
                                   value="${computer.discontinued}" placeholder="${discontinued}"/>
                        </div>
                        <div class="form-group">
                            <label for="companyId"><spring:message code="computer.company" /></label>
                            <springForm:select path="companyId" class="form-control" id="companyId" name="companyId">
                                <springForm:option value="0">---</springForm:option>
                                <c:forEach var="company" items="${companies}">
                                    <c:choose>
                                        <c:when test="${company.id == computer.companyId}">
                                            <springForm:option value="${company.id}" selected="true">${company.name}</springForm:option>
                                        </c:when>
                                        <c:otherwise>
                                            <springForm:option value="${company.id}">${company.name}</springForm:option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </springForm:select>
                        </div>
                    </fieldset>
                    <div class="actions pull-right">
                        <input type="submit" value="<spring:message code="computer.submit.edit" />"
                               id="submitEdit" class="btn btn-primary">
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
<script src="../../js/bootstrap.min.js"></script>
<script>
    $(function () {
        // Init the datepickers
        var intro = "";
        if ("${computer.introduced}" != "null")
            intro = "${computer.introduced}";

        var discon = "";
        if ("${computer.discontinued}" != "null")
            discon = "${computer.discontinued}";

        $('#introduced').datetimepicker({
            format: 'YYYY-MM-DD',
            defaultDate: intro
        });

        $('#discontinued').datetimepicker({
            format: 'YYYY-MM-DD',
            defaultDate: discon
        });

        $('#editForm').bootstrapValidator({
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                computerName: {
                    validators: {
                        regexp: {
                            regexp: /^[a-zA-Z\s_0-9\-.]+/,
                            message: 'You can only type in alphabetical and numerical characters.'
                        }
                    }
                }
            }
        });
    });
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/js/bootstrapValidator.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.10.6/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.37/js/bootstrap-datetimepicker.min.js"></script>
</body>
</html>