
package com.Greenlt.SSO_GAS;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
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
     * Gets the value of the autenticarRSAResult property.
     * 
     */
    public boolean isAutenticarRSAResult() {
        return autenticarRSAResult;
    }

    /**
     * Sets the value of the autenticarRSAResult property.
     * 
     */
    public void setAutenticarRSAResult(boolean value) {
        this.autenticarRSAResult = value;
    }

}
