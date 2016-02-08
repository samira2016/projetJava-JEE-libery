<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:import url="Controller?section=entete&page=Connexion"></c:import>
    <h1>Connexion</h1>


    <div class="col-sm-4">

    <p class="bg-success">${success}</p>
    <p class="bg-danger">${ErrConn}</p> 
    <form class="form-horizontal" action="Controller" method="post"  accept-charset="UTF-8">

        <input type="hidden"  name="section" value="utilisateur"/>
        <input type="hidden"  name="action" value="connexion"/>
        <div class="form-group">
            <label for="login" class="col-sm-2 control-label">Email:</label>
            <div class="col-sm-10">
                <input type="text"  name="login" value="${login}" class="form-control" id="login" placeholder="Email" >
                
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">Mot de passe:</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="password" placeholder="Password" name="password">
               
            </div>
        </div>

        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Se connecter</button>
        </div>
                
</div>
</form>
</div>
<hr>
<br>
<div class="col-sm-12">
<c:url var="url01" value="Controller?section=inscription&action=afficher"></c:url>
<a href="${url01}">s'inscrire</a><br>

  <c:import url="Controller?section=footer"></c:import>
  </div>