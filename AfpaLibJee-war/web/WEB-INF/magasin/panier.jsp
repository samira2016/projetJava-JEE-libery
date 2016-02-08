<%-- 
    Document   : panier
    Created on : 9 sept. 2015, 12:12:58
    Author     : fafiec107
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="Controller?section=entete&page=Achat"></c:import>
<div>
    <h1>Panier</h1>
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
            <c:if test="${PanierVide}">
                Panier vide!
            </c:if>
            <c:if test="${!PanierVide}">
                <c:forEach items="${monPanier}" var="item" >
                    <tr>
                        <td>${item.titre}</td>
                        <td>${item.stock}</td>
                        <td><fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${item.prixHT}" /></td>
                        <td>
                            <a href="Controller?section=panier&action=inc&ref=${item.isbn}">+</a>
                            <a href="Controller?section=panier&action=dec&ref=${item.isbn}">-</a>
                            <a href="Controller?section=panier&action=sup&ref=${item.isbn}">X</a>
                        </td>
                    </tr>
                </c:forEach>
                <a href="Controller?section=panier&action=vider">Vider le panier</a> | 
                <a href="Controller?section=commande&action=afficher">Passer commande</a><br>
            </c:if>
            <a href="Controller?section=achat&action=afficher">Revenir au catalogue</a>
        </tbody>
    </table>
    <hr />
</div>

<c:import url="Controller?section=footer&page=Accueil"></c:import>