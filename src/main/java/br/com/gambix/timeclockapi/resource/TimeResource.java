package br.com.gambix.timeclockapi.resource;

import br.com.gambix.timeclockapi.domain.*;
import br.com.gambix.timeclockapi.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController()
public class TimeResource {

    @Autowired
    private TimeService timeService;

    @GetMapping(path = "/note/day")
    public NoteResult getDailyNotes(@RequestBody GetNoteParam param) {
        return timeService.getDailyNotes(getDate(param));
    }

    @PostMapping(path = "/note/day")
    public NoteResult addDailyNote(@RequestBody PostNoteParam param) {
        return timeService.addDailyNote(getDate(param), param.getTime());
    }

    @PostMapping(path = "/notes/day")
    public NoteResult addDailyNotes(@RequestBody PostNotesParam param) {
        return timeService.addDailyNotes(getDate(param), param.getNotes());
    }

    @PutMapping(path = "/notes/day")
    public NoteResult overrideDailyNotes(@RequestBody PutNotesParam param) {
        return timeService.overrideDailyNotes(getDate(param), param.getNotes());
    }

    @GetMapping(path = "/notes/month")
    public NotesResult getMonthlyNotes(@RequestBody GetNotesParam param) {
        return timeService.getMonthlyNotes(getDate(param));
    }

    private LocalDate getDate(BaseNoteParam param) {
        if (param != null && param.getDate() == null) {
            return LocalDate.now();
        }
        return param.getDate();
    }
}
