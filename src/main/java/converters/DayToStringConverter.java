
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Day;

@Component
@Transactional
public class DayToStringConverter implements Converter<Day, String> {

	@Override
	public String convert(final Day day) {
		String result;

		if (day == null)
			result = null;
		else
			result = String.valueOf(day.getId());
		return result;
	}

}
