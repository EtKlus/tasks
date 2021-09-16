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
@Table("closing")
public class Closing {

    private @Id Long id;

    private long task;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ending;

    private String note;

    private boolean isclosed;
}
