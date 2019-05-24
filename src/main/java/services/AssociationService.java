
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AssociationRepository;
import domain.Activity;
import domain.Actor;
import domain.Application;
import domain.Association;
import domain.Enrolment;
import domain.Event;
import domain.Headquarter;
import domain.Meeting;
import domain.Member;
import domain.Score;
import domain.Sponsor;
import domain.Sponsorship;
import domain.Stand;
import domain.Visitor;

@Service
@Transactional
public class AssociationService {

	// Managed repository
	@Autowired
	private AssociationRepository	associationRepository;

	// Supporting services
	@Autowired
	private MemberService			memberService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private EventService			eventService;

	@Autowired
	private ActivityService			activityService;

	@Autowired
	private MeetingService			meetingService;

	@Autowired
	private HeadquarterService		headquarterService;

	@Autowired
	private StandService			standService;

	@Autowired
	private SponsorshipService		sponsorshipService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private EnrolmentService		enrolmentService;

	@Autowired
	private VisitorService			visitorService;

	@Autowired
	private ScoreService			scoreService;

	@Autowired
	private ApplicationService		applicationService;


	// Simple CRUD methods
	//R12.1
	public Association create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.isNull(memberLogged.getAssociation(), "You already belong to an association");

		final Collection<Application> applications = new HashSet<>();

		Association result;

		result = new Association();

		result.setApplications(applications);
		result.setIsAllowedToJoin(false);

