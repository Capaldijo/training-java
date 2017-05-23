<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Computer Database</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">
    <!-- Bootstrap -->
    <link href="../../css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="../../css/font-awesome.css" rel="stylesheet" media="screen">
    <link href="../../css/main.css" rel="stylesheet" media="screen">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/2.3.1/css/flag-icon.min.css" rel="stylesheet"/>
</head>
<body>
<header class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
    </div>
    <div class="i18n">
        <a class="flag-icon flag-icon-fr" href="?lang=fr"></a>
        <a class="flag-icon flag-icon-gb" href="?lang=en"></a>
    </div>
</header>

<section id="main">
    <div class="container">
        <h1 id="homeTitle">
            <spring:message code="dashboard.nbComputers" arguments="${nbComputer}"/>
        </h1>
        <div id="actions" class="form-horizontal">
            <div class="pull-left">
                <form id="searchForm" action="dashboard" method="GET" class="form-inline">
                    <input type="search" id="searchbox" name="search" class="form-control" placeholder="Search name"/>
                    <input type="hidden" value="${nbLine}" id="nbLine" name="nbLine"/>
                    <input type="submit" id="searchsubmit" value="<spring:message code="dashboard.search" />"
                           class="btn btn-primary"/>
                </form>
            </div>
            <div class="pull-right">
                <a class="btn btn-success" id="addComputer" href="add_computer">
                    <spring:message code="dashboard.addComputer"/>
                </a>
                <a class="btn btn-default" id="editComputer" href="#" onclick="$.fn.toggleEditMode();">
                    <spring:message code="dashboard.edit"/>
                </a>
            </div>
        </div>
    </div>

    <form id="deleteForm" action="dashboard" method="POST">
        <input type="hidden" name="selection" value="">
        <input type="hidden" value="${search}" name="search"/>
        <input type="hidden" value="${nbLine}" name="nbLine"/>
        <input type="hidden" value="${numPage}" name="numPage"/>
    </form>

    <div class="container" style="margin-top: 10px;">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <!-- Variable declarations for passing labels as parameters -->
                <!-- Table header for Computer Name -->

                <th class="editMode" style="width: 60px; height: 22px; display: none">
                    <input type="checkbox" id="selectall"/>
                    <span style="vertical-align: top;">
                                 -  <a href="#" id="deleteSelected" onclick="$.fn.deleteSelected();">
                                        <i class="fa fa-trash-o fa-lg"></i>
                                    </a>
                            </span>
                </th>
                <myTag:orderBy numPage="${numPage}" asc="${asc}"
                               orderBy="${orderBy}" nbLine="${nbLine}" search="${search}"/>
            </tr>
            </thead>
            <!-- Browse attribute computers -->
            <tbody id="results">
            <c:forEach var="computer" items="${computers}">
                <tr>
                    <td class="editMode" style="display: none">
                        <input type="checkbox" name="cb" class="cb" value="${computer.id}">
                    </td>
                    <td>
                        <a href="edit_computer?id=${computer.id}" onclick="">${computer.name}</a>
                    </td>
                    <td>${computer.introduced}</td>
                    <td>${computer.discontinued}</td>
                    <td>${computer.companyId}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</section>

<footer class="navbar-fixed-bottom">
    <div class="container text-center">
        <myTag:pagination numPage="${numPage}" nbComputer="${nbComputer}" nbLine="${nbLine}" search="${search}"/>
    </div>
</footer>
<script src="../../js/jquery.min.js"></script>
<script src="../../js/bootstrap.min.js"></script>
<script src="../../js/dashboard.js"></script>

</body>
</html>