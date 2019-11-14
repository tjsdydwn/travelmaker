package com.travelmaker.story.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.travelmaker.route.domain.RouteContentDTO;
import com.travelmaker.route.domain.RouteDTO;
import com.travelmaker.route.domain.RouteImageDTO;
import com.travelmaker.story.domain.StoryDTO;

@Repository("storyDAO")
@Transactional
public class StoryDAOMybatis implements StoryDAO {

    @Autowired
    private SqlSession sqlSession;

<<<<<<< HEAD
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
=======
	@Override
	public List<StoryDTO> getStory(Map<String, String> map) {
			
		return sqlSession.selectList("storySQL.getList",map);
	}
	
	@Override
	public RouteDTO getRoute(String bno) {
		return sqlSession.selectOne("storySQL.getRoute",bno);
	}

	@Override
	public List<RouteContentDTO> getRouteContentStory(int rno) {
		System.out.println("디비 들어옴");
		
		List<RouteContentDTO> list = sqlSession.selectList("storySQL.getRouteContentStory", rno);
		
		for(RouteContentDTO routeContentDTO : list) {
			List<String> imgs = new ArrayList<String>();
			List<RouteImageDTO> imageList = sqlSession.selectList("storySQL.getRouteImageStory", routeContentDTO.getCrno());
			for(RouteImageDTO routeImageDTO : imageList) {
				imgs.add(routeImageDTO.getImg());
				System.out.println("img = " + imgs);
			}
			routeContentDTO.setImgs(imgs);
		}
		return list;
	}


	@Override
	public String selectBoard(int bno) {
		
		return sqlSession.selectOne("storySQL.selectEssay",bno)==null?"route":"essay";
	}
}
>>>>>>> 586671ae114d8b2a9c3d6a1660afc4ea93908b98
