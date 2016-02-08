<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div>
    <div class="col-lg-2">
        <ul>
            
            <c:forEach var="item" items="${themes}">
                <c:url var="url01" value="Controller?section=catalogue&action=listSousTheme&theme=${item.theme}"></c:url>
                <li><a href ="${url01}">${item.theme}</a>
                    <c:if test="${sousTheme.size()>=0}">
                    <ul>
                        <c:forEach var="i" items="${sousTheme}">
                           <li>${i.theme}</li> 
                        </c:forEach> 
                    </ul>
                </c:if>
                    </li> 
            </c:forEach>
            
        </ul>
    </div>
    <div class="col-lg-10">

        <h1>Le catalogue</h1>

        <p>${message01}</p>
        <table >
            <tbody>
                <c:forEach items="${monCatalogue}" var="item" >
                    <tr>
                        <td><c:url var="url01" value="Controller?section=catalogue&action=afficherDetail&ref=${item.isbn}"/>
                            <a href = "${url01}"><img src="${item.image}" height="150" width="100" /> </a></td>
                        <td><a href="${url01}">${item.titre} </a><br/>
                            ${item.sousTitre}</br>
                            <c:forEach var="them" items="${item.themes}">
                                ${them.theme} .
                            </c:forEach></br>
                            <c:forEach var="aut" items="${item.auteurs}">
                                ${aut.nomAuteur} ${aut.prenomAuteur} .
                            </c:forEach></br>
                            Prix HT: <fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${item.prixHT}" /><br/>
                            Prix TTC: <fmt:formatNumber minFractionDigits="2" maxFractionDigits="2" value="${item.prixTTC}" /><br/>
                        </td>
                        <td> Evaluations</td>
                    </tr> 
                </c:forEach>
            </tbody>   
        </table>
        <c:if test="${afficherPagination}">
            <table class="pagination">    
                <tr>
                    <c:url var = "url04" value="Controller?section=Catalogue&action=rechercher&page=${currentPage-1}&theme=${theme}&auteur=${auteur}&titre=${titre}&motClef=${motClef}&isbn=${isbn}&CritereRecherche=${CritereRecherche}"></c:url>
                    <td><a href="${url04}"><<</a></td><td></td>  
                    <c:url var = "url02" value="Controller?section=Catalogue&action=rechercher&page=${currentPage-1}&theme=${theme}&auteur=${auteur}&titre=${titre}&motClef=${motClef}&isbn=${isbn}&CritereRecherche=${CritereRecherche}"></c:url>
                    <td><a href="${url02}"><</a></td>   
                    <c:forEach begin="1" end="${nbrPage}" var="i"><td></td>
                        <c:url var = "url01" value="Controller?section=Catalogue&action=rechercher&page=${i}&theme=${theme}&auteur=${auteur}&titre=${titre}&motClef=${motClef}&isbn=${isbn}&CritereRecherche=${CritereRecherche}"></c:url>
                        <td><a href="${url01}">${i}</a></td><td></td>
                        </c:forEach>
                        <c:url var = "url03" value="Controller?section=Catalogue&action=rechercher&page=1&theme=${theme}&auteur=${auteur}&titre=${titre}&motClef=${motClef}&isbn=${isbn}&CritereRecherche=${CritereRecherche}"></c:url>
                    <td><a href="${url03}">></a><td><td></td>
                    <c:url var = "url04" value="Controller?section=Catalogue&action=rechercher&page=${nbrPage}&theme=${theme}&auteur=${auteur}&titre=${titre}&motClef=${motClef}&isbn=${isbn}&CritereRecherche=${CritereRecherche}"></c:url>
                    <td><a href="${url04}">>></a><td><td></td>

                </tr>
            </table> 

        </c:if>

    </div>
</div>