		return result;
	}

	public Collection<Association> findAll() {
		Collection<Association> result;

		result = this.associationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Association findOne(final int associationId) {
		Assert.isTrue(associationId != 0);

		Association result;

		result = this.associationRepository.findOne(associationId);
		Assert.notNull(result);

		return result;
	}

	//R12.1, R13.2
	public Association save(final Association association) {
		Assert.notNull(association);

		Association result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		if (association.getId() == 0) {
			Assert.isNull(memberLogged.getAssociation(), "You already belong to an association");
			result = this.associationRepository.save(association);
			memberLogged.setAssociation(result);
			memberLogged.setRole("PRESIDENT");
			this.memberService.saveAuxiliar(memberLogged);
		} else {
			Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
			Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");
			result = this.associationRepository.save(association);
		}

		return result;
	}

	public Association saveAuxiliar(final Association association) {
		Assert.notNull(association);

		Association result;

		result = this.associationRepository.save(association);

		return result;
	}

	public void delete(final Association association) {
		Assert.notNull(association);
		Assert.isTrue(association.getId() != 0);
		Assert.isTrue(this.associationRepository.exists(association.getId()));

		this.associationRepository.delete(association);
	}

	// Other business methods
	public Association findAssociationByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Association result;

		final Member memberLogged = (Member) actorLogged;

		result = this.associationRepository.findAssociationByMemberId(memberLogged.getId());

		return result;
	}

	public Association findAssociationMemberLogged(final int associationId) {
		Assert.isTrue(associationId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		final Association association = this.associationRepository.findOne(associationId);

		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
		Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");

		Association result;

		result = this.associationRepository.findOne(associationId);
		Assert.notNull(result);

		return result;
	}

	//R13.2
	public Association allowMembers(final Association association) {
		Association result;
		Assert.notNull(association);
		Assert.isTrue(association.getId() != 0);
		Assert.isTrue(this.associationRepository.exists(association.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Assert.isTrue(!association.getIsAllowedToJoin(), "This association already allows members");
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
		Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");

		association.setIsAllowedToJoin(true);

		result = this.associationRepository.save(association);

		return result;
	}

	//R13.2
	public Association notAllowMembers(final Association association) {
		Association result;
		Assert.notNull(association);
		Assert.isTrue(association.getId() != 0);
		Assert.isTrue(this.associationRepository.exists(association.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Assert.isTrue(association.getIsAllowedToJoin(), "This association no longer allows members");
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
		Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");

		association.setIsAllowedToJoin(false);

		result = this.associationRepository.save(association);

		return result;
	}

	//R13.1
	public Collection<Association> findAssociations() {
		Collection<Association> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		result = this.associationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Association> findAssociationsToJoin() {
		Collection<Association> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		result = this.associationRepository.findAssociationsToJoin();
		Assert.notNull(result);

		return result;
	}

	public Association findAssociationByEnrolmentId(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);
		Association result;

		result = this.associationRepository.findAssociationByEnrolmentId(enrolmentId);

		return result;
	}

	//R14.5
	public void leave(final Integer newPresidentId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		final Association association = memberLogged.getAssociation();

		final Collection<Member> membersAssociation = this.memberService.findMembersByAssociationMemberLogged();
		final Collection<Event> events = new HashSet<>(memberLogged.getEvents());
		final Collection<Activity> activities = new HashSet<>(memberLogged.getActivities());
		final Collection<Meeting> meetings = new HashSet<>(memberLogged.getMeetings());
		final Collection<Headquarter> headquarters = new HashSet<>(memberLogged.getHeadquarters());
		final Collection<Application> applicationsAssociation = new HashSet<>(memberLogged.getAssociation().getApplications());

		if (membersAssociation.size() == 1) {
			for (final Event e : events) {
				final Collection<Sponsorship> sponsorships = new HashSet<>(e.getSponsorships());
				final Collection<Stand> stands = new HashSet<>(this.standService.findStandsByEventId(e.getId()));
				for (final Sponsorship s : sponsorships) {
					final Sponsor sp = s.getSponsor();
					e.getSponsorships().remove(s);
					sp.getSponsorships().remove(s);
					this.sponsorshipService.deleteAuxiliar(s);
					this.sponsorService.saveAuxiliar(sp);
				}
				for (final Stand s : stands) {
					s.getEvents().clear();
					this.standService.saveAuxiliar(s);
				}
				this.eventService.deleteAuxiliar(e);
			}
			for (final Activity a : activities) {
				final Collection<Enrolment> enrolments = new HashSet<>(a.getEnrolments());
				for (final Enrolment e : enrolments) {
					final Visitor v = e.getVisitor();
					a.getEnrolments().remove(e);
					v.getEnrolments().remove(e);
					this.enrolmentService.deleteAuxiliar(e);
					this.visitorService.saveAuxiliar(v);
				}
				for (final Score s : a.getScores())
					this.scoreService.deleteAuxiliar(s);
				this.activityService.deleteAuxiliar(a);
			}
			for (final Meeting m : meetings)
				this.meetingService.deleteAuxiliar(m);
			for (final Headquarter h : headquarters)
				this.headquarterService.deleteAuxiliar(h);
			for (final Application a : applicationsAssociation) {
				final Member m = a.getMember();
				memberLogged.getAssociation().getApplications().remove(a);
				m.getApplications().remove(a);
				this.applicationService.deleteAuxiliar(a);
				this.memberService.saveAuxiliar(m);
			}
			memberLogged.setAssociation(null);
			memberLogged.setRole(null);
			this.memberService.saveAuxiliar(memberLogged);
			this.associationRepository.delete(association);

		} else if (memberLogged.getRole().equals("PRESIDENT")) {
			final Member newPresident = this.memberService.findOne(newPresidentId);
			Assert.isTrue(newPresident.getAssociation().equals(memberLogged.getAssociation()), "The selected member does not belong to your association");
			for (final Event e : events) {
				memberLogged.getEvents().remove(e);
				newPresident.getEvents().add(e);
			}
			for (final Activity e : activities) {
				memberLogged.getActivities().remove(e);
				newPresident.getActivities().add(e);
			}
			for (final Meeting e : meetings) {
				memberLogged.getMeetings().remove(e);
				newPresident.getMeetings().add(e);
			}
			for (final Headquarter e : headquarters) {
				memberLogged.getHeadquarters().remove(e);
				newPresident.getHeadquarters().add(e);
			}

			memberLogged.setAssociation(null);
			memberLogged.setRole(null);
			newPresident.setRole("PRESIDENT");
			this.memberService.saveAuxiliar(memberLogged);
			this.memberService.saveAuxiliar(newPresident);
		} else {
			final Member president = this.memberService.findPresidentByAssociationMemberLogged();
			for (final Event e : events) {
				memberLogged.getEvents().remove(e);
				president.getEvents().add(e);
			}
			for (final Activity e : activities) {
				memberLogged.getActivities().remove(e);
				president.getActivities().add(e);
			}
			for (final Meeting e : meetings) {
				memberLogged.getMeetings().remove(e);
				president.getMeetings().add(e);
			}
			for (final Headquarter e : headquarters) {
				memberLogged.getHeadquarters().remove(e);
				president.getHeadquarters().add(e);
			}

			memberLogged.setAssociation(null);
			memberLogged.setRole(null);
			this.memberService.saveAuxiliar(memberLogged);
			this.memberService.saveAuxiliar(president);
		}
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Association reconstruct(final Association association, final BindingResult binding) {
		Association result;

		if (association.getId() == 0) {
			final Collection<Application> applications = new HashSet<>();
			association.setApplications(applications);
			association.setIsAllowedToJoin(false);
			result = association;
		} else {
			final Association originalAssociation = this.associationRepository.findOne(association.getId());
			Assert.notNull(originalAssociation, "This entity does not exist");
			result = association;
			result.setApplications(originalAssociation.getApplications());
			result.setIsAllowedToJoin(originalAssociation.getIsAllowedToJoin());
		}

		this.validator.validate(result, binding);

		this.associationRepository.flush();

		return result;
	}

	public void flush() {
		this.associationRepository.flush();
	}

}
