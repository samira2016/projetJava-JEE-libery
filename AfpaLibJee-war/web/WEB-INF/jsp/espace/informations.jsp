<%@taglib  prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:import url="Controller?section=entete&page=Inscription"></c:import>
    <h1>Informations personnelles</h1>


    <div class="col-sm-6">

        <p class="bg-success">${success}</p>
    <p class="bg-danger">${echec}</p>
    <form class="form-horizontal" action="Controller" method="post"  accept-charset="UTF-8">

        <input type="hidden"  name="section" value="espace"/>
        <input type="hidden"  name="action" value="modifier"/>
        
         <div class="form-group">
            
            
            <label for="nom" class="col-sm-2 control-label" >Login :</label>
            <div class="col-sm-10">
                <input type="text"  name="nom" value="${user.login}" class="form-control" id="login" placeholder="Login"  disabled>
               <span class="bg-danger">${errLogin}</span>
            </div>
        </div>
        
            <div class="form-group">
            
            
            <label for="Password" class="col-sm-2 control-label" >Password:</label>
            <div class="col-sm-10">
                <input type="password"  name="password" value="${user.password}" class="form-control" id="Password" placeholder="Password"  >
               <span class="bg-danger">${errPassword}</span>
            </div>
        </div>
             <p class="bg-info">Mes Coordonnées de livraison et/ou facturation</p>
              <p>A chaque commande, vous pourrez choisir une autre adresse de livraison.</p>
             
            
        <div class="form-group">
            
            
            <label for="nom" class="col-sm-2 control-label">Nom:</label>
            <div class="col-sm-10">
                <input type="text"  name="nom" value="${user.nom}" class="form-control" id="nom" placeholder="Nom" >
               <span class="bg-danger">${errNom}</span>
            </div>
        </div>
        <div class="form-group">
            <label for="prenom" class="col-sm-2 control-label">Prenom:</label>
            <div class="col-sm-10">
                <input type="prenom" class="form-control" id="prenom" placeholder="prenom" name="prenom" value="${user.prenom}">
                <span class="bg-danger">${errPrenom}</span>
            </div>
        </div>

            <div class="form-group">
            <label for="telephone" class="col-sm-2 control-label">Telephone:</label>
            <div class="col-sm-10">
                <input type="hidden" name="idAdresse" value="${adresse.id}" /> 
                <input type="telephone" class="form-control" id="telephone" placeholder="telephone" name="telephone"value="${user.telephone}">
                <span class="bg-danger">${errTelephone}</span>
            </div>
        </div>
           
            <div class="form-group">
            <label for="adresse" class="col-sm-2 control-label">Adresse</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="adresse" placeholder="adresse" name="rue"value="${adresse.rue}">
                <span class="bg-danger">${errAdresse}</span>
            </div>
        </div>
            
             <div class="form-group">
            <label for="cp" class="col-sm-2 control-label">Code postal</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="cp" placeholder="code postal" name="cp"value="${adresse.codePostal}">
                <span class="bg-danger">${errCp}</span>
            </div>
        </div>
            
            
             <div class="form-group">
            <label for="ville" class="col-sm-2 control-label">Ville</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="cp" placeholder="ville" name="ville"value="${adresse.ville}">
                <span class="bg-danger">${errVille}</span>
            </div>
        </div>
            
            <div class="form-group">
            <label for="pays" class="col-sm-2 control-label">Pays</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="cp" placeholder="pays" name="pays"value="${adresse.pays}">
                <span class="bg-danger">${errPays}</span>
            </div>
        </div>
           
            
            
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">Modifier</button>
        </div>
</div>
</form>
</div>