<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>
    <h1>Détails Ouvrage</h1>
    <table>
        <tr>
            <td>
                <img src="${ouvrage.image}" height="150" width="100" /></td>
            <td>${ouvrage.titre} <br/>
                ${ouvrage.sousTitre}</br>
                <c:forEach var="them" items="${ouvrage.themes}">
                    ${them.theme} .
                </c:forEach></br>
                <c:forEach var="aut" items="${ouvrage.auteurs}">
                    ${aut.nomAuteur} ${aut.prenomAuteur} .
                </c:forEach></br>
                Prix HT: <fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ouvrage.prixHT}" /><br/>
                Prix TTC: <fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${ouvrage.prixTTC}" /><br/>
            </td>
            <td>   </td><td>   </td><td>   </td><td>   </td><td>   </td><td>   </td><td>   </td><td>   </td><td>   </td><td>   </td>
            <td>
                <div style="width:200px;height:150px;border:2px solid #999999;">
                    <c:if test="${ouvrage.stock >0}">
                        En Stock.<br/><br/>
                        <form action="Controller">
                            Quantité : <input type="number" name="quantity" min="1" max="${ouvrage.stock}"><br/><br/>
                            <input type="hidden" name="ref" value="${ouvrage.isbn}">
                            <input type="hidden" name="section" value="panier">
                            <input type="hidden" name="action" value="ajouter">
                            <input type="submit" name="AjouterProduit" value="Ajouter le livre à votre panier">
                        </form>
                    </c:if>
                    <c:if test="${ouvrage.stock <=0}">
                        Indisponible
                    </c:if>
                </div>
            </td>
        </tr>
    </table>
    <h3>Détail du produit</h3>
    <table>
        <tr> Thèmes :
            <c:forEach var="them" items="${ouvrage.themes}">
                ${them.theme}. 
            </c:forEach>
        </tr></br></br>
        <tr> Editeurs : 
            <c:forEach var="edit" items="${ouvrage.editeurs}">
                ${edit.nomEditeur}.
            </c:forEach>
        </tr></br></br>
        <tr> ISBN : ${ouvrage.isbn}</tr></br></br>
        <tr> Stock : ${ouvrage.stock}</tr></br></br>
        <tr> Résumé : ${ouvrage.resume}</tr></br></br>
    </table>
    <h3>Evaluation</h3>
    <input type="button" value="Laisser un avis sur le livre"/></br></br>
</div>
