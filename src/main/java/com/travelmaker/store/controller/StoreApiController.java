package com.travelmaker.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.travelmaker.store.domain.StoreHotelDTO;
import com.travelmaker.store.domain.StoreHotelPaging;
import com.travelmaker.store.service.StoreService;

@RestController
@RequestMapping("/store")
public class StoreApiController {
	
	@Autowired
	StoreService storeService;
	@Autowired
	StoreHotelPaging storeHotelPaging;
	
	// Store 전체 리스트
	@PostMapping(path = "/getHotelList", produces = "application/json")
	public ModelAndView getHotelList(@RequestParam(required = false, defaultValue = "1") String pg) {
		ModelAndView modelAndView = new ModelAndView();
		
		int endNum = Integer.parseInt(pg) * 5;
		int startNum = endNum - 4;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		map.put("startNum", startNum);
		map.put("endNum", endNum);
		
		List<StoreHotelDTO> list = storeService.getHotelList(map);
		
		int totalA = storeService.getTotalA();
		
		storeHotelPaging.setCurrentPage(Integer.parseInt(pg));
		storeHotelPaging.setPageBlock(3);
		storeHotelPaging.setPageSize(5);
		storeHotelPaging.setTotalA(totalA);
		storeHotelPaging.makePagingHTML();
		
		modelAndView.addObject("list", list);
		modelAndView.addObject("friendPaging", storeHotelPaging);
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
		
	}
	// Store View
	@PostMapping(value = "/getHotelView", produces = "application/json")
	public ResponseEntity<StoreHotelDTO> getHotelView(@RequestBody String hnb){
		String[] arr = hnb.split("=");
		hnb = arr[1];
		System.out.println(hnb);
		
		StoreHotelDTO storeHotelDTO = storeService.getHotelView(hnb);
		
		if(storeHotelDTO == null) {
			return new ResponseEntity<StoreHotelDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<StoreHotelDTO>(storeHotelDTO, HttpStatus.OK);
	}
}
