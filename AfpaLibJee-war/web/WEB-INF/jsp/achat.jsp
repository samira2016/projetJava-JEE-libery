<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:import url="Controller?section=entete&page=Achat"></c:import>
            <div>   
                <table>
                    <tbody>
                        <tr><label>Recherche Générale</label></tr>  
                    <form class="form-inline" method="POST" action="Controller?section=catalogue&action=rechercher" accept-charset="UTF-8">
                        <td> <input type="text" maxlength="25" name="CritereRecherche"  class="form-control"/></td>
                        <td> <input type="submit" value="Rechercher" class="btn btn-primary"/></td><td></td>
                    </form>
                    </tbody>
                </table>

                <table>
                    <tbody>
                        <tr><label>Recherche dans catalogue</label></tr>
                        <tr>
                    <form class="form-inline" method="POST" action="Controller?section=catalogue&action=rechercher" accept-charset="UTF-8">
                        <td><label>Thème</label>  
                            <select name="theme" class="form-control">
                                <option value=""></option>
                            <c:forEach var="item" items="${themes}">
                                <option value="${item.theme}" >${item.theme}</option>
                            </c:forEach>
                        </select>
                    </td><td></td>
                    <td><label>Auteur</label><input type="text" maxlength="50" name="auteur"  class="form-control"/></td>
                    <td><label>Titre</label><input type="text" maxlength="30" name="titre"  class="form-control"/></td>
                    <td><label>Mot-clef</label><input type="text" maxlength="22" name="motClef"  class="form-control"/></td>
                    <td><label>Numéro ISBN</label><input type="text" maxlength="13" name="isbn"  class="form-control"/></td>
                    <td><input type="submit" value="Rechercher" class="btn btn-primary"/></td>
                </form>
                </tr>
                </tbody>
            </table>
        </div>
        <c:url var="url02" value="Controller?section=catalogue&action=afficher" />
        <c:import url="${url02}" />
        <c:import url="Controller?section=footer"></c:import>
    </body>