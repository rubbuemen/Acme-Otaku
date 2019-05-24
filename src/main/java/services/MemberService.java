
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MemberRepository;
import security.Authority;
import security.UserAccount;
import domain.Activity;
import domain.Actor;
import domain.Application;
import domain.Event;
import domain.Headquarter;
import domain.Meeting;
import domain.Member;
import forms.MemberForm;

@Service
@Transactional
public class MemberService {

	// Managed repository
	@Autowired
	private MemberRepository	memberRepository;

	// Supporting services
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;


	// Simple CRUD methods
	// R10.1
	public Member create() {
		Member result;

		result = new Member();
		final Collection<Event> events = new HashSet<>();
		final Collection<Activity> activities = new HashSet<>();
		final Collection<Meeting> meetings = new HashSet<>();
		final Collection<Headquarter> headquarters = new HashSet<>();
		final Collection<Application> applications = new HashSet<>();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.MEMBER);
		userAccount.addAuthority(auth);
		result.setEvents(events);
		result.setActivities(activities);
		result.setMeetings(meetings);
		result.setHeadquarters(headquarters);
		result.setApplications(applications);
		result.setUserAccount(userAccount);
		result.setIsSuspicious(false);

		return result;
	}

	public Collection<Member> findAll() {
		Collection<Member> result;

		result = this.memberRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Member findOne(final int memberId) {
		Assert.isTrue(memberId != 0);

		Member result;

		result = this.memberRepository.findOne(memberId);
		Assert.notNull(result);

		return result;
	}

	// R10.1
	public Member save(final Member member) {
		Assert.notNull(member);

		Member result;

		result = (Member) this.actorService.save(member);
		result = this.memberRepository.save(result);

		return result;
	}

	public Member saveAuxiliar(final Member member) {
		Assert.notNull(member);

		Member result;

		result = this.memberRepository.save(member);

		return result;
	}

	public void delete(final Member member) {
		Assert.notNull(member);
		Assert.isTrue(member.getId() != 0);
		Assert.isTrue(this.memberRepository.exists(member.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		this.actorService.deleteEntities(memberLogged);

		//Completar

		this.memberRepository.flush();
		this.memberRepository.delete(member);
	}

	// Other business methods
	//R13.3
	public Collection<Member> findMembersByAssociationPresidentLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Member> result;

		final Member memberLogged = (Member) actorLogged;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association to see the members");

		result = this.memberRepository.findMembersByAssociationId(memberLogged.getAssociation().getId());
		Assert.notNull(result);
		result.remove(memberLogged);

		return result;
	}

	//R13.3
	public Member findMemberByPresidentLogged(final int memberId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member result;

		final Member memberLogged = (Member) actorLogged;
		result = this.memberRepository.findOne(memberId);
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association to see the members");
		Assert.isTrue(memberLogged.getAssociation().equals(result.getAssociation()), "This member do not belong to your association");

		return result;
	}

	//R13.3
	public Member changeRole(final Member member) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Member result;

		final Member memberLogged = (Member) actorLogged;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association to change a member's role");
		Assert.isTrue(memberLogged.getAssociation().equals(member.getAssociation()), "This member do not belong to your association");

		final Member memberPresident = this.memberRepository.findMemberPresidentByAssociationId(memberLogged.getAssociation().getId());
		final Member memberVicePresident = this.memberRepository.findMemberVicePresidentByAssociationId(memberLogged.getAssociation().getId());
		if (member.getRole().equals("PRESIDENT"))
			Assert.isNull(memberPresident, "There can only be one president in the association");
		if (member.getRole().equals("VICEPRESIDENT"))
			Assert.isNull(memberVicePresident, "There can only be one vice president in the association");

		result = this.memberRepository.save(member);

		return result;
	}

	public Member findMemberByEventId(final int eventId) {
		Assert.isTrue(eventId != 0);

		Member result;

		result = this.memberRepository.findMemberByEventId(eventId);
		Assert.notNull(result);

		return result;
	}

	public Member findMemberByActivityId(final int activityId) {
		Assert.isTrue(activityId != 0);

		Member result;

		result = this.memberRepository.findMemberByActivityId(activityId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Member> findMembersByAssociationMemberLogged() {
		Collection<Member> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		result = this.memberRepository.findMembersByAssociationId(memberLogged.getAssociation().getId());
		Assert.notNull(result);

		return result;
	}

	public Member findPresidentByAssociationMemberLogged() {
		Member result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		result = this.memberRepository.findMemberPresidentByAssociationId(memberLogged.getAssociation().getId());
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public MemberForm reconstruct(final MemberForm memberForm, final BindingResult binding) {
		MemberForm result;
		final Member member = memberForm.getActor();

		if (member.getId() == 0) {
			final Collection<Event> events = new HashSet<>();
			final Collection<Activity> activities = new HashSet<>();
			final Collection<Meeting> meetings = new HashSet<>();
			final Collection<Headquarter> headquarters = new HashSet<>();
			final Collection<Application> applications = new HashSet<>();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.MEMBER);
			userAccount.addAuthority(auth);
			userAccount.setUsername(memberForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(memberForm.getActor().getUserAccount().getPassword());
			member.setEvents(events);
			member.setActivities(activities);
			member.setMeetings(meetings);
			member.setHeadquarters(headquarters);
			member.setApplications(applications);
			member.setUserAccount(userAccount);
			member.setIsSuspicious(false);
			memberForm.setActor(member);
		} else {
			final Member res = this.memberRepository.findOne(member.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(member.getName());
			res.setMiddleName(member.getMiddleName());
			res.setSurname(member.getSurname());
			res.setPhoto(member.getPhoto());
			res.setEmail(member.getEmail());
			res.setPhoneNumber(member.getPhoneNumber());
			res.setAddress(member.getAddress());
			memberForm.setActor(res);
		}

		result = memberForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.memberRepository.flush();
	}

	public Member reconstruct(final Member member, final BindingResult binding) {
		Member result;

		//Nunca se crea, tan sólo es para editar el rol
		final Member originalmember = this.memberRepository.findOne(member.getId());
		Assert.notNull(originalmember, "This entity does not exist");
		result = member;
		result.setName(originalmember.getName());
		result.setSurname(originalmember.getSurname());
		result.setMiddleName(originalmember.getMiddleName());
		result.setAddress(originalmember.getAddress());
		result.setEmail(originalmember.getEmail());
		result.setIsSuspicious(originalmember.getIsSuspicious());
		result.setUserAccount(originalmember.getUserAccount());
		result.setBoxes(originalmember.getBoxes());
		result.setApplications(originalmember.getApplications());
		result.setAssociation(originalmember.getAssociation());
		result.setEvents(originalmember.getEvents());
		result.setActivities(originalmember.getActivities());
		result.setMeetings(originalmember.getMeetings());
		result.setHeadquarters(originalmember.getHeadquarters());

		this.validator.validate(result, binding);

		this.memberRepository.flush();

		return result;
	}

}
