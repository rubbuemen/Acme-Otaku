
package forms;

import javax.validation.Valid;

import domain.Seller;

public class SellerForm {

	// Attributes
	@Valid
	private Seller	actor;
	private String	passwordCheck;
	private Boolean	termsConditions;


	// Constructors
	public SellerForm() {
		super();
	}

	public SellerForm(final Seller actor) {
		this.actor = actor;
		this.passwordCheck = "";
		this.termsConditions = false;
	}

	// Getters and Setters
	public Seller getActor() {
		return this.actor;
	}

	public void setActor(final Seller actor) {
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
