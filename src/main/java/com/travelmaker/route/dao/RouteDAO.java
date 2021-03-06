package com.travelmaker.route.dao;

import com.travelmaker.route.domain.RouteContentDTO;
import com.travelmaker.route.domain.RouteDTO;
import com.travelmaker.route.domain.RouteImageDTO;

import java.util.List;
import java.util.Map;

public interface RouteDAO {

	public int setRoute(RouteDTO routeDTO);

	public int modifySetRoute(RouteDTO routeDTO);

	public int saveCourse(RouteContentDTO routeContentDTO);

	public void saveRoute(RouteDTO routeDTO);

	public void saveRouteImage(RouteImageDTO routeImageDTO);

    public RouteDTO getRoute(int rno);
    
    public RouteDTO getRouteByBno(int bno);

    public List<RouteContentDTO> getRouteContentStory(int rno);
    
    public List<RouteDTO> getRouteListByUserSeq(int seq);

	public RouteContentDTO getCourse(int crno);

	public void patchCourse(RouteContentDTO routeContentDTO);

	public List<RouteImageDTO> deleteCourse(int crno);

	public void saveOrder(Map<String, Integer> map);

	public void updateViews(int rno);

	public RouteDTO getRouteTemp(int seq);

	public List<String> deleteRoute(int rno);

	public void deleteRouteImage(String delImage);


}
