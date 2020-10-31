package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.*;
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
    @Column(unique = true, nullable = false)
    private String username;
    private String password;

    // when someone fetches an account, get all the roles as well
    // we will get a NPE
    // Auth
    // todo: (fetch = FetchType.EAGER)
    @ManyToMany
    private List<Role> roles = new ArrayList<>();
}