<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="br">
    <head>
        <title>Pilacoin | Login</title>
    </head>
    <body>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <h2>Acessar o sistema</h2>

            <table>
                <tr>
                    <td><label for="email">E-mail: </label></td>
                    <td><input type="email" name="email" id="email" placeholder="email@example.com" required /></td>
                </tr>
                <tr>
                    <td><label for="senha">Senha: </label></td>
                    <td><input type="password" name="senha" id="senha" required /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><button type="submit">Acessar</button></td>
                </tr>
            </table>

            <c:set var="erro" scope="session" />
            <c:if test="${erro}">
                <p style="color: #FF0000">Usuário ou senha incorretos!</p>
            </c:if>
        </form>
    </body>
</html>
