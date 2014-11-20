
package com.Greenlt.SSO_GAS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="autenticarRSAResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "autenticarRSAResult"
})
@XmlRootElement(name = "autenticarRSAResponse")
public class AutenticarRSAResponse {

    protected boolean autenticarRSAResult;

    /**
     * Obtiene el valor de la propiedad autenticarRSAResult.
     * 
     */
    public boolean isAutenticarRSAResult() {
        return autenticarRSAResult;
    }

    /**
     * Define el valor de la propiedad autenticarRSAResult.
     * 
     */
    public void setAutenticarRSAResult(boolean value) {
        this.autenticarRSAResult = value;
    }

}
