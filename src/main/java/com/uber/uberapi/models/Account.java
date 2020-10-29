package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account extends Auditable {
    private String username;
    private String password;

    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}