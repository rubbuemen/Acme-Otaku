
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Score;

@Component
@Transactional
public class ScoreToStringConverter implements Converter<Score, String> {

	@Override
	public String convert(final Score score) {
		String result;

		if (score == null)
			result = null;
		else
			result = String.valueOf(score.getId());
		return result;
	}

}
