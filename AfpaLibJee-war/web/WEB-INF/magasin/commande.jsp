<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt"  uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:import url="Controller?section=entete&page=Commande"></c:import>
<div class="panel panel-primary">
    <c:if test="${commandeVerifiee eq 'annulé'}" >
        <h3 class="panel-heading">Votre commande a été annulée</h3>
    </c:if>
    <c:if test="${!(commandeVerifiee eq 'annulé')}" >
        <h1 class="panel-heading">Récapitulatif de votre commande</h1>

        <div class="panel-body">
            <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <th>Couverture</th>
                    <th>Reference</th>
                    <th>Auteur</th>
                    <th>prix TTC</th>
                    <th>Quantité</th>
                    <th>prix TTC total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${maCommande}" var="item" >
                    <tr>
                        <th><img src="${item.ouvrage.imageUrl}" height="150" width="100" /></th>
                        <th>${item.ouvrage.titre}</th>
                        <th><c:forEach items="${item.ouvrage.auteurs}" var="itemAuteur">${itemAuteur.nomAuteur}</c:forEach></th>
                        <th><fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${item.prixTTC}" /></th>
                        <th>${item.nombreArticle}</th>
                        <th><fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${item.nombreArticle*item.prixTTC}"/></th>
                    </tr>
                </c:forEach>
                <tr>
                    <th>TOTAL</th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th></th>
                    <th><c:out value="${prixTotal}"/></th>
                </tr>
            </tbody>
        </table>
        </div>
        <hr />

        <form method="POST" action="#">
        <div class="panel-body">
        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <td width="50%">
                        Adresse de Livraison
                    </td>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${monAdresseLivraison}" var="itemLivraison">
                    <tr>
                        <td>${itemLivraison}<td/> 
                    </tr>
                </c:forEach>

                <tr>
                    <td width="50%"><input class="btn btn-xs btn-primary" type="submit" value="modifier" name="modifierLivraison"/></td>
                </tr>
            </tbody>
        </table>
        </form>
        </div>

        <form method="POST" action="#">
        <div class="panel-body">
        <table class="table table-striped table-bordered">
            <thead>
                <tr>
                    <td width="50%">
                        Adresse de Facturation
                    </td>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${monAdresseFacturation}" var="itemFacturation">
                    <tr>
                        <td>${itemFacturation}<td/> 
                    </tr>
                </c:forEach>
                <tr>
                    <td width="50%"><input class="btn btn-xs btn-primary" type="submit" value="modifier" name="modifierFacturation"/></td>
                </tr>
            </tbody>
        </table>
        </div>
        </form>

        <hr/>
        <c:if test="${commandeVerifiee eq 'Validé'}" >
            <h3 class="panel-heading">Votre commande a été prise en compte</h3>
        <form method="POST" action="Controller?section=commande&action=recu">
            <input class="btn btn-xs btn-primary" type ="submit" value="Envoyer reçu" name="recu"/>
        </form>    
        </c:if>
        <c:if test="${commandeVerifiee eq 'En attente...'}" >
            <h3 class="panel-heading">Votre commande est en attente de validation de votre payement.
                Vous recevrez un mail de confirmation dès qu'il aura été validé</h3>
            <form method="POST" action="Controller?section=commande&action=recu">
                <input class="btn btn-xs btn-primary" type ="submit" value="Accepter" name="atteindre"/>
            </form>    
        </c:if>
        <c:if test="${commandeVerifiee eq 'Echoué'}" >
            <h3 class="panel-heading">La vérification de votre payement a echoué. Veuillez ressayer ultérieurement. On garde votre commande.</h3>
        </c:if>
        <c:if test="${!(commandeVerifiee eq 'Validé') and !(commandeVerifiee eq 'En attente...')}" >
            <form method="POST" action="Controller?section=modifierCommande">
                <input class="btn btn-xs btn-primary" type ="submit" value="modifier" name="modifier"/>
            </form>
            <form method="POST" action="Controller?section=commande&action=annuler">
                <input class="btn btn-xs btn-primary" type ="submit" value="Annuler" name="annuler"/>
            </form>
        </c:if>
    </c:if>
</div>

<c:import url="Controller?section=footer&page=Commande"></c:import>
