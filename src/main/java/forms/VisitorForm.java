
package forms;

import javax.validation.Valid;

import domain.Visitor;

public class VisitorForm {

	// Attributes
	@Valid
	private Visitor	actor;
	private String	passwordCheck;
	private Boolean	termsConditions;


	// Constructors
	public VisitorForm() {
		super();
	}

	public VisitorForm(final Visitor actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Visitor getActor() {
		return this.actor;
	}

	public void setActor(final Visitor actor) {
		this.actor = actor;
	}

	public String getPasswordCheck() {
		return this.passwordCheck;
	}

	public void setPasswordCheck(final String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public Boolean getTermsConditions() {
		return this.termsConditions;
	}

	public void setTermsConditions(final Boolean termsConditions) {
		this.termsConditions = termsConditions;
	}

}
