
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Seller extends Actor {

	// Relationships
	private Collection<Registration>	registrations;
	private Collection<Stand>			stands;
	private Collection<Answer>			answers;


	@Valid
	@EachNotNull
	@OneToMany(mappedBy = "seller")
	public Collection<Registration> getRegistrations() {
		return this.registrations;
	}

	public void setRegistrations(final Collection<Registration> registrations) {
		this.registrations = registrations;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Stand> getStands() {
		return this.stands;
	}

	public void setStands(final Collection<Stand> stands) {
		this.stands = stands;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Answer> getAnswers() {
		return this.answers;
	}

	public void setAnswers(final Collection<Answer> answers) {
		this.answers = answers;
	}

}
