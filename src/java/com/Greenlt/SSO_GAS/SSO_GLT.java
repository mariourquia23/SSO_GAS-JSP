/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Greenlt.SSO_GAS;

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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.logging.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.OutputStream;
import java.util.Properties;
import java.io.FileOutputStream;
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

    public SSO_GLT(String username, String token) {
        this.user = username.trim();
        this.token = token.trim();
        LoadPropertiesFile();
    }
    public SSO_GLT(String username, String password, boolean p){
        this.user=username.trim();
        this.pass=password.trim();
        this.token="";
        LoadPropertiesFile();
    }

    public Boolean autenticarRSAWSDL() {
        //GLT
        com.Greenlt.SSO_GAS.EmulacionRSA service = new com.Greenlt.SSO_GAS.EmulacionRSA();
        com.Greenlt.SSO_GAS.EmulacionRSASoap port = service.getEmulacionRSASoap();
        return port.autenticarRSA(Integer.parseInt(this.token), this.user);
        //Banrep      

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
            String authCredential = String.format("username=%s&password=%s", this.user, this.pass);
            webClient = new URL(getProperty("GoAnyWhere_LoginURL"));//"https://labsserver:4331/login"
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
            
            input=new FileInputStream("C:\\Users\\JoseMario\\Documents\\NetBeansProjects\\SSO_GAS\\web\\conf\\SSOconfig.properties");
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

}
