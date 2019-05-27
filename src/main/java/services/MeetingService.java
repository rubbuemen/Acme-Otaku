
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MeetingRepository;
import domain.Actor;
import domain.Meeting;
import domain.Member;

@Service
@Transactional
public class MeetingService {

	// Managed repository
	@Autowired
	private MeetingRepository	meetingRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private MemberService		memberService;


	// Simple CRUD methods
	//R34.1
	public Meeting create() {
		Meeting result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association for manage meetings");

		result = new Meeting();
		return result;
	}

	public Collection<Meeting> findAll() {
		Collection<Meeting> result;

		result = this.meetingRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Meeting findOne(final int meetingId) {
		Assert.isTrue(meetingId != 0);

		Meeting result;

		result = this.meetingRepository.findOne(meetingId);
		Assert.notNull(result);

		return result;
	}

	//R34.1
	public Meeting save(final Meeting meeting) {
		Assert.notNull(meeting);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Meeting result;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association for manage meetings");

		if (meeting.getId() == 0) {
			result = this.meetingRepository.save(meeting);
			final Collection<Meeting> meetingsMemberLogged = memberLogged.getMeetings();
			meetingsMemberLogged.add(result);
			memberLogged.setMeetings(meetingsMemberLogged);
			this.memberService.saveAuxiliar(memberLogged);
		} else {
			final Member memberOwner = this.memberService.findMemberByMeetingId(meeting.getId());
			Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");
			result = this.meetingRepository.save(meeting);
		}

		return result;
	}

	//R34.1
	public void delete(final Meeting meeting) {
		Assert.notNull(meeting);
		Assert.isTrue(meeting.getId() != 0);
		Assert.isTrue(this.meetingRepository.exists(meeting.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberOwner = this.memberService.findMemberByMeetingId(meeting.getId());
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		final Member memberLogged = (Member) actorLogged;

		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association for manage meetings");

		final Collection<Meeting> meetingsActorLogged = memberLogged.getMeetings();
		meetingsActorLogged.remove(meeting);
		memberLogged.setMeetings(meetingsActorLogged);
		this.memberService.saveAuxiliar(memberLogged);

		this.meetingRepository.delete(meeting);
	}

	public void deleteAuxiliar(final Meeting meeting) {
		Assert.notNull(meeting);
		Assert.isTrue(meeting.getId() != 0);
		Assert.isTrue(this.meetingRepository.exists(meeting.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		final Member memberLogged = (Member) actorLogged;

		final Collection<Meeting> meetingsActorLogged = memberLogged.getMeetings();
		meetingsActorLogged.remove(meeting);
		memberLogged.setMeetings(meetingsActorLogged);
		this.memberService.saveAuxiliar(memberLogged);

		this.meetingRepository.delete(meeting);
	}

	// Other business methods
	//R34.1
	public Collection<Meeting> findMeetingsByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Meeting> result;

		final Member memberLogged = (Member) actorLogged;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association for manage meetings");

		result = this.meetingRepository.findMeetingsByMemberId(memberLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Meeting findMeetingMemberLogged(final int meetingId) {
		Assert.isTrue(meetingId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberOwner = this.memberService.findMemberByMeetingId(meetingId);
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Meeting result;

		result = this.meetingRepository.findOne(meetingId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Meeting reconstruct(final Meeting meeting, final BindingResult binding) {
		Meeting result;

		if (meeting.getId() == 0)
			result = meeting;
		else {
			final Meeting originalMeeting = this.meetingRepository.findOne(meeting.getId());
			Assert.notNull(originalMeeting, "This entity does not exist");
			result = meeting;
		}

		this.validator.validate(result, binding);

		this.meetingRepository.flush();

		return result;
	}

	public void flush() {
		this.meetingRepository.flush();
	}

}
