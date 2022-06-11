package br.com.gambix.timeclockapi.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class NoteResult implements Comparable<NoteResult> {
    private LocalDate date;
    private List<String> notes;
    private String duration;
    private String lunch;
    private String remaining;
    private String suggestion;
    private String extra;

    @Override
    public int compareTo(NoteResult noteResult) {
        return this.date.compareTo(noteResult.getDate());
    }
}
