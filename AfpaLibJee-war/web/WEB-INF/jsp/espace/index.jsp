
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <c:import url="Controller?section=entete&page=Espace personnel"></c:import>

         <c:url var="url02" value="Controller?section=espace&action=deconnexion"></c:url>
         <a href="${url02}">Se deconnecter</a><br>
         <div>
             
             <h4>Mon compte</h4>
             <c:url var="url01" value="Controller?section=espace&action=afficherModif"></c:url>
             <a href="${url01}">Mes coordonnées </a><br>
             <c:url var="url01" value="Controller?section=espace&action=afficherAdresse"></c:url>
             <a href="${url01}">Mes adresses </a><br>
             
             
             
         </div>
             
             <div>
              <h4>Suivi de commande</h4>   
              <p>mes  commandes </p>
                 
                  <c:url var="url01" value="Controller?section=espace&action=afficherCommande"></c:url>
     <a href="${url01}">Afficher mes comandes </a><br>
     <c:url var="url01" value="Controller?section=espace&action=afficherSuivie"></c:url>
     <a href="${url01}">Suivi de commande </a><br>
                 
             </div>
    
    
     <c:url var="url01" value="Controller?section=espace&action=desinscrire"></c:url>
     <a href="${url01}">Me desinscrire</a><br>
    <br>
     
    <div>
        <p>
            
        </p>
    </div>
    <c:import url="Controller?section=footer"></c:import>
