package com.uber.uberapi.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends Auditable {
    private Integer ratingOutOfFive;
    private String note;
}
