package com.example.test.crall.service;

import com.example.test.crall.dao.StoreDao;
import com.example.test.crall.domain.Notice;
import com.example.test.crall.domain.Store;
import com.example.test.crall.dto.NoticeDto;
import com.example.test.crall.dto.StoreDto;
import com.example.test.crall.dto.StoreRequestMenuVo;
import com.example.test.crall.dto.StoreRequestVo;
import com.example.test.crall.repository.NoticeRepository;
import com.example.test.crall.repository.StoreRepository;
import com.example.test.crall.util.Crawller;
import io.netty.util.concurrent.NonStickyEventExecutorGroup;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class UpbitCrawlerService {
    // NoticeRepository 주입
    private final NoticeRepository noticeRepository;

    private final StoreRepository storeRepository;

    private final StoreDao storeDao;





    // 생성자를 통한 의존성 주입
    @Autowired
    public UpbitCrawlerService(NoticeRepository noticeRepository, StoreRepository storeRepository, StoreDao storeDao) {
        this.noticeRepository = noticeRepository;
        this.storeRepository = storeRepository;
        this.storeDao = storeDao;
    }

    // Upbit 공지사항 크롤링 메소드
    public List<NoticeDto> crawlUpbitNotices() {
        // 크롬 드라이버 설정
        //System.setProperty("webdriver.chrome.driver", "src/main/java/com/example/test/crall/driver/chromedriver.exe"); // 윈도우
        System.setProperty("webdriver.chrome.driver", "src/main/java/com/example/test/crall/driver/chromedriver_mac"); // 맥
        // System.setProperty("webdriver.chrome.driver", "./chromedriver"); // 리눅스, 맥

        // 크롬 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);

        // Upbit 공지사항 페이지 접속
        driver.get("https://upbit.com/service_center/notice");

        // 공지사항 목록을 가져옵니다.
        List<WebElement> elements = driver.findElements(By.cssSelector(".css-8atqhb > tbody > tr"));

        // 반환할 NoticeDto 리스트 생성
        List<NoticeDto> notices = new ArrayList<>();

        // 각 공지사항에 대하여
        for (WebElement noticeElement : elements) {

            WebElement element = noticeElement.findElement(By.cssSelector("td > a"));
            WebElement elementTd = noticeElement.findElement(By.cssSelector("td:nth-child(1)"));
            WebElement elementTd2 = noticeElement.findElement(By.cssSelector("td:nth-child(2)"));

            String link = element.getAttribute("href");
            String title = elementTd.getText();

            LocalDate date = LocalDate.parse(elementTd2.getText(), DateTimeFormatter.ofPattern("yyyy.MM.dd")); // 날짜 문자열을 LocalDate 객체로 변환합니다.

            // Notice 객체 생성 및 저장
            Notice notice = Notice.builder()
                    .title(title)
                    .link(link)
                    .date(date)
                    .build();

            // Notice 객체를 DB에 저장합니다.
            noticeRepository.save(notice);

            // 저장된 Notice 객체를 NoticeDto로 변환하여 리스트에 추가합니다.
            notices.add(new NoticeDto(notice));
        }

        // WebDriver 종료
        driver.quit();

        // NoticeDto 리스트 반환
        return notices;
    }

    public StoreDto crawlUpbitStore() {



        // 크롬 드라이버 설정
        //System.setProperty("webdriver.chrome.driver", "src/main/java/com/example/test/crall/driver/chromedriver.exe"); // 윈도우
        System.setProperty("webdriver.chrome.driver", "src/main/java/com/example/test/crall/driver/chromedriver_mac"); // 맥
        // System.setProperty("webdriver.chrome.driver", "./chromedriver"); // 리눅스, 맥

        // 크롬 옵션 설정
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("headless");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--blink-settings=imagesEnabled=false");
        WebDriver driver = new ChromeDriver(options);


        // Upbit 공지사항 페이지 접속
        driver.get("https://place.map.kakao.com/19197483");

        System.out.println(driver.getPageSource());
        // 업데이트 날자를 가져옵니다.
        WebElement element = driver.findElement(By.xpath("//*[@id=\"mArticle\"]"));

        //System.out.println("엘리멘트 SIZE --> : " + elements.size());
        System.out.println("엘리멘트 --> : " + element.getText());


        WebElement span_date_revise = element.findElement(By.cssSelector("span.date_revise"));
        WebElement time_operation = element.findElement(By.cssSelector("span.time_operation"));
        WebElement location_present = element.findElement(By.cssSelector("div.details_placeinfo > div.placeinfo_default.placeinfo_homepage > div > div > a"));
        WebElement txt_contact = element.findElement(By.cssSelector("span.txt_contact"));

        String date_revise = span_date_revise.getText();
        String opening_hours = time_operation.getText();
        String site = location_present.getAttribute("href");
        String phone = txt_contact.getText();


        LocalDate update_date = LocalDate.parse(date_revise.substring(0, date_revise.length() - 1), DateTimeFormatter.ofPattern("yyyy.MM.dd")); // 날짜 문자열을 LocalDate 객체로 변환합니다.

        // Notice 객체 생성 및 저장
        Store store = Store.builder()
                .update_date(update_date)
                .opening_hours(opening_hours)
                .site(site)
                .phone(phone)
                .build();

        // Notice 객체를 DB에 저장합니다.
        storeRepository.save(store);

        // 저장된 Notice 객체를 NoticeDto로 변환하여 리스트에 추가합니다.
        StoreDto storeDto = new StoreDto(store);


//         WebDriver 종료
        driver.quit();

        return storeDto;
//        return null;
    }

    public void kakaoStoreCrwall(List<StoreRequestVo> storeRequestVoList) throws Exception{

//        System.out.println("kakaoStoreCrwall Service");
//
//        String currentDirectory = System.getProperty("user.dir");
//        System.out.println("현재 작업 디렉토리: " + currentDirectory);
//
//        List<StoreRequestVo> storeRequestVoList = new ArrayList<StoreRequestVo>();
//        StoreRequestVo storeRquestVo = StoreRequestVo.builder()
//                .address_name("address_name")
//                .category_name("category_name")
//                .phone("010-2839-0108")
//                .address_name("충청남도 아산시 인주면 산24번지")
//                .place_name("선현 식당")
//                .x("127.027589")
//                .y("37.498102")
//                .road_address_name("도로명 도려명")
//                .place_url("https://place.map.kakao.com/7819490?openhour=1")
//                .build();
//        storeRequestVoList.add(storeRquestVo);

        Crawller crawller = new Crawller();
        HashMap<String, Object> hashMap = crawller.new_crawller(storeRequestVoList);

        System.out.println(hashMap.size());
        storeDao.insertStore(hashMap);
    }
}