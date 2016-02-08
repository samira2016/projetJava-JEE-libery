<%-- 
    Document   : entete
    Created on : 8 sept. 2015, 15:47:03
    Author     : fafiec103
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <link href="css/bootstrap/css/bootstrap-theme.css" rel="stylesheet" type="text/css"/>
        <link href="css/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css"/>

        <title>${page}</title>
    </head>
    <body>
        <c:url var="urlBanniere" value="Controller?section=banniere" />
        <c:import url="${urlBanniere}" />
