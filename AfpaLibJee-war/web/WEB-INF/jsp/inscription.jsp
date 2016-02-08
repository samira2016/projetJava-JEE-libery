<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:import url="Controller?section=entete&page=Inscription"></c:import>
    <h1>inscription</h1>


    <div class="col-sm-4">

        <p class="bg-success">${success}</p>
    <form class="form-horizontal" action="Controller" method="post"  accept-charset="UTF-8">

        <input type="hidden"  name="section" value="utilisateur"/>
        <input type="hidden"  name="action" value="inscription"/>
        <div class="form-group">
            <label for="login" class="col-sm-2 control-label">Email:</label>
            <div class="col-sm-10">
                <input type="text"  name="login" value="${login}" class="form-control" id="login" placeholder="Email" >
               <span class="bg-danger">${errLogin}</span>
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">Mot de passe:</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="password" placeholder="Password" name="password">
                <span class="bg-danger">${errPassword}</span>
            </div>
        </div>

        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Valider</button>
        </div>
</div>
</form>
</div>