package com.example.test.crall.dto;

import com.example.test.crall.domain.Notice;
import com.example.test.crall.domain.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class StoreDto {

    private String opening_hours;
    private String site;
    private String phone;
    @Column(nullable = false)
    private LocalDate update_date;
    public StoreDto(Store store) {
        this.opening_hours = store.getOpening_hours();
        this.site = store.getSite();
        this.phone = store.getPhone();
        this.update_date = store.getUpdate_date();
    }
}