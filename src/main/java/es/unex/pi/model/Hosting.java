package es.unex.pi.model;

import java.util.Objects;

import jakarta.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Hosting {

	private long id;
	private String title;
	private String description;
	private String telephone;
	private String location;
	private String contactEmail;
	private int likes;
	private int available;
	private int idu;
	private int price;
	private String services;
	private String isFav;
	private String redSocial;
	
	public String getIsFav() {
		return isFav;
	}
	public void setIsFav(String isFav) {
		this.isFav = isFav;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int available) {
		this.available = available;
	}
	public int getIdu() {
		return idu;
	}
	public void setIdu(int idu) {
		this.idu = idu;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getServices() {
		return services;
	}
	public void setServices(String services) {
		this.services = services;
	}
	public String getRedSocial() {
		return redSocial;
	}
	public void setRedSocial(String redSocial) {
		this.redSocial = redSocial;
	}
	@Override
	public int hashCode() {
		return Objects.hash(available, contactEmail, description, id, idu, isFav, likes, location, price, redSocial,
				services, telephone, title);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hosting other = (Hosting) obj;
		return available == other.available && Objects.equals(contactEmail, other.contactEmail)
				&& Objects.equals(description, other.description) && id == other.id && idu == other.idu
				&& Objects.equals(isFav, other.isFav) && likes == other.likes
				&& Objects.equals(location, other.location) && price == other.price
				&& Objects.equals(redSocial, other.redSocial) && Objects.equals(services, other.services)
				&& Objects.equals(telephone, other.telephone) && Objects.equals(title, other.title);
	}

	
}
