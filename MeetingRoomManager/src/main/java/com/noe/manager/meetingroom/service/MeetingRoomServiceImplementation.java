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
	
	/**
	 * @return a list of all the available MeetingRooms*/
	@Override
	public List<MeetingRoom> getAllMeetingRooms() {
		return repo.findAll();
	}

	/**
	 * @return a MeetingRoom whose id attribute member matches the @param[id] if no match then null*/
	@Override
	public MeetingRoom getMeetingRoom(Long id) {
		return repo.findById(id).orElse(null);
	}

	/**
	 * Persists(creates) the @param[room] entity in the DB following the next rules:
	 * 1.-Meeting rooms working-hours time-range should be valid
	 * 2.-Meeting rooms cannot share the same name*/
	@Override
	public MeetingRoom createMeetingRoom(MeetingRoom room) {
		/*1.-Reservations working hours time range should be valid*/
		if(room.getAvailableUntil().isBefore(room.getAvailableFrom())) return null;
		if(room.getAvailableFrom().equals(room.getAvailableUntil())) return null;
		/*2.-Meeting rooms cannot share the same name*/
		List<MeetingRoom> roomDB = repo.findByName(room.getName());
		if(roomDB.isEmpty()) return repo.save(room);
		else return null;
	}

	/**
	 * Persists(updates) the @param[room] entity in the DB following the next rules:
	 * 1.-Meeting rooms working-hours time-range should be valid
	 * 2.-Meeting rooms cannot share the same name*/
	@Override
	public MeetingRoom updateMeetingRoom(MeetingRoom room) {
		/*1.-Meeting rooms working-hours time-range should be valid*/
		if(room.getAvailableUntil().isBefore(room.getAvailableFrom())) return null;
		if(room.getAvailableFrom().equals(room.getAvailableUntil())) return null;
		/*If we are trying to modify something that does not exist return null*/
		MeetingRoom toBeEdited = getMeetingRoom(room.getId());
		if(toBeEdited == null) return null;
		/*2.-Meeting rooms cannot share the same name*/
		List<MeetingRoom> roomDB = repo.findByName(room.getName());
		roomDB.remove(toBeEdited);
		if(!roomDB.isEmpty()) return null;
		/*When making an update to the working-hours time-range prevent changes that affect future reservations*/
		LocalDate today = LocalDate.now();
		List<Reservation> reservations = repoReservation.findByRoomAndAfterDate(toBeEdited,today);
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
	/**
	 * @return a MeetingRoom whose id attribute member matches the @param[id] after it has been found and deleted
	 * if no match then null*/
	@Override
	public MeetingRoom deleteMeetingRoom(Long id) {
		MeetingRoom toBeDeleted = getMeetingRoom(id);
		if(toBeDeleted == null) return null;
		repo.delete(toBeDeleted);
		return toBeDeleted;
	}
	/**
	 * @return A list of MeetingRooms whose name matches the @param[name] if there is no match then an empty list
	 */
	@Override
	public List<MeetingRoom> findByName(String name) {
		return repo.findByName(name);
	}
	/**
	 * @return A list of MeetingRooms whose availableFrom member attribute matches the @param[availableFrom] if there is no match then an empty list
	 */
	@Override
	public List<MeetingRoom> findByAvailableFrom(LocalTime time) {
		return repo.findByAvailableFrom(time);
	}
	/**
	 * @return A list of MeetingRooms whose availableUntil member attribute matches the @param[availableUntil] if there is no match then an empty list
	 */
	@Override
	public List<MeetingRoom> findByAvailableUntil(LocalTime time) {
		return repo.findByAvailableUntil(time);
	}
	
	/**
	 * @return A list of MeetingRooms that are currently available
	 */
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
	
	/**
	 * Helper function that determines if a room is contained ina list of reservations */
	private boolean findCurrentlyAvailableHelper(MeetingRoom room, List<Reservation> reservations ) {
		return reservations.stream().anyMatch(reservation->{
			return reservation.getRoom().getId() == room.getId(); 
		});
	}

}
