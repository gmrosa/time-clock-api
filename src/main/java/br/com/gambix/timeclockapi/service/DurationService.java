package br.com.gambix.timeclockapi.service;

import br.com.gambix.timeclockapi.config.ApplicationProperties;
import br.com.gambix.timeclockapi.db.TimeContext;
import br.com.gambix.timeclockapi.domain.NoteResult;
import br.com.gambix.timeclockapi.file.LocalDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DurationService {

    private static final String ZERO = "00:00";

    @Autowired
    private ApplicationProperties properties;

    public NoteResult getNoteResult(LocalDate date) {
        List<String> notes = TimeContext.getCopyDailyNotes(date);

        if (!notes.isEmpty()) {
            NoteResult noteResult = NoteResult.builder()
                    .notes(notes)
                    .duration(sum(date))
                    .lunch((lunch(date)))
                    .remaining(remaining(date))
                    .suggestion(suggestion(date))
                    .extra(extra(date))
                    .build();

            LocalDB.persist(date, noteResult);

            return noteResult;
        }
        return NoteResult.builder().build();
    }

    public String sum(LocalDate date) {
        List<String> notes = TimeContext.getCopyDailyNotes(date);

        Duration sum = Duration.ZERO;

        while (!notes.isEmpty()) {
            Duration first = toDuration(notes.remove(0));
            Duration second;

            if (notes.isEmpty()) {
                second = now();
            } else {
                second = toDuration(notes.remove(0));
            }

            Duration duration = second.minus(first);
            sum = sum.plus(duration);
        }
        return toString(sum);
    }

    public String lunch(LocalDate date) {
        if (size(date) < 2) {
            return properties.getLunchDuration();
        }
        if (size(date) == 2) {
            return toString(now().minus(getDuration(date, 1)));
        }
        return toString(getDuration(date, 2).minus(getDuration(date, 1)));
    }

    public String remaining(LocalDate date) {
        Duration first = toDuration(sum(date));
        Duration second = getWorkDuration();

        Duration suggestion = second.minus(first);

        if (suggestion.isNegative()) {
            return ZERO;
        }
        return toString(suggestion);
    }

    public String suggestion(LocalDate date) {
        Duration first = getDuration(date, 0);
        Duration workDuration = getWorkDuration();

        Duration lunch = toDuration(lunch(date));
        Duration minimumLunch = toDuration(properties.getMinimumLunchDuration());

        if (lunch.compareTo(minimumLunch) == -1) {
            lunch = minimumLunch;
        }
        return toString(first.plus(workDuration).plus(lunch));
    }

    public String extra(LocalDate date) {
        if (size(date) >= 4) {
            Duration diff = toDuration(sum(date)).minus(getWorkDuration());

            if (!diff.isNegative()) {
                if (diff.compareTo(toDuration(properties.getToleranceDuration())) == 1) {
                    return toString(diff);
                }
            }
        }
        return ZERO;
    }

    private Duration getDuration(LocalDate date, int index) {
        return toDuration(TimeContext.getCopyDailyNotes(date).get(index));
    }

    private String toString(Duration duration) {
        return String.format("%02d", duration.toHoursPart()) + ":" + String.format("%02d", duration.toMinutesPart());
    }

    private Duration now() {
        Duration duration;
        LocalDateTime now = LocalDateTime.now();
        duration = toDuration(now.getHour() + ":" + now.getMinute());
        return duration;
    }

    private Duration toDuration(String time) {
        Integer hours = Integer.valueOf(time.split(":")[0]);
        Integer minutes = Integer.valueOf(time.split(":")[1]);

        return Duration.ofHours(hours).plusMinutes(minutes);
    }

    private Duration getWorkDuration() {
        return toDuration(properties.getWorkDuration());
    }

    private int size(LocalDate date) {
        return TimeContext.getCopyDailyNotes(date).size();
    }
}
