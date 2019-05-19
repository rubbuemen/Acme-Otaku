
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import cz.jirutka.validator.collection.constraints.EachNotBlank;
import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
public class Score extends DomainEntity {

	// Attributes
	private Integer				ranking;
	private String				comments;
	private Collection<String>	photos;
	private Collection<String>	attachments;


	// Getters and Setters
	@NotNull
	@Range(min = 1, max = 5)
	public Integer getRanking() {
		return this.ranking;
	}

	public void setRanking(final Integer ranking) {
		this.ranking = ranking;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getComments() {
		return this.comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	@ElementCollection
	@EachNotBlank
	@EachURL
	public Collection<String> getPhotos() {
		return this.photos;
	}

	public void setPhotos(final Collection<String> photos) {
		this.photos = photos;
	}

	@ElementCollection
	@EachNotBlank
	@EachURL
	public Collection<String> getAttachments() {
		return this.attachments;
	}

	public void setAttachments(final Collection<String> attachments) {
		this.attachments = attachments;
	}

}
