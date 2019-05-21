
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
public class Enrolment extends DomainEntity {

	// Attributes
	private String	comments;
	private String	status;


	// Getters and Setters
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComments() {
		return this.comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Pattern(regexp = "^PENDING|ACCEPTED|DECLINED|CANCELLED$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}


	// Relationships
	private Visitor		visitor;
	private Activity	activity;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Visitor getVisitor() {
		return this.visitor;
	}

	public void setVisitor(final Visitor visitor) {
		this.visitor = visitor;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(final Activity activity) {
		this.activity = activity;
	}

}
