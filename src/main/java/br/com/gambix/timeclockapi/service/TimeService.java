package br.com.gambix.timeclockapi.service;

import br.com.gambix.timeclockapi.db.TimeContext;
import br.com.gambix.timeclockapi.domain.NoteResult;
import br.com.gambix.timeclockapi.domain.NotesResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TimeService {

    @Autowired
    private DurationService durationService;

    public NotesResult getMonthlyNotes(LocalDate date) {
        return TimeContext.getMonthlyNotes(date);
    }

    public NoteResult addDailyNote(LocalDate date, String note) {
        validate(note);
        add(date, note);

        return durationService.getNoteResult(date);
    }

    public NoteResult addDailyNotes(LocalDate date, List<String> notes) {
        notes.forEach(this::validate);
        notes.forEach(note -> add(date, note));

        return durationService.getNoteResult(date);
    }

    public NoteResult overrideDailyNotes(LocalDate date, List<String> notes) {
        TimeContext.clearDailyNotes(date);
        return addDailyNotes(date, notes);
    }

    private void validate(String note) {
        if (!note.matches("\\d{2}:\\d{2}")) {
            throw new IllegalArgumentException();
        }
    }

    public void add(LocalDate date, String note) {
        if (!TimeContext.getCopyDailyNotes(date).stream().filter(note::equals).findFirst().isPresent()) {
            TimeContext.addDailyNote(date, note);
        }
    }

    public NoteResult getDailyNotes(LocalDate date) {
        return durationService.getNoteResult(date);
    }
}
