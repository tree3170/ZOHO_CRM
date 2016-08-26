package darlen.crm.jaxb_xml_object.t3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "provCity" })
public class Province {

	@XmlElement(name = "province_name")
	private String name;

	@XmlElement(name = "prov_city")
	private String provCity;

	/**
	 * @return the provCity
	 */
	public String getProvCity() {
		return provCity;
	}

	/**
	 * @param provCity the provCity to set
	 */
	public void setProvCity(String provCity) {
		this.provCity = provCity;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Province [name=" + name + ", provCity=" + provCity + "]";
	}

}
