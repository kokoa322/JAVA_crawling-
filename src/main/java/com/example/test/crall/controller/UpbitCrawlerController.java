package com.example.test.crall.controller;

import com.example.test.crall.dto.NoticeDto;
import com.example.test.crall.dto.StoreDto;
import com.example.test.crall.dto.StoreRequestVo;
import com.example.test.crall.service.UpbitCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @PostMapping("/kakaoStoreCrwall")
    public void kakaoStoreCrwall(@RequestBody List<StoreRequestVo> storeRequestVoList) throws Exception{
        upbitCrawlerService.kakaoStoreCrwall(storeRequestVoList);
    }
}