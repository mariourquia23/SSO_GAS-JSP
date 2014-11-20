
package co.gov.banrep.s3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para autenticarRSA complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="autenticarRSA">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datosPeticion" type="{http://banrep.gov.co/s3}peticionWSAutenticarUsuarioRSA" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "autenticarRSA", propOrder = {
    "datosPeticion"
})
public class AutenticarRSA {

    protected PeticionWSAutenticarUsuarioRSA datosPeticion;

    /**
     * Obtiene el valor de la propiedad datosPeticion.
     * 
     * @return
     *     possible object is
     *     {@link PeticionWSAutenticarUsuarioRSA }
     *     
     */
    public PeticionWSAutenticarUsuarioRSA getDatosPeticion() {
        return datosPeticion;
    }

    /**
     * Define el valor de la propiedad datosPeticion.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionWSAutenticarUsuarioRSA }
     *     
     */
    public void setDatosPeticion(PeticionWSAutenticarUsuarioRSA value) {
        this.datosPeticion = value;
    }

}
