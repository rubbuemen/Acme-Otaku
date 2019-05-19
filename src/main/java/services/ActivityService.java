
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ActivityRepository;
import domain.Activity;

@Service
@Transactional
public class ActivityService {

	// Managed repository
	@Autowired
	private ActivityRepository	activityRepository;


	// Supporting services

	// Simple CRUD methods
	public Activity create() {
		Activity result;

		result = new Activity();
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

	public Activity save(final Activity activity) {
		Assert.notNull(activity);

		Activity result;

		if (activity.getId() == 0)
			result = this.activityRepository.save(activity);
		else
			result = this.activityRepository.save(activity);

		return result;
	}

	public void delete(final Activity activity) {
		Assert.notNull(activity);
		Assert.isTrue(activity.getId() != 0);
		Assert.isTrue(this.activityRepository.exists(activity.getId()));

		this.activityRepository.delete(activity);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Activity reconstruct(final Activity activity, final BindingResult binding) {
		Activity result;

		if (activity.getId() == 0)
			result = activity;
		else {
			final Activity originalActivity = this.activityRepository.findOne(activity.getId());
			Assert.notNull(originalActivity, "This entity does not exist");
			result = activity;
		}

		this.validator.validate(result, binding);

		this.activityRepository.flush();

		return result;
	}

	public void flush() {
		this.activityRepository.flush();
	}

}
