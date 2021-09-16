package com.getklus.task.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table("task")
public class Task {

    private @Id Long id;
    private String intitule;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate debut;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate echeance;

    private String note;
    private boolean ispriority;
    private long responsable;
    private long author;

}
