
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Headquarter;

@Component
@Transactional
public class HeadquarterToStringConverter implements Converter<Headquarter, String> {

	@Override
	public String convert(final Headquarter headquarter) {
		String result;

		if (headquarter == null)
			result = null;
		else
			result = String.valueOf(headquarter.getId());
		return result;
	}

}
