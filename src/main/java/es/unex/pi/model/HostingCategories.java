package es.unex.pi.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HostingCategories {

	private long idh;
	private long idct;
	
	public long getIdh() {
		return idh;
	}
	
	public void setIdh(long idh) {
		this.idh = idh;
	}
	
	public long getIdct() {
		return idct;
	}
	
	public void setIdct(long idct) {
		this.idct = idct;
	}
}
