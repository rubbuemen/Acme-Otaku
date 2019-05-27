
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdministratorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Box;
import domain.Seller;
import domain.Stand;
import forms.AdministratorForm;

@Service
@Transactional
public class AdministratorService {

	// Managed repository
	@Autowired
	private AdministratorRepository	administratorRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private UserAccountService		userAccountService;


	// Simple CRUD methods
	// R16.1
	public Administrator create() {
		Administrator result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		final Collection<Box> boxes = new HashSet<>();

		result = new Administrator();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.ADMIN);
		userAccount.addAuthority(auth);
		result.setBoxes(boxes);
		result.setUserAccount(userAccount);
		result.setIsSuspicious(false);

		return result;
	}

	public Collection<Administrator> findAll() {
		Collection<Administrator> result;

		result = this.administratorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Administrator findOne(final int administratorId) {
		Assert.isTrue(administratorId != 0);
		Administrator result;

		result = this.administratorRepository.findOne(administratorId);
		Assert.notNull(result);

		return result;
	}

	// R16.1
	public Administrator save(final Administrator administrator) {
		Assert.notNull(administrator);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		Administrator result;

		result = (Administrator) this.actorService.save(administrator);
		result = this.administratorRepository.save(result);

		return result;
	}

	public void delete(final Administrator administrator) {
		Assert.notNull(administrator);
		Assert.isTrue(administrator.getId() != 0);
		Assert.isTrue(this.administratorRepository.exists(administrator.getId()));

		this.administratorRepository.delete(administrator);
	}

	// Other business methods
	//Queries Dashboard
	public String dashboardQueryC1() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC1();

		return result;
	}

	public String dashboardQueryC2() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC2();

		return result;
	}

	public String dashboardQueryC3() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC3();

		return result;
	}

	public String dashboardQueryC4() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC4();

		return result;
	}

	public String dashboardQueryC5() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC5();

		return result;
	}

	public String dashboardQueryC6() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC6();

		return result;
	}

	public String dashboardQueryC7() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC7();

		return result;
	}

	public String dashboardQueryC8() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryC8();

		return result;
	}

	public String dashboardQueryB1() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryB1();

		return result;
	}

	public String dashboardQueryB2() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryB2();

		return result;
	}

	public String dashboardQueryB3() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryB3();

		return result;
	}

	public Collection<Seller> dashboardQueryB4() {
		Collection<Seller> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryB4();

		final List<Seller> resultList = new ArrayList<>(result);

		if (resultList.size() > 3)
			result = resultList.subList(0, 3);

		return result;
	}

	public Collection<Stand> dashboardQueryA1() {
		Collection<Stand> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryA1();

		final List<Stand> resultList = new ArrayList<>(result);

		if (resultList.size() > 3)
			result = resultList.subList(0, 3);

		return result;
	}

	public String dashboardQueryA2() {
		String result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		result = this.administratorRepository.dashboardQueryA2();

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public AdministratorForm reconstruct(final AdministratorForm administratorForm, final BindingResult binding) {
		AdministratorForm result;
		final Administrator administrator = administratorForm.getActor();

		if (administrator.getId() == 0) {
			final Collection<Box> boxes = new HashSet<>();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.ADMIN);
			userAccount.addAuthority(auth);
			userAccount.setUsername(administratorForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(administratorForm.getActor().getUserAccount().getPassword());
			administrator.setUserAccount(userAccount);
			administrator.setIsSuspicious(false);
			administrator.setBoxes(boxes);
			administratorForm.setActor(administrator);
		} else {
			final Administrator res = this.administratorRepository.findOne(administrator.getId());
			res.setName(administrator.getName());
			res.setMiddleName(administrator.getMiddleName());
			res.setSurname(administrator.getSurname());
			res.setPhoto(administrator.getPhoto());
			res.setEmail(administrator.getEmail());
			res.setPhoneNumber(administrator.getPhoneNumber());
			res.setAddress(administrator.getAddress());
			administratorForm.setActor(res);
		}

		result = administratorForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.administratorRepository.flush();
	}

}
