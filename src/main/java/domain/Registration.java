
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Registration extends DomainEntity {

	// Attributes
	private String	comment;
	private String	status;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Pattern(regexp = "^PENDING|ACCEPTED|DECLINED$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}


	// Relationships
	private Seller	seller;
	private Event	event;
	private Stand	stand;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Seller getSeller() {
		return this.seller;
	}

	public void setSeller(final Seller seller) {
		this.seller = seller;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Event getEvent() {
		return this.event;
	}

	public void setEvent(final Event event) {
		this.event = event;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Stand getStand() {
		return this.stand;
	}

	public void setStand(final Stand stand) {
		this.stand = stand;
	}

}
