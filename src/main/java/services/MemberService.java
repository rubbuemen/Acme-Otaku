
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MemberRepository;
import domain.Member;

@Service
@Transactional
public class MemberService {

	// Managed repository
	@Autowired
	private MemberRepository	memberRepository;


	// Supporting services

	// Simple CRUD methods
	public Member create() {
		Member result;

		result = new Member();
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

	public Member save(final Member member) {
		Assert.notNull(member);

		Member result;

		if (member.getId() == 0)
			result = this.memberRepository.save(member);
		else
			result = this.memberRepository.save(member);

		return result;
	}

	public void delete(final Member member) {
		Assert.notNull(member);
		Assert.isTrue(member.getId() != 0);
		Assert.isTrue(this.memberRepository.exists(member.getId()));

		this.memberRepository.delete(member);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Member reconstruct(final Member member, final BindingResult binding) {
		Member result;

		if (member.getId() == 0)
			result = member;
		else {
			final Member originalMember = this.memberRepository.findOne(member.getId());
			Assert.notNull(originalMember, "This entity does not exist");
			result = member;
		}

		this.validator.validate(result, binding);

		this.memberRepository.flush();

		return result;
	}

	public void flush() {
		this.memberRepository.flush();
	}

}
