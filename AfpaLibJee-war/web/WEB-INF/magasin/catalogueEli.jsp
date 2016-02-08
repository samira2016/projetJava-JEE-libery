<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>
    <h1>Catalogue</h1>
    <table border="1">
        <thead>
            <tr>
                <th>Reference</th>
                <th>Quantité</th>
                <th>prix HT</th>
                <th>action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${monCatalogue}" var="item" >
                <tr>
                    <td>${item.titre}</td>
                    <td>${item.stock}</td>
                    <td><fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${item.prixHT}" /></td>
                    <td><a href="Controller?section=panier&action=ajouter&ref=${item.isbn}">Ajouter</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <hr />
</div>
