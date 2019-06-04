
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "isAllowedToJoin")
})
public class Association extends DomainEntity {

	// Attributes
	private String	name;
	private String	description;
	private String	slogan;
	private String	logo;
	private Boolean	isAllowedToJoin;
	private String	representativeColor;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSlogan() {
		return this.slogan;
	}

	public void setSlogan(final String slogan) {
		this.slogan = slogan;
	}

	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getLogo() {
		return this.logo;
	}

	public void setLogo(final String logo) {
		this.logo = logo;
	}

	@NotNull
	public Boolean getIsAllowedToJoin() {
		return this.isAllowedToJoin;
	}

	public void setIsAllowedToJoin(final Boolean isAllowedToJoin) {
		this.isAllowedToJoin = isAllowedToJoin;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Pattern(regexp = "^[#]([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
	public String getRepresentativeColor() {
		return this.representativeColor;
	}

	public void setRepresentativeColor(final String representativeColor) {
		this.representativeColor = representativeColor;
	}


	// Relationships
	private Collection<Application>	applications;


	@Valid
	@EachNotNull
	@OneToMany(mappedBy = "association")
	public Collection<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

}
