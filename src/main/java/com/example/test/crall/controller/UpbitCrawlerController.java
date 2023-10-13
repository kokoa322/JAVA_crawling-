package com.example.test.crall.controller;

import com.example.test.crall.dto.NoticeDto;
import com.example.test.crall.dto.StoreDto;
import com.example.test.crall.service.UpbitCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawl")
public class UpbitCrawlerController {
    private final UpbitCrawlerService upbitCrawlerService;

    @GetMapping("/upbit/notices")
    public List<NoticeDto> crawlUpbitNotices() {
        return upbitCrawlerService.crawlUpbitNotices();
    }

    @GetMapping("/upbit/store")
    public StoreDto crawlUpbitStore() {
        return upbitCrawlerService.crawlUpbitStore();
    }
}