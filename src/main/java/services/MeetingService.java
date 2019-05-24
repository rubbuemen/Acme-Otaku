
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
	public Meeting create() {
		Meeting result;

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

	public Meeting save(final Meeting meeting) {
		Assert.notNull(meeting);

		Meeting result;

		if (meeting.getId() == 0)
			result = this.meetingRepository.save(meeting);
		else
			result = this.meetingRepository.save(meeting);

		return result;
	}

	public void delete(final Meeting meeting) {
		Assert.notNull(meeting);
		Assert.isTrue(meeting.getId() != 0);
		Assert.isTrue(this.meetingRepository.exists(meeting.getId()));

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
