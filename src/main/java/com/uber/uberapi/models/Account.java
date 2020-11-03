package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Column;
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
    @Column(unique = true, nullable = false)
    private String username;
    private String password;

    // when someone fetches an account, get all the roles as well
    // we will get a NPE
    // Auth
    // todo: (fetch = FetchType.EAGER)
    // account_roles -> account_id, role_id
    // primary key -> (account_id, role_id)
    // add a unique constraint to the role_id column -> one to one relation  (you can have #roles entries)
    // add a unique constraint to the account_id column -> one role can be mapped to many accounts, but one account can only have one role

    @ManyToMany
    private List<Role> roles = new ArrayList<>();
    // talk the auth - Role based authentication
}

// Requirement analysis
// what the basic requirements are
// what the flows look like for our actors
// passenger will request a ride, driver will get notified, driver will accept

// drivermatchingservice
// locationtrackingservice

// very rare, remove a column, remove a model
// common - add certain columns to your tables
// you add new relationships