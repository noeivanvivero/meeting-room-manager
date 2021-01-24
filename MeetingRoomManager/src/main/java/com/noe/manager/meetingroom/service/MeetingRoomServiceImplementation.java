/**
* <h1>MeetingRoomServiceImplementation</h1>
* Description: Implementation of the MeetingRoomService interface
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.repository.MeetingRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingRoomServiceImplementation implements MeetingRoomService {

	private final MeetingRoomRepository repo;
	
	@Override
	public List<MeetingRoom> getAllMeetingRooms() {
		return repo.findAll();
	}

	@Override
	public MeetingRoom getMeetingRoom(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public MeetingRoom createMeetingRoom(MeetingRoom room) {
		return repo.save(room);
	}

	@Override
	public MeetingRoom updateMeetingRoom(MeetingRoom room) {
		MeetingRoom toBeEdited = getMeetingRoom(room.getId());
		if(toBeEdited == null) return null;
		toBeEdited.setName(room.getName());
		toBeEdited.setDescription(room.getDescription());
		toBeEdited.setAvailableFrom(room.getAvailableFrom());
		toBeEdited.setAvailableUntil(room.getAvailableUntil());
		return repo.save(toBeEdited);
	}

	@Override
	public MeetingRoom deleteMeetingRoom(Long id) {
		MeetingRoom toBeDeleted = getMeetingRoom(id);
		if(toBeDeleted == null) return null;
		repo.delete(toBeDeleted);
		return toBeDeleted;
	}

	@Override
	public List<MeetingRoom> findByName(String name) {
		return repo.findByName(name);
	}

	@Override
	public List<MeetingRoom> findByAvailableFrom(LocalTime time) {
		return repo.findByAvailableFrom(time);
	}

	@Override
	public List<MeetingRoom> findByAvailableUntil(LocalTime time) {
		return repo.findByAvailableUntil(time);
	}

}
