package com.travelmaker.story.dao;

import com.travelmaker.friend.domain.FriendDTO;
import com.travelmaker.purchase.domain.PurchaseDTO;
import com.travelmaker.story.domain.StoryDTO;
import com.travelmaker.story.domain.StorySearchFilter;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository("storyDAO")
@Transactional
public class StoryDAOMybatis implements StoryDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<StoryDTO> getStory(StorySearchFilter storySearchFilter) {
    	List<StoryDTO> list = sqlSession.selectList("storySQL.getList", storySearchFilter);
    	StringBuffer rnos = new StringBuffer("[불러오는 게시글 rno들] ");
    	for(StoryDTO dto : list) {
    		rnos.append(dto.getRno()+"번 / ");
    	}
    	System.out.println(rnos);
        return list;
    }
    
//    @Override
//    public List<StoryDTO> getKeywordStory(StorySearchFilter storySearchFilter) {
//    	System.out.println("필터 키워드 : "+storySearchFilter.getKeyword());
//    	return sqlSession.selectList("storySQL.getList", storySearchFilter);
//    }

    @Override
    public String selectBoard(int bno) {

        return sqlSession.selectOne("storySQL.selectEssay", bno) == null ? "route" : "essay";
    }

	@Override
	public List<Map<String, String>> getFriends(StorySearchFilter storySearchFilter) {
		return sqlSession.selectList("storySQL.getFriends", storySearchFilter);
	}

	@Override
	public List<Map<String, String>> getPurchase(StorySearchFilter storySearchFilter) {
		return sqlSession.selectList("storySQL.getPurchases", storySearchFilter);
	}
}
