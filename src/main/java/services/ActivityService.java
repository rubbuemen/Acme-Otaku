
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ActivityRepository;
import domain.Activity;
import domain.Actor;
import domain.Enrolment;
import domain.Member;
import domain.Score;

@Service
@Transactional
public class ActivityService {

	// Managed repository
	@Autowired
	private ActivityRepository	activityRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private MemberService		memberService;


	// Simple CRUD methods
	//R14.2
	public Activity create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.notNull(memberLogged.getAssociation(), "You need to belong to an association to manage activities");

		Activity result;

		result = new Activity();
		final Collection<Score> scores = new HashSet<>();
		final Collection<Enrolment> enrolments = new HashSet<>();

		result.setScores(scores);
		result.setEnrolments(enrolments);
		result.setIsFinalMode(false);
		result.setIsFinished(false);

		return result;
	}

	public Collection<Activity> findAll() {
		Collection<Activity> result;

		result = this.activityRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Activity findOne(final int activityId) {
		Assert.isTrue(activityId != 0);

		Activity result;

		result = this.activityRepository.findOne(activityId);
		Assert.notNull(result);

		return result;
	}

	//R14.2
	public Activity save(final Activity activity) {
		Assert.notNull(activity);
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Activity result;

		final Member memberLogged = (Member) actorLogged;
		Assert.notNull(memberLogged.getAssociation(), "You need to belong to an association to manage activities");

		Assert.isTrue(!activity.getIsFinalMode(), "You can only save activities that are not in final mode");

		final Date currentMoment = new Date(System.currentTimeMillis());
		if (activity.getDeadline() != null)
			Assert.isTrue(activity.getDeadline().compareTo(currentMoment) > 0, "The deadline must be future");

		if (activity.getId() == 0) {
			result = this.activityRepository.save(activity);
			final Collection<Activity> activitiesMemberLogged = memberLogged.getActivities();
			activitiesMemberLogged.add(result);
			memberLogged.setActivities(activitiesMemberLogged);
			this.memberService.saveAuxiliar(memberLogged);
		} else {
			final Member memberOwner = this.memberService.findMemberByActivityId(activity.getId());
			Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");
			result = this.activityRepository.save(activity);
		}

		return result;
	}

	//R14.2
	public void delete(final Activity activity) {
		Assert.notNull(activity);
		Assert.isTrue(activity.getId() != 0);
		Assert.isTrue(this.activityRepository.exists(activity.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberOwner = this.memberService.findMemberByActivityId(activity.getId());
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!activity.getIsFinalMode(), "You can only delete activities that are not in final mode");

		final Member memberLogged = (Member) actorLogged;

		final Collection<Activity> activitiesActorLogged = memberLogged.getActivities();
		activitiesActorLogged.remove(activity);
		memberLogged.setActivities(activitiesActorLogged);
		this.memberService.saveAuxiliar(memberLogged);

		this.activityRepository.delete(activity);
	}

	public void deleteAuxiliar(final Activity activity) {
		Assert.notNull(activity);
		Assert.isTrue(activity.getId() != 0);
		Assert.isTrue(this.activityRepository.exists(activity.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		final Member memberLogged = (Member) actorLogged;

		final Collection<Activity> activitiesActorLogged = memberLogged.getActivities();
		activitiesActorLogged.remove(activity);
		memberLogged.setActivities(activitiesActorLogged);
		this.memberService.saveAuxiliar(memberLogged);

		this.activityRepository.delete(activity);
	}

	// Other business methods
	//R14.2
	public Collection<Activity> findActivitiesByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Activity> result;

		final Member memberLogged = (Member) actorLogged;
		Assert.notNull(memberLogged.getAssociation(), "You need to belong to an association to manage activities");

		result = this.activityRepository.findActivitiesByMemberId(memberLogged.getId());
		Assert.notNull(result);

		return result;
	}

	//R14.2
	public Activity changeFinalMode(final Activity activity) {
		Assert.notNull(activity);
		Assert.isTrue(activity.getId() != 0);
		Assert.isTrue(this.activityRepository.exists(activity.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Activity result;

		final Member memberOwner = this.memberService.findMemberByActivityId(activity.getId());
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!activity.getIsFinalMode(), "This activity is already in final mode");
		activity.setIsFinalMode(true);

		result = this.activityRepository.save(activity);

		return result;
	}

	//R14.2
	public Activity finish(final Activity activity) {
		Assert.notNull(activity);
		Assert.isTrue(activity.getId() != 0);
		Assert.isTrue(this.activityRepository.exists(activity.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Activity result;

		final Member memberOwner = this.memberService.findMemberByActivityId(activity.getId());
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(activity.getIsFinalMode(), "Cannot finish an activity that is not in final mode");

		Assert.isTrue(!activity.getIsFinished(), "This activity is already finished");
		activity.setIsFinished(true);

		result = this.activityRepository.save(activity);

		return result;
	}

	public Activity findActivityMemberLogged(final int activityId) {
		Assert.isTrue(activityId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberOwner = this.memberService.findMemberByActivityId(activityId);
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Activity result;

		result = this.activityRepository.findOne(activityId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Activity> findActivitiesFinalModeByAssociationId(final int associationId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Activity> result;

		final Member memberLogged = (Member) actorLogged;

		result = this.activityRepository.findActivitiesFinalModeByAssociationId(memberLogged.getAssociation().getId());
		Assert.notNull(result);
		result.remove(memberLogged);

		return result;
	}

	//R15.1
	public Collection<Activity> findActivitiesFinalModeNotFinishedByEventId(final int eventId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		Collection<Activity> result;

		result = this.activityRepository.findActivitiesFinalModeNotFinishedByEventId(eventId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Activity reconstruct(final Activity activity, final BindingResult binding) {
		Activity result;

		if (activity.getId() == 0) {
			final Collection<Score> scores = new HashSet<>();
			final Collection<Enrolment> enrolments = new HashSet<>();
			activity.setScores(scores);
			activity.setEnrolments(enrolments);
			activity.setIsFinalMode(false);
			activity.setIsFinished(false);
			result = activity;
		} else {
			final Activity originalactivity = this.activityRepository.findOne(activity.getId());
			Assert.notNull(originalactivity, "This entity does not exist");
			result = activity;
			result.setScores(originalactivity.getScores());
			result.setEnrolments(originalactivity.getEnrolments());
			result.setIsFinalMode(originalactivity.getIsFinalMode());
			result.setIsFinished(originalactivity.getIsFinished());
		}

		this.validator.validate(result, binding);

		this.activityRepository.flush();

		return result;
	}

	public void flush() {
		this.activityRepository.flush();
	}

}
