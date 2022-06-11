package br.com.gambix.timeclockapi.domain;

import lombok.Data;

import java.util.List;

@Data
public class PutNotesParam extends BaseNoteParam {
    private List<String> notes;
}
