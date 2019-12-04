package com.travelmaker.alarm.dao;

import java.util.List;

import com.travelmaker.alarm.domain.AlarmDTO;

public interface AlarmDAO {

	public void addAlarm(AlarmDTO alarmDTO);

	public List<AlarmDTO> getAlarmList(String seq);

	public int alarmChange(int ano);

	public void alarmDelete(String dataseq);

	public List<AlarmDTO> getMyAlarmList(String userSeq,String con);

	public void deleteNreadAlarm(String requestFid ,int con,int alarmType);

}
