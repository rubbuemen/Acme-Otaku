
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
	private Collection<Stand>	stands;


	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Stand> getStands() {
		return this.stands;
	}

	public void setStands(final Collection<Stand> stands) {
		this.stands = stands;
	}

}
