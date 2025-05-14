package com.ming.mgo.dto;

import com.ming.mgo.entity.Sgf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SgfDTO {
    private Long id;
    private Long timeMs;
    private String bName;
    private String wName;
    private Integer bLevel;
    private Integer wLevel;

    public static SgfDTO from(Sgf sgf) {
        return new SgfDTO(
            sgf.getId(),
            sgf.getTimeMs(),
            sgf.getBName(),
            sgf.getWName(),
            sgf.getBLevel(),
            sgf.getWLevel()
        );
    }
}
