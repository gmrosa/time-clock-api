package br.com.gambix.timeclockapi.domain;

import lombok.Data;

@Data
public class PostNoteParam extends BaseNoteParam {
    private String time;
}
