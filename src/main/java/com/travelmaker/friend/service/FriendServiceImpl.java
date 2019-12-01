package com.travelmaker.friend.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.travelmaker.friend.dao.FriendDAO;
import com.travelmaker.friend.domain.FriendDTO;
import com.travelmaker.friend.domain.FriendRequestDTO;
import com.travelmaker.friend.domain.FriendRouteDTO;

@Service("friendService")
public class FriendServiceImpl implements FriendService {
	@Autowired
	FriendDAO friendDAO;
	
	@Override
	public List<FriendDTO> getList(Map<String, Integer> map) {
		return friendDAO.getList(map);
	}

	@Override
	public int setWrite(FriendDTO friendDTO) {
		return friendDAO.setWrite(friendDTO);		
	}

	@Override
	public void setRouteWrite(FriendRouteDTO friendRouteDTO) {
		friendDAO.setRouteWrite(friendRouteDTO);
	}

	@Override
	public void cancelWrite(String fno) {
		friendDAO.cancelWrite(fno);
	}

	@Override
	public int getTotalA() {
		return friendDAO.getTotalA();
	}

	@Override
	public FriendDTO getView(String fno) {
		return friendDAO.getView(fno);
	}

	@Override
	public List<FriendRequestDTO> getRequestView(String fcno) {
		return friendDAO.getRequestView(fcno);
	}

	@Override
	public void setRequestWrite(FriendRequestDTO friendRequestDTO) {
		friendDAO.setRequestWrite(friendRequestDTO);
	}
	public FriendRequestDTO getFriendRequestDTO(int data) {
		return friendDAO.getFriendRequestDTO(data);
	}
	
	@Override
	public FriendRouteDTO getFriendRouteDTO(int fcno) {
		return friendDAO.getFriendRouteDTO(fcno);
	}

	@Override
	public FriendDTO getFriendDTO(int fno) {
		return friendDAO.getFriendDTO(fno);
	}

	@Override
	public void requestAccept(String fccno) {
		friendDAO.requestAccept(fccno);
	}

	@Override
	public void requestReject(String fccno) {
		friendDAO.requestReject(fccno);
	}

	@Override
	public void delete(Map<String, String> map) {
		friendDAO.delete(map);
	}

	@Override
	public FriendDTO modify(String fno) {
		return friendDAO.modify(fno);
	}

	@Override
	public void setModify(FriendDTO friendDTO) {
		friendDAO.setModify(friendDTO);
	}

	@Override
	public List<FriendRouteDTO> getRouteModify(String fno) {
		return friendDAO.getRouteModify(fno);
	}

	@Override
	public void setRouteModify(FriendRouteDTO friendRouteDTO) {
		friendDAO.setRouteModify(friendRouteDTO);
	}

}
