/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Greenlt.SSO_GAS;

import co.gov.banrep.s3.AdministradorRSAException_Exception;
import co.gov.banrep.s3.RespuestaWSAutenticarUsuarioRSA;
import com.linoma.dpa.util.GASFileUtilities;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.Properties;
import java.util.logging.Level;
/**
 *
 * @author JoseMario
 */
public class SSO_GLT {

    private final String user;
    private final String token;
    private String pass;
    private String JsessionID;    
    private Logger logger = Logger.getLogger(this.getClass().getName());
    Properties prop=new Properties();
    private URL RequestUrl=null;

    public SSO_GLT(String username, String passwordOToken, boolean conPassword){
        this.user=username.trim();
        if (conPassword==true){
        this.pass=passwordOToken.trim();
        this.token="";
        }else 
        {
            this.token = passwordOToken.trim();
        }
        LoadPropertiesFile();
    }
    
    public Boolean autenticarRSAWSDL() {
        try {
        
        //GLT
        //inicio        
//        com.Greenlt.SSO_GAS.EmulacionRSA service = new com.Greenlt.SSO_GAS.EmulacionRSA();
//        com.Greenlt.SSO_GAS.EmulacionRSASoap port = service.getEmulacionRSASoap();
//        return port.autenticarRSA(Integer.parseInt(this.token), this.user);
        //fin  
        //Banrep        
        //inicio
        co.gov.banrep.s3.PeticionWSAutenticarUsuarioRSA peticion= new  co.gov.banrep.s3.PeticionWSAutenticarUsuarioRSA();
        co.gov.banrep.s3.RespuestaWSAutenticarUsuarioRSA resp;
        co.gov.banrep.s3.AdministradorRSAWS_Service service = new co.gov.banrep.s3.AdministradorRSAWS_Service();
        co.gov.banrep.s3.AdministradorRSAWS port = service.getAdministradorRSAWSPort();
        
        peticion.setUsuario(this.user.trim());
        peticion.setPassCode(this.token.trim());        
        resp= port.autenticarRSA(peticion);
        return resp.isResultado();
        //fin
        } catch (Exception e) {
            System.out.println(e.toString());
            logger.severe(e.toString());
            return false;
        }
        
}

    public String LDAPSearch() {
        try {

            Hashtable env = new Hashtable();

            //String ldap_Context="";
            String ldap_Host = getProperty("LDAP_Host");//"74.208.202.242";
            String ldap_Port = getProperty("LDAP_Port");//"389";
            String ldap_DN = getProperty("LDAP_DNBase");//"dc=maxcrc,dc=com";
            String ldap_attribute = getProperty("LDAP_Attribute");//"telephoneNumber";
            //      
            
            
            String sp = "com.sun.jndi.ldap.LdapCtxFactory";
            env.put(Context.INITIAL_CONTEXT_FACTORY, sp);

            String ldapUrl = String.format("ldap://%s:%s/%s", ldap_Host, ldap_Port, ldap_DN);
            env.put(Context.PROVIDER_URL, ldapUrl);

            DirContext dctx = new InitialDirContext(env);

            String base = "";

            SearchControls sc = new SearchControls();
            String[] attributeFilter = {ldap_attribute};
            sc.setReturningAttributes(attributeFilter);
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

            String filter = String.format("uid=%s", this.user);

            NamingEnumeration results = dctx.search(base, filter, sc);

            while (results.hasMore()) {
                SearchResult sr = (SearchResult) results.next();
                Attributes attrs = sr.getAttributes();

                Attribute attr = attrs.get(ldap_attribute);
                this.pass = attr.get().toString();
                return attr.get().toString();
            }
            dctx.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            logger.severe(e.toString());
        }
        return "error";
    }

    public String autenticarGoAny() {

        HttpsURLConnection conn = null;
        URL webClient;
        try {
            //Aceptar todos los certificados
            disableSslVerification();
            String authCredential = String.format("username=%s&password=%s", this.user, URLEncoder.encode(this.pass,"UTF-8"));
            //authCredential= URLEncoder.encode(authCredential, "UTF-8");
            webClient =(RequestUrl==null)? new URL(getProperty("GoAnyWhere_LoginURL")):RequestUrl;//"https://labsserver:4331/login"
            conn = (HttpsURLConnection) webClient.openConnection();
            conn.setRequestMethod("POST");
            
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            conn.setRequestProperty("Content-Length", ""
                    + Integer.toString(authCredential.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(
                    conn.getOutputStream());
            wr.writeBytes(authCredential);
            wr.flush();
            wr.close();
//            String responde="";
//            Map<String, List<String>> map = conn.getHeaderFields();
//            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//                responde+= String.format("\nKey : " + entry.getKey() + " , Value : " + entry.getValue());
//            }
//            
            String jsessionid = conn.getHeaderField("Set-Cookie");
            String XGDXReply = conn.getHeaderField("X-GDX-Reply");
            if (XGDXReply.indexOf("200 Welcome") > -1) {
                setJsessionID(jsessionid.substring(11, jsessionid.indexOf(";")));
            }
            return XGDXReply;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.severe(e.toString());
            return e.toString();

        } catch (Exception e) {
            logger.severe(e.toString());
            return e.toString();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    public static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {            
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public String getJsessionID() {
        return JsessionID;
    }

    private void setJsessionID(String jessionid) {
        this.JsessionID =jessionid;
    }
    private void LoadPropertiesFile(){        
        InputStream input=null;        
        try {
            input=new FileInputStream(GASFileUtilities.resolveFile("userdata/greenltsso/SSOconfig.properties"));
//            input=new FileInputStream("C:\\Users\\JoseMario\\Documents\\NetBeansProjects\\SSO_GAS\\web\\conf\\SSOconfig.properties");
            prop.load(input);   
             
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.severe(ex.toString());
            
           
        }finally{
            if (input!=null)
                try{
                    input.close();
                }catch(IOException e){
                    e.printStackTrace();
                    logger.severe(e.toString());
                    
                }
        }
    }
    
    public String getProperty(String prop){
             
        return this.prop.getProperty(prop);
    }
    public void setRequestUrl(URL url){
        try {
            URL fileUrl=new URL(prop.getProperty("GoAnyWhere_LoginURL"));
            this.RequestUrl=new URL(url.getProtocol(),url.getHost(),url.getPort(),fileUrl.getPath());
        } catch (Exception e) {
            logger.severe(e.toString());
            e.printStackTrace();
        }
        
    }
    public String getRequestUrlTarget(){
        try{
            
        
        
        URL fileUrl=new URL(prop.getProperty("GoAnyWhere_TargetURL"));
        URL newUrl= new URL(RequestUrl.getProtocol(),RequestUrl.getHost(),RequestUrl.getPort(),fileUrl.getPath());
        return newUrl.toString();
        }
        catch(MalformedURLException e){
            logger.severe(e.toString());
            e.printStackTrace();
        }
        return "";
    }
    public static String getUrlFile(String url){
        URL myUrl;
        try {
            myUrl = new URL(url);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SSO_GLT.class.getName()).log(Level.SEVERE, null, ex);
            return "Error";
                    
        }
        return myUrl.getPath();
    }
    
}
