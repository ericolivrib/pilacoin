<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <head>
        <title>Pilacoin | Página principal</title>
        <link rel="stylesheet" href="../css/style.css" />
    </head>
    <body>
        <sec:authorize access="isAuthenticated()">
            <h2>Bem vindo, <sec:authentication property="principal.username"/></h2>

<%--            <hr style="margin-bottom: 20px" />--%>

            <div>
                <strong style="color: #0000FF">
                    Clique em 1 botão de cada vez, e finalize o processo para iniciar um novo. =)
                </strong>

                <p>
                    <strong>Pilacoins</strong>
                    <a href="/pilacoin/minerar-pilacoins" style="text-decoration: none">
                        <button>Minerar pilacoins</button>
                    </a>
                    <a href="/pilacoin/validar-pilacoins" style="text-decoration: none">
                        <button>Validar pilacoins</button>
                    </a>
                    <a href="/pilacoin/transferir-pilacoin" style="text-decoration: none">
                        <button>Transferir pilacoin</button>
                    </a>
                </p>

                <p>
                    <strong>Blocos</strong>
                    <a href="/pilacoin/minerar-blocos" style="text-decoration: none">
                        <button>Minerar blocos</button>
                    </a>
                    <a href="/pilacoin/validar-blocos" style="text-decoration: none">
                        <button>Validar blocos</button>
                    </a>
                </p>

                <hr />

                <table>
                    <thead>
                    <tr>
                        <th>Nonce</th>
                        <th>Data de criação</th>
                        <th>Status</th>
<%--                        <th>Transferir</th>--%>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="pilacoins" scope="session" />
                    <c:forEach var="p" items="${pilacoins}">
                        <tr>
                            <td>${p.nonce}</td>
                            <td>${p.dataCriacao}</td>
                            <td>${p.status}</td>
<%--                            <td>--%>
<%--                                <a href="/pilacoin/transferir-pilacoin">Transferir</a>--%>
<%--                            </td>--%>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

            <div>
                <%-- Processos em andamento --%>

                <c:set var="minerandoPilacoins" scope="session" />
                <c:if test="${minerandoPilacoins}">
                    <p>Minerando pilacoins...</p>
                    <a href="/pilacoin/parar-mineracao-pilacoins">
                        <button>Parar</button>
                    </a>
                </c:if>

                <c:set var="validandoPilacoins" scope="session" />
                <c:if test="${validandoPilacoins}">
                    <p>Validando pilacoins...</p>
                    <a href="/pilacoin/parar-validacao-pila">
                        <button>Parar</button>
                    </a>
                </c:if>

                <c:set var="minerandoBlocos" scope="session" />
                <c:if test="${minerandoBlocos}">
                    <p>Minerando blocos...</p>
                    <a href="/pilacoin/parar-mineracao-blocos">
                        <button>Parar</button>
                    </a>
                </c:if>

                <c:set var="validandoBlocos" scope="session" />
                <c:if test="${validandoBlocos}">
                    <p>Validando blocos...</p>
                    <a href="/pilacoin/parar-validacao-blocos">
                        <button>Parar</button>
                    </a>
                </c:if>

<%--                <c:set var="transferindoPilacoin" scope="session" />--%>
<%--                <c:if test="${transferindoPilacoin}">--%>
<%--                    <p>Transferindo pilacoin...</p>--%>
<%--                    <a href="/pilacoin/parar-transferencia-pila">--%>
<%--                        <button>Parar</button>--%>
<%--                    </a>--%>
<%--                </c:if>--%>

                <%-- Parada de processos --%>

                <c:set var="mpf" scope="session" />
                <c:if test="${mpf}">
                    <strong>Mineração de pilacoins finalizada.</strong>
                    <p>Nonces minerados:</p>
                    <a href="/home">
                        <button>Recarregar tabela</button>
                    </a>
                    <c:set var="nonces" scope="session" />
                    <ul>
                        <c:forEach var="n" items="${nonces}">
                            <li>${n}</li>
                        </c:forEach>
                        <c:if test="${nonces.size() == 0}">
                            Nenhum pila foi minerado!
                        </c:if>
                    </ul>
                </c:if>

                <c:set var="vpf" scope="session" />
                <c:if test="${vpf}">
                    <strong>Validação de pilacoins finalizada.</strong>
                    <p>Nonces validados:</p>
                    <a href="/home">
                        <button>Recarregar tabela</button>
                    </a>
                    <c:set var="nonces" scope="session" />
                    <ul>
                        <c:forEach var="n" items="${nonces}">
                            <li>${n}</li>
                        </c:forEach>
                        <c:if test="${nonces.size() == 0}">
                            Nenhum pila foi validado!
                        </c:if>
                    </ul>
                </c:if>

                <c:set var="mbf" scope="session" />
                <c:if test="${mbf}">
                    <strong>Mineração de blocos finalizada.</strong>
                    <p>Nonces minerados:</p>
                    <a href="/home">
                        <button>Recarregar tabela</button>
                    </a>
                    <c:set var="nonces" scope="session" />
                    <ul>
                        <c:forEach var="n" items="${nonces}">
                            <li>${n}</li>
                        </c:forEach>
                        <c:if test="${nonces.size() == 0}">
                            Nenhum bloco foi minerado!
                        </c:if>
                    </ul>
                </c:if>

                <c:set var="vbf" scope="session" />
                <c:if test="${vbf}">
                    <strong>Validação de blocos finalizada.</strong>
                    <p>Nonces validados:</p>
                    <a href="/home">
                        <button>Recarregar tabela</button>
                    </a>
                    <c:set var="nonces" scope="session" />
                    <ul>
                        <c:forEach var="n" items="${nonces}">
                            <li>${n}</li>
                        </c:forEach>
                        <c:if test="${nonces.size() == 0}">
                            Nenhum bloco foi validado!
                        </c:if>
                    </ul>
                </c:if>

<%--                <c:set var="tpf" scope="session" />--%>
<%--                <c:if test="${tpf}">--%>
<%--                    <strong>Transferência de pilacoin finalizada.</strong>--%>
<%--                    <p>Nonces transferidos:</p>--%>
<%--                    <a href="/home">--%>
<%--                        <button>Recarregar tabela</button>--%>
<%--                    </a>--%>
<%--                    <c:set var="nonces" scope="session" />--%>
<%--                    <ul>--%>
<%--                        <c:forEach var="n" items="${nonces}">--%>
<%--                            <li>${n}</li>--%>
<%--                        </c:forEach>--%>
<%--                        <li></li>--%>
<%--                    </ul>--%>
<%--                </c:if>--%>
            </div>
        </sec:authorize>

        <sec:authorize access="!isAuthenticated()">
            <p>Faça log-in para acessar o sistema.</p>
            <a href="${pageContext.request.contextPath}/login" style="text-decoration: none">Fazer login</a>
        </sec:authorize>
    </body>
</html>
