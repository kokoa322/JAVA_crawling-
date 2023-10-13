package com.example.test.crall.dto;

import com.example.test.crall.domain.Notice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoticeDto {

    private String title;
    private String link;
    private LocalDate date;

    public NoticeDto(Notice notice) {
        this.title = notice.getTitle();
        this.link = notice.getLink();
        this.date = notice.getDate();
    }
}