
package converters;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.MeetingRepository;
import domain.Meeting;

@Component
@Transactional
public class StringToMeetingConverter implements Converter<String, Meeting> {

	@Autowired
	MeetingRepository	meetingRepository;


	@Override
	public Meeting convert(final String text) {
		Meeting result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.meetingRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
