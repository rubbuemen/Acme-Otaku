
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Seller;

@Component
@Transactional
public class SellerToStringConverter implements Converter<Seller, String> {

	@Override
	public String convert(final Seller seller) {
		String result;

		if (seller == null)
			result = null;
		else
			result = String.valueOf(seller.getId());
		return result;
	}

}
