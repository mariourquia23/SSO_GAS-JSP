<%-- 
    Document   : response
    Created on : Sep 4, 2014, 3:25:28 PM
    Author     : JoseMario
--%>

<%@page import="java.util.logging.Logger"%>
<%@page import="java.net.MalformedURLException"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.net.URL"%>
<%@page import="javax.net.ssl.HttpsURLConnection"%>
<%@page import="com.Greenlt.SSO_GAS.*"%>
<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%

    //decalaracion de variables
    boolean respRSA = false;
    String GoAnyTargetURL = null; //"https://labsserver:4331/webclient/WebApplet.jsf";
    String textBoxInfo = "";
    String redirec = "";
    Logger logger = Logger.getLogger(this.getClass().getName());
    String titleInfoArea = "";
    String params="";
    boolean useRequestURL=false;
    
    //Borrar cookie en el navegador
    Cookie killMyCookie = new Cookie("JSESSIONID", null);
    killMyCookie.setMaxAge(0);
    killMyCookie.setPath("/");
    response.addCookie(killMyCookie);

    //recibir parametros
    Map<String,String[]> parameters=request.getParameterMap();
    for (Map.Entry<String,String[]> entry:parameters.entrySet()){
        params+=" "+entry.getKey()+"\n";
    }
    
    if (request.getParameter("token") != null && request.getParameter("token").length() > 0) {
        //Recibiendo Parametros con Token RSA
        String user = request.getParameter("username");
        String token = request.getParameter("token");
        textBoxInfo += String.format("\nUsuario: %s y token: %d recibidos", user, token.length());
        logger.info(String.format("Usuario: %s y token: %d, recibidos", user, token.length()));
        SSO_GLT sso = new SSO_GLT(user, token, false);
        logger.info("\n URL Recibida:\n"+request.getRequestURL());
        logger.info("\n Parametros Recibidos:\n"+params);
        //valida si va utilizar URL de la peticion        
        useRequestURL=(sso.getProperty("useRequestURL").toLowerCase().trim().hashCode() == "yes".hashCode()?true:false);
        if ( useRequestURL) {
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String path = "";            
            URL requestUrl = new URL(scheme,serverName,serverPort,path);
            sso.setRequestUrl(requestUrl);

        }
        respRSA = sso.autenticarRSAWSDL();
        textBoxInfo += (respRSA) ? "\nRespuesta de WS RSA: Autenticacion Exitosa" : "\nRespuesta de WS RSA: Autenticacion Fallida";
        logger.info(String.format("Respuesta de WS RSA: %b", respRSA));
        //agregar respuesta a log resRSA
        if (respRSA) {
            String PasswordLDAP = sso.LDAPSearch();
            textBoxInfo += (PasswordLDAP != "error") ? String.format("\nContraseña de LDAP: %d", PasswordLDAP.length()) : String.format("\nError al recuperar Contraseña de LDAP");
            logger.info(String.format("Contraseña de LDAP: %d", PasswordLDAP.length()));
            //escribir el tamaño del pass a log.
            String respGoAny = sso.autenticarGoAny();
            titleInfoArea = respGoAny;
            textBoxInfo += String.format("\nRespuesta de GoanyWhereServices: %s", respGoAny);
            logger.info(String.format("\nRespuesta de GoanyWhereServices: %s", respGoAny));
            if (respGoAny.indexOf("200 Welcome") > -1) {
                GoAnyTargetURL = ( useRequestURL) ? sso.getRequestUrlTarget() : sso.getProperty("GoAnyWhere_TargetURL");
                GoAnyTargetURL += ";jsessionid=" + sso.getJsessionID();
                //redirec = String.format("<meta http-equiv=\"refresh\" content=\"5; url=%s\" />", GoAnyTargetURL);
                redirec = GoAnyTargetURL;
                //crear nueva Cookie
                Cookie jsessionid = new Cookie("JSESSIONID", sso.getJsessionID());
                jsessionid.setMaxAge(60);
                jsessionid.setPath("/");
                response.addCookie(jsessionid);

            }
        }
    } else if (request.getParameter("password") != null && request.getParameter("password").length() > 0) {
        //Recibiendo Parametros con Integracion por Post
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        textBoxInfo += String.format("\nUsuario: %s y Contraseña: %d recibidos", user, pass.length());
        logger.info(String.format("Usuario: %s y Contraseña: %d, recibidos", user, pass.length()));
        SSO_GLT sso = new SSO_GLT(user, pass, true);
        logger.info("\n URL Recibida:\n"+request.getRequestURL());
        logger.info("\n Parametros Recibidos:\n"+params);
        
        //valida si va utilizar URL de la peticion
         useRequestURL=(sso.getProperty("useRequestURL").toLowerCase().trim().hashCode() == "yes".hashCode()?true:false);
        if ( useRequestURL) {       
            String scheme = request.getScheme();
            String serverName = request.getServerName();
            int serverPort = request.getServerPort();
            String path = "";
            URL requestUrl = new URL(scheme,serverName,serverPort,path);
            sso.setRequestUrl(requestUrl);

        }
        String respGoAny = sso.autenticarGoAny();
        titleInfoArea = respGoAny;
        textBoxInfo += String.format("\nRespuesta de GoanyWhereServices: %s", respGoAny);
        logger.info(String.format("\nRespuesta de GoanyWhereServices: %s", respGoAny));
        if (respGoAny.indexOf("200 Welcome") > -1) {

            GoAnyTargetURL = (useRequestURL) ? sso.getRequestUrlTarget() : sso.getProperty("GoAnyWhere_TargetURL");
            GoAnyTargetURL += ";jsessionid=" + sso.getJsessionID();
            //redirec = String.format("<meta http-equiv=\"refresh\" content=\"5; url=%s\" />", GoAnyTargetURL);
            redirec = GoAnyTargetURL;

            //crear nueva Cookie
            Cookie jsessionid = new Cookie("JSESSIONID", sso.getJsessionID());
            jsessionid.setMaxAge(60);
            jsessionid.setPath("/");
            response.addCookie(jsessionid);

        }

    } else {
        textBoxInfo = "No se recibieron los parametros Correctos para SSO";
        textBoxInfo+="\n URL Recibida:\n"+request.getRequestURL();
        textBoxInfo+="\n Parametros Recibidos:\n"+params;
        logger.info("\n URL Recibida:\n"+request.getRequestURL());
        logger.info("\n Parametros Recibidos:\n"+params);
            }

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SSO_GAS_V1R10</title>

        <style type="text/css">

            /* Basics */
            html, body {
                width: 100%;
                height: 100%;
                background-color:#fff;


            }

            #panel {
                position: absolute;
                left: 50%;
                top: 50%;
                height: 450px;
                width: 680px;
                margin-top: -225px;
                margin-left: -340px;
                overflow: hidden;
                /*border: 2px solid Red;*/
                text-align: center;
            }
            #container {
                padding-right:1px;
                width: 675px;
                height: 300px;
                background-color: #f3f8ff;
                border: 1px solid #93b8d8;
                border-radius: 3px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, .1);
                text-align: center;
            }

            #infoArea {
                height: 200px;
                width: 95%;
                background-color: #FFFFFF;
                border: 1px solid #93b8d8;
                border-radius: 3px;
                box-shadow: 0 1px 2px rgba(0, 0, 0, .1)
            }
            label {
                color: #555;
                display: inline-block;
                margin-left: 18px;
                padding-top: 10px;
                font-size: 14px;
            }
            p a {
                font-size: 11px;
                color: #aaa;
                float: right;
                margin-top: -13px;
                margin-right: 20px;
                -webkit-transition: all .4s ease;
                -moz-transition: all .4s ease;
                transition: all .4s ease;
            }
            p a:hover {
                color: #555;
            }
            input {
                font-family: "Helvetica Neue", Helvetica, sans-serif;
                font-size: 12px;
                outline: none;
                border:hidden;
            }
        </style> 
        <!--<%=redirec%>-->

    </head>
    <body>
        <div id="panel">
            
           

            <% if (redirec.length() > 1) {%>
            <h2>Procesando Solicitud</h2>
            <br />
            <img src="img/gears2.gif" />
            <br />
            <br />
            <br />

             <% String rdirect=SSO_GLT.getUrlFile(GoAnyTargetURL);
                out.print("Espere unos segundos, sino de <a href=\"" + rdirect + "\">clic aquí</a>."); %>
            
            <!-- redireccionador JavaScript-->
            <script type="text/javascript">
                    window.location = "<%=rdirect%>";//GoAnyTargetURL
                </script>
            
            <%} else {%>
             <img src="img/logo.png" width="70" height="70" alt="logo"/>
             
            <br />
            <br />
            <div id="container">
                <h2>INFORMACION</h2>
                <textarea name="infoArea" id="infoArea" rows="4" cols="20" readonly="readonly" title="<%=titleInfoArea%>" ><%=textBoxInfo%>
                   
                </textarea>

            </div>
            <div>
                
                <a href="index.html">Autenticar con Token RSAs</a>
                                    
                       <% //out.print(" | <a href=\"" + SSO_GLT.getUrlFile(GoAnyTargetURL) + "\">Ir a GTA</a>");
                        //response.sendRedirect(GoAnyTargetURL);
                %>
                
                
                                
                <%
                //RequestDispatcher d=request.getRequestDispatcher(SSO_GLT.getUrlFile(GoAnyTargetURL));
                    
                %>

            </div>
                <% }%>
        </div>
        <br>

    </body>
</html>
