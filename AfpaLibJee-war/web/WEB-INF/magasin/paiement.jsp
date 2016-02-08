<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="Controller?section=entete&page=Commande"></c:import>
<div>
    <h1>Bienvenue sur le site de paiement</h1>

    <hr/>
    <form method="POST" action="Controller?section=commande&action=verification">
        <label for="paymentStatus">Etat de la transaction :</label>
        <select id="paymentStatus" name="transactionStatus" >
            <option>Validé</option>
            <option>Echoué</option>
            <option>En attente...</option>
        </select><br/>
        <input type ="submit" value="Payer" name="accepter"/>
    </form>
</div>

<c:import url="Controller?section=footer&page=Commande"></c:import>