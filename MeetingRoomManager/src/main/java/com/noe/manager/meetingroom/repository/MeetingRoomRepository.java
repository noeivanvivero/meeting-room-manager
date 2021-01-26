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
	
	/**
	 * @return A list of MeetingRooms whose name matches the @param[name] if there is no match then an empty list
	 */
	public List<MeetingRoom> findByName(String name);
	/**
	 * @return A list of MeetingRooms whose availableFrom member attribute matches the @param[availableFrom] if there is no match then an empty list
	 */
	public List<MeetingRoom> findByAvailableFrom(LocalTime availableFrom);
	/**
	 * @return A list of MeetingRooms whose availableUntil member attribute matches the @param[availableUntil] if there is no match then an empty list
	 */
	public List<MeetingRoom> findByAvailableUntil(LocalTime availableUntil);
	
}
