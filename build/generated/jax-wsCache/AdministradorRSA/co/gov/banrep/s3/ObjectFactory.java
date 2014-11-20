
package co.gov.banrep.s3;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the co.gov.banrep.s3 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AdministradorRSAException_QNAME = new QName("http://banrep.gov.co/s3", "AdministradorRSAException");
    private final static QName _AutenticarRSA_QNAME = new QName("http://banrep.gov.co/s3", "autenticarRSA");
    private final static QName _AutenticarRSAResponse_QNAME = new QName("http://banrep.gov.co/s3", "autenticarRSAResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: co.gov.banrep.s3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AutenticarRSAResponse }
     * 
     */
    public AutenticarRSAResponse createAutenticarRSAResponse() {
        return new AutenticarRSAResponse();
    }

    /**
     * Create an instance of {@link AutenticarRSA }
     * 
     */
    public AutenticarRSA createAutenticarRSA() {
        return new AutenticarRSA();
    }

    /**
     * Create an instance of {@link AdministradorRSAException }
     * 
     */
    public AdministradorRSAException createAdministradorRSAException() {
        return new AdministradorRSAException();
    }

    /**
     * Create an instance of {@link MensajeBase }
     * 
     */
    public MensajeBase createMensajeBase() {
        return new MensajeBase();
    }

    /**
     * Create an instance of {@link PeticionWSAutenticarUsuarioRSA }
     * 
     */
    public PeticionWSAutenticarUsuarioRSA createPeticionWSAutenticarUsuarioRSA() {
        return new PeticionWSAutenticarUsuarioRSA();
    }

    /**
     * Create an instance of {@link RespuestaWSAutenticarUsuarioRSA }
     * 
     */
    public RespuestaWSAutenticarUsuarioRSA createRespuestaWSAutenticarUsuarioRSA() {
        return new RespuestaWSAutenticarUsuarioRSA();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdministradorRSAException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://banrep.gov.co/s3", name = "AdministradorRSAException")
    public JAXBElement<AdministradorRSAException> createAdministradorRSAException(AdministradorRSAException value) {
        return new JAXBElement<AdministradorRSAException>(_AdministradorRSAException_QNAME, AdministradorRSAException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AutenticarRSA }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://banrep.gov.co/s3", name = "autenticarRSA")
    public JAXBElement<AutenticarRSA> createAutenticarRSA(AutenticarRSA value) {
        return new JAXBElement<AutenticarRSA>(_AutenticarRSA_QNAME, AutenticarRSA.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AutenticarRSAResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://banrep.gov.co/s3", name = "autenticarRSAResponse")
    public JAXBElement<AutenticarRSAResponse> createAutenticarRSAResponse(AutenticarRSAResponse value) {
        return new JAXBElement<AutenticarRSAResponse>(_AutenticarRSAResponse_QNAME, AutenticarRSAResponse.class, null, value);
    }

}
