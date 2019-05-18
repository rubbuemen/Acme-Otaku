
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachNotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	// Attributes
	private String				nameSystem;
	private String				bannerUrl;
	private String				welcomeMessageEnglish;
	private String				welcomeMessageSpanish;
	private Collection<String>	spamWords;
	private Double				VATPercentage;
	private String				phoneCountryCode;
	private Collection<String>	creditCardMakes;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNameSystem() {
		return this.nameSystem;
	}

	public void setNameSystem(final String nameSystem) {
		this.nameSystem = nameSystem;
	}

	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBannerUrl() {
		return this.bannerUrl;
	}

	public void setBannerUrl(final String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageEnglish() {
		return this.welcomeMessageEnglish;
	}

	public void setWelcomeMessageEnglish(final String welcomeMessageEnglish) {
		this.welcomeMessageEnglish = welcomeMessageEnglish;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageSpanish() {
		return this.welcomeMessageSpanish;
	}

	public void setWelcomeMessageSpanish(final String welcomeMessageSpanish) {
		this.welcomeMessageSpanish = welcomeMessageSpanish;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	@EachNotBlank
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
		this.spamWords = spamWords;
	}

	@NotNull
	@Range(min = 0, max = 100)
	@Digits(integer = 2, fraction = 2)
	public Double getVATPercentage() {
		return this.VATPercentage;
	}

	public void setVATPercentage(final Double VATPercentage) {
		this.VATPercentage = VATPercentage;
	}
	

	@NotBlank
	@Pattern(regexp = "^[+][1-9]\\d{0,2}$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPhoneCountryCode() {
		return this.phoneCountryCode;
	}

	public void setPhoneCountryCode(final String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	@ElementCollection(fetch = FetchType.EAGER)
	@NotEmpty
	@EachNotBlank
	public Collection<String> getCreditCardMakes() {
		return this.creditCardMakes;
	}

	public void setCreditCardMakes(final Collection<String> creditCardMakes) {
		this.creditCardMakes = creditCardMakes;
	}


}
