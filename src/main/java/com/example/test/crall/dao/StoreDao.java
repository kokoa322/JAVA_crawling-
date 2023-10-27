package com.example.test.crall.dao;

import com.example.test.crall.dto.StoreRequestMenuVo;
import com.example.test.crall.dto.StoreRequestVo;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Component
public class StoreDao {

    private final JdbcTemplate jdbcTemplate;

    public void insertStore(HashMap<String, Object> storeMap) throws Exception {
        for(int index = 0; index < storeMap.size()/2; index++) {
            System.out.println("------"+ index +"-------");
            StoreRequestVo storeRequestVo = (StoreRequestVo) storeMap.get("store_info_"+index);
            List<StoreRequestMenuVo> storeRequestMenuVoList = (List<StoreRequestMenuVo>) storeMap.get("store_menu_"+index);
            if(storeRequestVo == null){continue; }
            System.out.println(storeRequestVo.getName());
            System.out.println(storeRequestVo.getAddress());
            System.out.println(storeRequestVo.getLoad_address());
            System.out.println(storeRequestVo.getLikes());
            System.out.println(storeRequestVo.getCategory());
            System.out.println(storeRequestVo.getMenu_update());
            System.out.println(storeRequestVo.getUpdate_date());
            System.out.println(storeRequestVo.getPhone());
            System.out.println(storeRequestVo.getSite());
            System.out.println(storeRequestVo.getLatitude());
            System.out.println(storeRequestVo.getLongitude());

            String sql = "SELECT id FROM stores WHERE place_url = ? ";
            int store_id =0;
            try {
                store_id = jdbcTemplate.queryForObject(sql, Integer.class, storeRequestVo.getPlace_url());
            }catch (Exception e){}

            if(store_id > 0){ continue; }
            String insertStoresSql = "INSERT INTO Stores (name, likes, longitude, latitude, address, load_address, category, opening_hours, phone, site, menu_update, place_url, update_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            int result = 0;
            jdbcTemplate.update(insertStoresSql,
                    storeRequestVo.getName(), 	  storeRequestVo.getLikes(), 	     storeRequestVo.getLongitude(),
                    storeRequestVo.getLatitude(), storeRequestVo.getAddress(),	     storeRequestVo.getLoad_address(),
                    storeRequestVo.getCategory(), storeRequestVo.getOpening_hours(), storeRequestVo.getPhone(),
                    storeRequestVo.getSite(),     storeRequestVo.getMenu_update(),   storeRequestVo.getPlace_url(),
                    storeRequestVo.getUpdate_date());

            System.out.println("storeRequestVo.getPlace_url() --> : "+storeRequestVo.getPlace_url());


            store_id = jdbcTemplate.queryForObject(sql, Integer.class, storeRequestVo.getPlace_url());
            for(int menuIndex = 0; menuIndex < storeRequestMenuVoList.size(); menuIndex++) {
                if(storeRequestMenuVoList.get(menuIndex).getMenu() == null){continue;}
                String insertStoresMenuSql = "INSERT INTO Menus (store_id, menu, price) \n"
                        + "VALUES (?, ?, ?);";

                jdbcTemplate.update(insertStoresMenuSql,
                        store_id,
                        storeRequestMenuVoList.get(menuIndex).getMenu(),
                        storeRequestMenuVoList.get(menuIndex).getPrice()
                );
            }
        }
    }
}
