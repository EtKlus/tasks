package com.getklus.task.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table("folder")
public class Folder {
    private @Id Long id;
    private String name;
    private LocalDate created;
    private long parent;
    private boolean personal;
    private long author;


}
