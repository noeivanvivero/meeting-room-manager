/**
* <h1>MeetingRoomServiceImplementation</h1>
* Description: Implementation of the MeetingRoomService interface
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;
import com.noe.manager.meetingroom.repository.MeetingRoomRepository;
import com.noe.manager.meetingroom.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MeetingRoomServiceImplementation implements MeetingRoomService {

	private final MeetingRoomRepository repo;
	private final ReservationRepository repoReservation;
	
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
		if(room.getAvailableUntil().isBefore(room.getAvailableFrom())) return null;
		if(room.getAvailableFrom().equals(room.getAvailableUntil())) return null;
		List<MeetingRoom> roomDB = repo.findByName(room.getName());
		if(roomDB.isEmpty()) return repo.save(room);
		else return null;
	}

	@Override
	public MeetingRoom updateMeetingRoom(MeetingRoom room) {
		if(room.getAvailableUntil().isBefore(room.getAvailableFrom())) return null;
		if(room.getAvailableFrom().equals(room.getAvailableUntil())) return null;
		MeetingRoom toBeEdited = getMeetingRoom(room.getId());
		if(toBeEdited == null) return null;
		List<MeetingRoom> roomDB = repo.findByName(room.getName());
		roomDB.remove(toBeEdited);
		if(!roomDB.isEmpty()) return null;
		
		LocalDate today = LocalDate.now();
		List<Reservation> reservations = repoReservation.findByRoomAndDate(toBeEdited,today);
		if(reservations.stream().anyMatch(reservation->{
			return reservation.getReservedUntil().isAfter(room.getAvailableUntil()) || 
					reservation.getReservedFrom().isBefore(room.getAvailableFrom());
		})) return null;  
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
	
	@Override
	public List<MeetingRoom> findCurrentlyAvailable(){
		List<MeetingRoom> rooms = this.repo.findAll();
		LocalDate today = LocalDate.now();
		LocalTime now = LocalTime.now();
		List<Reservation> reservations=this.repoReservation.findByDate(today);
		final List<Reservation> filterdReservations = reservations.stream().filter(element->{
			return (element.getReservedUntil().isAfter(now) || element.getReservedUntil().equals(now)) &&
					(element.getReservedFrom().isBefore(now) || element.getReservedUntil().equals(now));
		}).collect(Collectors.toList());
		
		rooms = rooms.stream().filter(room->{
			return !findCurrentlyAvailableHelper(room,filterdReservations);
		}).collect(Collectors.toList());
		return rooms;
		
	}
	
	private boolean findCurrentlyAvailableHelper(MeetingRoom room, List<Reservation> reservations ) {
		return reservations.stream().anyMatch(reservation->{
			return reservation.getRoom().getId() == room.getId(); 
		});
	}

}
