package com.travelmaker.story.dao;

import com.travelmaker.route.domain.RouteContentDTO;
import com.travelmaker.route.domain.RouteDTO;
import com.travelmaker.route.domain.RouteImageDTO;
import com.travelmaker.story.domain.StoryDTO;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository("storyDAO")
@Transactional
public class StoryDAOMybatis implements StoryDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<StoryDTO> getStory(Map<String, String> map) {

        return sqlSession.selectList("storySQL.getList", map);
    }

    @Override
    public RouteDTO getRoute(String bno) {
        return sqlSession.selectOne("storySQL.getRoute", bno);
    }

    @Override
    public List<RouteContentDTO> getRouteContentStory(int rno) {

        List<RouteContentDTO> list = sqlSession.selectList("storySQL.getRouteContentStory", rno);

        for (RouteContentDTO routeContentDTO : list) {
            List<String> imgs = new ArrayList<String>();
            List<RouteImageDTO> imageList = sqlSession.selectList("storySQL.getRouteImageStory", routeContentDTO.getCrno());
            for (RouteImageDTO routeImageDTO : imageList) {
                imgs.add(routeImageDTO.getImg());
                System.out.println("img = " + imgs);
            }
            routeContentDTO.setImgs(imgs);
        }
        return list;
    }


    @Override
    public String selectBoard(int bno) {

        return sqlSession.selectOne("storySQL.selectEssay", bno) == null ? "route" : "essay";
    }
}
