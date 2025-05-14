package com.ming.mgo.entity;

import org.hibernate.annotations.CurrentTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "sgf")
public class Sgf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "time_ms")
    private Long timeMs;

    @NotBlank
    @Column(name = "b_name")
    private String bName;

    @NotBlank
    @Column(name = "w_name")
    private String wName;

    @NotNull
    @Min(-18)
    @Max(9)
    @Column(name = "b_level")
    private Integer bLevel;

    @NotNull
    @Min(-18)
    @Max(9)
    @Column(name = "w_level")
    private Integer wLevel;

    @NotBlank
    @Column(name = "sgf", columnDefinition = "TEXT") // sgf 可能比较长
    private String sgf;
}
