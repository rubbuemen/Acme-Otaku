
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import cz.jirutka.validator.collection.constraints.EachNotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Member extends Actor {

	// Attributes
	private String	role;


	// Getters and Setters
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Pattern(regexp = "^PRESIDENT|VICEPRESIDENT|SECRETARY|TREASURE|VOCAL|MEMBER$")
	public String getRole() {
		return this.role;
	}

	public void setRole(final String role) {
		this.role = role;
	}


	// Relationships
	private Collection<Application>	applications;
	private Association				association;
	private Collection<Event>		events;
	private Collection<Activity>	activities;
	private Collection<Meeting>		meetings;
	private Collection<Headquarter>	headquarters;


	@Valid
	@EachNotNull
	@OneToMany(mappedBy = "member")
	public Collection<Application> getApplications() {
		return this.applications;
	}

	public void setApplications(final Collection<Application> applications) {
		this.applications = applications;
	}

	@Valid
	@ManyToOne(optional = true)
	public Association getAssociation() {
		return this.association;
	}

	public void setAssociation(final Association association) {
		this.association = association;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Event> getEvents() {
		return this.events;
	}

	public void setEvents(final Collection<Event> events) {
		this.events = events;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Activity> getActivities() {
		return this.activities;
	}

	public void setActivities(final Collection<Activity> activities) {
		this.activities = activities;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Meeting> getMeetings() {
		return this.meetings;
	}

	public void setMeetings(final Collection<Meeting> meetings) {
		this.meetings = meetings;
	}

	@Valid
	@EachNotNull
	@OneToMany
	public Collection<Headquarter> getHeadquarters() {
		return this.headquarters;
	}

	public void setHeadquarters(final Collection<Headquarter> headquarters) {
		this.headquarters = headquarters;
	}

}
