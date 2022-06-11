package br.com.gambix.timeclockapi.db;

import br.com.gambix.timeclockapi.domain.NotesResult;
import br.com.gambix.timeclockapi.file.LocalDB;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.*;

@UtilityClass
public class TimeContext {

    private final Map<LocalDate, List<String>> notes = new HashMap<>();

    public NotesResult getMonthlyNotes(LocalDate date) {
        NotesResult result = LocalDB.loadMonth(date);

        result.getDailyNotes().forEach(day-> {
            addDailyNotes(day.getDate(),day.getNotes());
        });
        Collections.sort(result.getDailyNotes());


        return result;
    }

    public List<String> getCopyDailyNotes(LocalDate date) {
        List<String> copy = new ArrayList<>();
        copy.addAll(getDailyNotes(date));

        return copy;
    }

    public void addDailyNote(LocalDate date, String note) {
        getDailyNotes(date).add(note);
    }

    public void addDailyNotes(LocalDate date, List<String> notes) {
        clearDailyNotes(date);
        getDailyNotes(date).addAll(notes);
    }

    public void clearDailyNotes(LocalDate date) {
        getDailyNotes(date).clear();
    }

    private List<String> getDailyNotes(LocalDate date) {
        if (!notes.containsKey(date)) {
            notes.put(date, new ArrayList<>());
            LocalDB.loadDay(date).ifPresent(n -> notes.put(date, n.getNotes()));
        }
        return notes.get(date);
    }
}
