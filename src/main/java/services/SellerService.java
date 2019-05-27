
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SellerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Report;
import domain.Seller;
import domain.Stand;
import domain.Visitor;
import forms.SellerForm;

@Service
@Transactional
public class SellerService {

	// Managed repository
	@Autowired
	private SellerRepository	sellerRepository;

	// Supporting services
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private StandService		standService;

	@Autowired
	private ReportService		reportService;

	@Autowired
	private VisitorService		visitorService;


	// Simple CRUD methods
	// R33.1
	public Seller create() {
		Seller result;

		result = new Seller();
		final Collection<Stand> stands = new HashSet<>();
		final Collection<Box> boxes = new HashSet<>();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.SELLER);
		userAccount.addAuthority(auth);
		result.setBoxes(boxes);
		result.setStands(stands);
		result.setUserAccount(userAccount);
		result.setIsSuspicious(false);

		return result;
	}

	public Collection<Seller> findAll() {
		Collection<Seller> result;

		result = this.sellerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Seller findOne(final int sellerId) {
		Assert.isTrue(sellerId != 0);

		Seller result;

		result = this.sellerRepository.findOne(sellerId);
		Assert.notNull(result);

		return result;
	}

	// R33.1
	public Seller save(final Seller seller) {
		Assert.notNull(seller);

		Seller result;

		result = (Seller) this.actorService.save(seller);
		result = this.sellerRepository.save(result);

		return result;
	}

	public Seller saveAuxiliar(final Seller seller) {
		Assert.notNull(seller);

		Seller result;

		result = this.sellerRepository.save(seller);

		return result;
	}

	public void delete(final Seller seller) {
		Assert.notNull(seller);
		Assert.isTrue(seller.getId() != 0);
		Assert.isTrue(this.sellerRepository.exists(seller.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		final Seller sellerLogged = (Seller) actorLogged;

		this.actorService.deleteEntities(sellerLogged);

		final Collection<Stand> standsSeller = new HashSet<>(sellerLogged.getStands());
		for (final Stand s : standsSeller) {
			final Collection<Report> reportsByStandIdCollection = new HashSet<>(this.reportService.findReportsByStandId(s.getId()));
			for (final Report r : reportsByStandIdCollection) {
				final Visitor v = this.visitorService.findVisitorByReportId(r.getId());
				v.getReports().remove(r);
				this.reportService.deleteAuxiliar(r);
				this.visitorService.saveAuxiliar(v);
			}
			this.standService.deleteAuxiliar(s);
		}

		this.sellerRepository.flush();
		this.sellerRepository.delete(seller);
	}

	// Other business methods

	public Seller findSellerByStandId(final int standId) {
		Assert.isTrue(standId != 0);

		Seller result;

		result = this.sellerRepository.findSellerByStandId(standId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SellerForm reconstruct(final SellerForm sellerForm, final BindingResult binding) {
		SellerForm result;
		final Seller seller = sellerForm.getActor();

		if (seller.getId() == 0) {
			final Collection<Box> boxes = new HashSet<>();
			final Collection<Stand> stands = new HashSet<>();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.SELLER);
			userAccount.addAuthority(auth);
			userAccount.setUsername(sellerForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(sellerForm.getActor().getUserAccount().getPassword());
			seller.setStands(stands);
			seller.setUserAccount(userAccount);
			seller.setIsSuspicious(false);
			seller.setBoxes(boxes);
			sellerForm.setActor(seller);
		} else {
			final Seller res = this.sellerRepository.findOne(seller.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(seller.getName());
			res.setMiddleName(seller.getMiddleName());
			res.setSurname(seller.getSurname());
			res.setPhoto(seller.getPhoto());
			res.setEmail(seller.getEmail());
			res.setPhoneNumber(seller.getPhoneNumber());
			res.setAddress(seller.getAddress());
			sellerForm.setActor(res);
		}

		result = sellerForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.sellerRepository.flush();
	}

}
