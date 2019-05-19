
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.HeadquarterRepository;
import domain.Headquarter;

@Component
@Transactional
public class StringToHeadquarterConverter implements Converter<String, Headquarter> {

	@Autowired
	HeadquarterRepository	headquarterRepository;


	@Override
	public Headquarter convert(final String text) {
		Headquarter result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.headquarterRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
