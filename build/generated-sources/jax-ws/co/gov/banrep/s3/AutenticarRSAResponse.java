
package co.gov.banrep.s3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for autenticarRSAResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="autenticarRSAResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://banrep.gov.co/s3}respuestaWSAutenticarUsuarioRSA" minOccurs="0" form="qualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "autenticarRSAResponse", propOrder = {
    "_return"
})
public class AutenticarRSAResponse {

    @XmlElement(name = "return", namespace = "http://banrep.gov.co/s3")
    protected RespuestaWSAutenticarUsuarioRSA _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaWSAutenticarUsuarioRSA }
     *     
     */
    public RespuestaWSAutenticarUsuarioRSA getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaWSAutenticarUsuarioRSA }
     *     
     */
    public void setReturn(RespuestaWSAutenticarUsuarioRSA value) {
        this._return = value;
    }

}
