<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<div>
    <ul class="nav nav-pills">
        <li role="presentation" class="active"><a href="Controller">Accueil</a></li>
        
        <!--li role="presentation" class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true" aria-expanded="false">
            Test <span class="caret"></span>
            </a>
            <ul class="dropdown-menu">
                <c:url var="urlSupprimerJeuTest" value="Controller?section=donnees&action=supprimer" />
                <li><a href="${urlSupprimerJeuTest}">supprimer le jeu de test</a></li>
                <c:url var="urlCreerJeuTest" value="Controller?section=catalogue&action=creer" />
                <li><a href="${urlCreerJeuTest}">créer le jeu de test</a></li>
                <c:url var="urlRetStock" value="Controller?section=donnees&action=restock" />
                <li><a href="${urlRetStock}">retablir le stock</a></li>
            </ul>
        </li-->
        <c:url var="urlSupprimerJeuTest" value="Controller?section=donnees&action=supprimer" />
        <li role="presentation"><a href="${urlSupprimerJeuTest}">supprimer le jeu de test</a></li>
        <c:url var="urlCreerJeuTest" value="Controller?section=catalogue&action=creer" />
        <li role="presentation"><a href="${urlCreerJeuTest}">créer le jeu de test</a></li>
        <c:url var="urlRetStock" value="Controller?section=donnees&action=restock" />
        <li role="presentation"><a href="${urlRetStock}">retablir le stock</a></li>
        <c:url var="urlAchat" value="Controller?section=achat&action=afficher" />
        <li role="presentation"><a href="${urlAchat}">vers page des achats</a></li>
        <c:url var="urlPanier" value="Controller?section=panier&action=afficher" />
        <li role="presentation"><a href="${urlPanier}">voir panier</a></li>
        <c:url var="urlCommande" value="Controller?section=commande&action=afficher" />
        <li role="presentation"><a href="${urlCommande}">voir commande</a></li><br><br>
        
        
        <c:url var="urlInscription" value="Controller?section=inscription&action=afficher"></c:url>
            <li role="presentation"><a href="${urlInscription}">s'inscrire</a></li>   
        <c:url var="urlEspacePerso" value="Controller?section=espace&action=afficher"></c:url>
            <li role="presentation"><a href="${urlEspacePerso}">Mon compte</a></li>
    
        <c:if test="${not empty user}">
            <c:url var="urldec" value="Controller?section=espace&action=deconnexion"></c:url>
            <li role="presentation"><a href="${urldec}"> Se deconnecter </a></li><br>
        </c:if>        
    </ul>
    
    <p>Bienvenue nous sommes le 
        <fmt:formatDate value="${today}" pattern="EEEE dd MMMM yyyy" /></p>
    <hr />
</div>