
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Association;

@Component
@Transactional
public class AssociationToStringConverter implements Converter<Association, String> {

	@Override
	public String convert(final Association association) {
		String result;

		if (association == null)
			result = null;
		else
			result = String.valueOf(association.getId());
		return result;
	}

}
