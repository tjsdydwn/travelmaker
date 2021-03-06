package com.travelmaker.story.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.travelmaker.story.domain.StoryDTO;
import com.travelmaker.story.domain.StorySearchFilter;
import com.travelmaker.story.service.StoryService;

@RestController
@RequestMapping(value = "/api/story")
public class StoryApiController {
	
	@Autowired
	StoryService storyService;

	@GetMapping(path = {"/{start}/{end}/{keyword}","/{start}/{end}"})
	public List<StoryDTO> getList(@PathVariable int start, @PathVariable int end, @PathVariable Optional<String> keyword) {
		
		StorySearchFilter storySearchFilter = new StorySearchFilter();
		storySearchFilter.setStart(start);
		storySearchFilter.setEnd(end);
		storySearchFilter.setKeyword(keyword.isPresent() ? keyword.get() : "");
		
//		System.out.println("start : "+start);
//		System.out.println("end : "+end);
//		System.out.println("keyword : "+keyword);

		List<StoryDTO> list = storyService.getStory(storySearchFilter);
		System.out.println("가져온게시글 수 : "+list.size());
		return list;
	}
	
	@GetMapping(path ={"/home/{keyword}","/home/"} )
	public Map<String, Object> homeList(@PathVariable Optional<String> keyword){
			
		return storyService.getHomeList(keyword.isPresent() ? keyword.get() : "");
	}

	
}
