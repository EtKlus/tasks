package com.getklus.task.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Table("users")
public class Users {

    private @Id Long id;
    private String username;
    private String password;
    private String authority;
    private String email;
    private boolean enable;
}
