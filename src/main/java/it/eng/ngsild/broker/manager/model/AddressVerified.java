package it.eng.ngsild.broker.manager.model;

public class AddressVerified {
	Address value;
	Boolean verified = true;

	public Address getValue() {
		return value;
	}
	public void setValue(Address value) {
		this.value = value;
	}
	public Boolean getVerified() {
		return verified;
	}
	public void setVerified(Boolean verified) {
		this.verified = verified;
	}
	@Override
	public String toString() {
		return "[value=" + value + ", verified=" + verified + "]";
	}
	public AddressVerified(Address value, Boolean verified) {
		super();
		this.value = value;
		this.verified = verified;
	}
	

}
