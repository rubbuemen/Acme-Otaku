
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Registration;

@Component
@Transactional
public class RegistrationToStringConverter implements Converter<Registration, String> {

	@Override
	public String convert(final Registration registration) {
		String result;

		if (registration == null)
			result = null;
		else
			result = String.valueOf(registration.getId());
		return result;
	}

}
