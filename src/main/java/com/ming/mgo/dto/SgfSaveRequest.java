package com.ming.mgo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SgfSaveRequest {

    @NotNull
    @JsonProperty("timeMs")
    private Long timeMs;

    @NotBlank
    @JsonProperty("bName")
    private String bName;

    @NotBlank
    @JsonProperty("wName")
    private String wName;

    @NotNull
    @Min(-18)
    @Max(9)
    @JsonProperty("bLevel")
    private Integer bLevel;

    @NotNull
    @Min(-18)
    @Max(9)
    @JsonProperty("wLevel")
    private Integer wLevel;

    @NotBlank
    @JsonProperty("sgf")
    private String sgf;
}
