/**
* <h1>MeetingRoomRepository</h1>
* Description: Provides a persistence layer for MeetingRoom entities
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.repository;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.noe.manager.meetingroom.entity.MeetingRoom;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long>{
	public MeetingRoom findById(long id);
	public List<MeetingRoom> findByName(String name);
	public List<MeetingRoom> findByAvailableFrom(LocalTime availableFrom);
	public List<MeetingRoom> findByAvailableUntil(LocalTime availableUntil);
	
}
