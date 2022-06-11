package br.com.gambix.timeclockapi.domain;

import lombok.Data;

import java.util.List;

@Data
public class PostNotesParam extends BaseNoteParam {
    private List<String> notes;
}
