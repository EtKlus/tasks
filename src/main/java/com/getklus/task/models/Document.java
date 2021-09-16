package com.getklus.task.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table("document")
public class Document {

    private @Id long id;
    private String name;
    private String uri;
    private String type;
    private long size;

    private long parent;
    private LocalDate created;
    private boolean star;
    private boolean task;
    private boolean event;
    private boolean personal;
    private long author;

    private byte[] file;

    public Document(String name, String uri, String type, long size, long p) {
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.size = size;
        this.parent = p;

    }

}
