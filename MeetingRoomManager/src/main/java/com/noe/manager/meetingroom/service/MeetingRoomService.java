/**
* <h1>MeetingRoomService</h1>
* Description: Defines the interface that the MeetingRoomService providers need to implement
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.service;
import java.time.LocalTime;
import java.util.List;

import com.noe.manager.meetingroom.entity.MeetingRoom;
public interface MeetingRoomService {
	public List<MeetingRoom> getAllMeetingRooms();
    public MeetingRoom getMeetingRoom(Long id);

    public MeetingRoom createMeetingRoom(MeetingRoom room);
    public MeetingRoom updateMeetingRoom(MeetingRoom room);
    public MeetingRoom deleteMeetingRoom(Long id);
    public List<MeetingRoom> findByName(String name);
    public List<MeetingRoom> findByAvailableFrom(LocalTime time);
    public List<MeetingRoom> findByAvailableUntil(LocalTime time);
    public List<MeetingRoom> findCurrentlyAvailable();
}
