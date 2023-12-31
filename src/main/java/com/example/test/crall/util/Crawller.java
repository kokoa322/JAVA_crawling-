package com.example.test.crall.util;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.example.test.crall.dto.StoreRequestMenuVo;
import com.example.test.crall.dto.StoreRequestVo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;



public class Crawller {
	/**
	 * @param storeRequestVos<StoreRequestVo> storeRequestVos
	 * 				------StoreRequestVo---------
	 * 				...
	 * 				...
	 * 				...
	 * 				...
	 * 
	 * @return HashMap<String, Object>
	 * 				---------HashMap KEY--------
	 * 				식당 정보 : "store_info_" +index
	 * 				식당 메뉴 : "store_menu_" +index
	 * 				-----------------------------
	 * @throws Exception
	 */
    public HashMap<String, Object> new_crawller(List<StoreRequestVo> storeRequestVos) throws Exception {
    	
    	// 결과를 저장할 HashMap 생성
        HashMap<String, Object> hashMap = new HashMap<>();

        // 드라이버 경로 윈도우 
        //String driverPath = "D:\\SunhyeonSpring\\mak_gol\\src\\driver\\chromedriver.exe";
        // 드라이버 절대경로 맥
        String driverPath = "src/main/java/com/example/test/crall/driver/chromedriver_mac";


        //스레드를 종료하기위한 List
    	List<JobThread> jobThreads = new ArrayList<>();


    	//storeRequestVos의 사이즈 많금스레드를 생성하겠다.
    	for(int i=0; i < storeRequestVos.size(); i++) {
    		//스레드에 주소값을 넘겨줌
    		JobThread jobThread = new JobThread(driverPath, storeRequestVos.get(i).getPlace_url(), storeRequestVos.get(i), hashMap, i);
        	jobThread.start();
        	jobThreads.add(jobThread);
    	}
        
    	// 스레드 종료
        for(JobThread jobThread: jobThreads) { jobThread.join(); }
        
        return hashMap;
    }
    
    private class JobThread extends Thread {
    	
    	private String driverPath;
    	private String detailPage;
    	private StoreRequestVo storeRequestVo;
    	private HashMap<String, Object> hashMap;
    	private int thread_count;
    	
    	public JobThread(String driverPath, String detailPage, StoreRequestVo storeRequestVo, HashMap<String, Object> hashMap,int thread_count) {
    		
    		this.driverPath = driverPath;
    		this.detailPage = detailPage;
    		this.storeRequestVo = storeRequestVo;
    		this.hashMap = hashMap;
    		this.thread_count = thread_count;
    	}
    	
