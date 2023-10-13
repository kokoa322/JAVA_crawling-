package com.example.test.crall.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long store_id;
    private String opening_hours;
    private String site;
    private String phone;
    @Column(nullable = false)
    private LocalDate update_date;

    @Builder
    public Store(String opening_hours, String site, LocalDate update_date, String phone) {
        this.opening_hours = opening_hours;
        this.site = site;
        this.update_date = update_date;
        this.phone = phone;
    }
}