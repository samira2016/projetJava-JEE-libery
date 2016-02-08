<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <c:import url="Controller?section=entete&page=Accueil"></c:import>
                
    <h1 class="active">page d'accueil</h1>
    
    
    <c:url var="urlInscription" value="Controller?section=inscription&action=afficher"></c:url>
     <a href="${urlInscription}">s'inscrire</a><br/>    
     <c:url var="urlEspacePerso" value="Controller?section=espace&action=afficher"></c:url>
     <a href="${urlEspacePerso}">Espace personnel</a><br/>
     
    
    
    <c:import url="Controller?section=footer&page=Accueil"></c:import>
