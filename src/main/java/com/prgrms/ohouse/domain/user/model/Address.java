package com.prgrms.ohouse.domain.user.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {
	@Column(name = "city")
	private String city;
	@Column(name = "street")
	private String street;
	@Column(name = "detail")
	private String detail;
	@Column(name = "zipcode")
	private String zipcode;
	@Column(name = "lot_number_address")
	private String lotNumberAddress;

	@Override
	public String toString() {
		return new StringBuilder()
			.append("Address[city=" + city)
			.append(",street=" + street)
			.append(",detail=" + detail)
			.append(",zipcode=" + zipcode)
			.append(",lotNumberAddress="+lotNumberAddress)
			.append("]")
			.toString();
	}
}
