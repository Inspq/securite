//
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.7 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2017.03.23 à 12:18:04 PM EDT 
//


package ca.qc.inspq.sx5.service.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour anonymous complex type.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hello" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "hello"
})
@XmlRootElement(name = "helloResponse")
public class HelloResponse {

    @XmlElement(required = true)
    protected String hello;

    /**
     * Obtient la valeur de la propriété hello.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHello() {
        return hello;
    }

    /**
     * Définit la valeur de la propriété hello.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHello(String value) {
        this.hello = value;
    }

}