    	@Override
        public void run(){processCawller();}
    	
    	
    	public void processCawller() {
    		
    		// 식당의 메뉴들를 저장할 List
    		List<StoreRequestMenuVo> storeRequestMenuVos = new ArrayList<StoreRequestMenuVo>();

            Random r = new Random();
    		
    		//chrome driver 경로 세팅
    		System.setProperty("webdriver.chrome.driver", driverPath); 
            // 크롬 옵션 설정
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");	//브라우저가 다른 출처(origin)의 리소스에 대한 요청을 수행할 수있음.
            options.addArguments("headless"); 					//브라우저 창을 표시하지 않고 백그라운드에서 웹 페이지를 실행하는 모드.
            options.addArguments("--disable-popup-blocking");	//팝업 차단을 비활성화하는 옵션.
            options.addArguments("--blink-settings=imagesEnabled=false");//웹 페이지에서 이미지 로딩을 비활성화.
            WebDriver driver = new ChromeDriver(options);

            System.out.println(detailPage);
            // 웹 페이지로 이동 후 코드 가져옴
            driver.get(detailPage);
            
            // 로딩 기다리기
            try {
            	Thread.sleep(1000);	
            } catch (Exception e) {
            	e.printStackTrace();
            }

            // 검색 버튼을 찾아서 클릭

            WebElement searchButton = null;
            WebElement element = null;
            try {
                searchButton = driver.findElement(By.xpath("//*[@id=\"mArticle\"]/div[3]/a"));
                searchButton.click();
                element 			= searchButton.findElement(By.xpath("//*[@id=\"mArticle\"]"));
            } catch (Exception e) {
                element 			= driver.findElement(By.cssSelector("#mArticle"));
            }

            // 웹 페이지에서 태그를 찾아서 데이터 가져오기
            WebElement span_date_revise = element.findElement(By.cssSelector("span.date_revise"));
            WebElement time_operation;
            String opening_hours;
            try {
                time_operation = element.findElement(By.cssSelector("span.time_operation"));
                opening_hours  = time_operation.getText();
                if(opening_hours == null){
                    storeRequestVo.setOpening_hours("");
                } else {
                    storeRequestVo.setOpening_hours(opening_hours);
                }
            } catch (Exception e) {}
            
            
            // 크롤링 멈춤 방지용 try catch 
            WebElement location_present;
            String site="";
            
            try {
                location_present = element.findElement(By.cssSelector("div.details_placeinfo > div.placeinfo_default.placeinfo_homepage > div > div > a"));
                site = location_present.getAttribute("href");
                if(site == null){
                    storeRequestVo.setSite("");
                } else {
                    storeRequestVo.setSite(site);
                }

    		} catch (Exception e) {}
            
            WebElement txt_contact;

            String phone = String.valueOf(r.nextInt(888888) + 111111);

            
            try {
            	txt_contact = element.findElement(By.cssSelector("span.txt_contact"));
            	phone = txt_contact.getText();
                if(phone == null || phone == ""){
                    storeRequestVo.setPhone("");
                } else {
                    storeRequestVo.setPhone(phone);
                }

    		} catch (Exception e) {}
            
            WebElement update_getMenu;
            String getMenu_update;
            String date_revise;
            LocalDate update_date;
            LocalDate menu_update;
            
            try {
            	update_getMenu = element.findElement(By.cssSelector("span.txt_updated > span"));
            	getMenu_update = update_getMenu.getText();
            	date_revise = span_date_revise.getText();
            	
            	update_date = LocalDate.parse(date_revise.substring(0, date_revise.length() - 1), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            	menu_update = LocalDate.parse(getMenu_update.substring(0, date_revise.length() - 1),  DateTimeFormatter.ofPattern("yyyy.MM.dd"));


                if(update_date == null){
                    storeRequestVo.setMenu_update(null);
                } else {
                    storeRequestVo.setMenu_update(menu_update);
                }


                if(menu_update == null){
                    storeRequestVo.setMenu_update(null);
                } else {
                    storeRequestVo.setUpdate_date(update_date);
                }


    		} catch (Exception e) {}
            
            




            
            //데이터 확인
            System.out.println("thread "+ thread_count +": 이름 : "+ storeRequestVo.getName());
            System.out.println("thread "+ thread_count +": 주소 : "+ storeRequestVo.getAddress());
            System.out.println("thread "+ thread_count +": 도로명 : "+ storeRequestVo.getLoad_address());
            System.out.println("thread "+ thread_count +": 전화번호 : "+ storeRequestVo.getPhone());
            System.out.println("thread "+ thread_count +": 카테고리 : "+ storeRequestVo.getCategory());
            System.out.println("thread "+ thread_count +": 상세페이지 : "+ storeRequestVo.getPlace_url());
            System.out.println("thread "+ thread_count +": 업데이트 : "+ storeRequestVo.getUpdate_date());
            System.out.println("thread "+ thread_count +": 영업시간 : "+ storeRequestVo.getOpening_hours());
            System.out.println("thread "+ thread_count +": 메뉴 업데이트 : "+ storeRequestVo.getMenu_update());

            // 메뉴 정보 가져오기
            List<WebElement> element_menu = element.findElements(By.cssSelector("#mArticle > div.cont_menu > ul > li"));
            for (WebElement element_menu_ : element_menu) {
                StoreRequestMenuVo storeRequestMenuVo = new StoreRequestMenuVo();


            	//메뉴 이름이 담긴 태그 타겟팅
                WebElement menu_name = element_menu_.findElement(By.cssSelector("div > span"));



                WebElement menu_price;
                String price;
                try{
                    //메뉴 가격이 담긴 태그 타겟팅
                    menu_price = element_menu_.findElement(By.cssSelector("div > em.price_menu"));
                    //메뉴 가격 가져오기
                    price = menu_price.getText();
                    if(price == null){
                        storeRequestMenuVo.setPrice("");
                    } else {
                        storeRequestMenuVo.setPrice(price);
                    }

                } catch (Exception e){}


                //메뉴 이름 가져오기
                String menu = menu_name.getText();
                if(menu == null){
                    storeRequestMenuVo.setMenu("");
                } else {
                    storeRequestMenuVo.setMenu(menu);
                }

                //식당의 메뉴를 저장할 객체


                //메뉴 객체에 담기


                storeRequestMenuVos.add(storeRequestMenuVo);
            }
            
         // HashMap에 결과 저장
                System.out.println("before hash put : "+thread_count+" : "+storeRequestVo.getName());
            	hashMap.put("store_menu_"+thread_count, storeRequestMenuVos);
            	hashMap.put("store_info_"+thread_count, storeRequestVo);


            // WebDriver 종료
            driver.quit();
    	}
    	
    }
    
}
