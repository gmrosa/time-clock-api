package br.com.gambix.timeclockapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NotesResult {
    private String total;
    private List<NoteResult> dailyNotes;
}
