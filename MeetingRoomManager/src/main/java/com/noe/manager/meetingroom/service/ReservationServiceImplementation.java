/**
* <h1>ReservationServiceImplementation</h1>
* Description: Implementation of the ReservationService interface
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;
import com.noe.manager.meetingroom.repository.MeetingRoomRepository;
import com.noe.manager.meetingroom.repository.ReservationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationServiceImplementation implements ReservationService{

	private final ReservationRepository repo;
	private final MeetingRoomRepository repoMeetingRoom;
	
	/**
	 * @return a list of all the available Reservations*/
	@Override
	public List<Reservation> getAllReservation() {
		return repo.findAll();
	}

	/**
	 * @return a Reservation whose id attribute member matches the @param[id] if no match then null*/
	@Override
	public Reservation getReservation(Long id) {
		return repo.findById(id).orElse(null);
	}

	
	/**
	 * Persists(creates) the @param[reservation] entity in the DB following the next rules:
	 * 1.-Reservations cannot be created for past dates
	 * 2.-Reservations should not overlap with other reservations
	 * 3.-Reservations cannot last more than 2 hours
	 * 4.-Reservations time range should be valid
	 * 5.-Reservations time range should not be out of meeting room working hours
	 * 
	 * @return the reservation if it does not violate the established rules otherwise null*/
	@Override
	public Reservation createReservation(Reservation reservation) {
		/*1.-Reservations cannot be created for past dates*/
		LocalDate today = LocalDate.now();
		if(reservation.getDate().isBefore(today)) return null;
		/*Time tolerance can be changed if the system response is too slow*/
		LocalTime now = LocalTime.now();
		if(reservation.getReservedFrom().isBefore(now.minusMinutes(1))) return null;
		/*3.-Reservations cannot last more than 2 hours*/
		if(reservation.getReservedUntil().isAfter(reservation.getReservedFrom().plusHours(2))) return null;
		/*4.-Reservations time range should be valid*/
		if(reservation.getReservedUntil().isBefore(reservation.getReservedFrom())) return null;
		if(reservation.getReservedUntil().equals(reservation.getReservedFrom())) return null;
		/*5.-Reservations time range should not be out of meeting room working hours*/
		if(reservation.getReservedFrom().isBefore(reservation.getRoom().getAvailableFrom()))return null;
		if(reservation.getReservedUntil().isAfter(reservation.getRoom().getAvailableUntil()))return null;
		
		/*2.-Reservations should not overlap with other reservations*/
		/*Get all the reservations for the room*/
		List<Reservation> reservations = repo.findByRoomAndDate(reservation.getRoom(), reservation.getDate());
		/*determine if the reservation we are trying to create overlaps with other reservations*/
		boolean interference = reservations.stream().anyMatch(element->{	
			return 
					(reservation.getReservedUntil().isAfter(element.getReservedFrom()) && 
							(reservation.getReservedFrom().isBefore(element.getReservedFrom()) || reservation.getReservedFrom().equals(element.getReservedFrom())))
							|| 
							(reservation.getReservedUntil().isAfter(element.getReservedUntil()) && 
							(reservation.getReservedFrom().isBefore(element.getReservedUntil()) || reservation.getReservedFrom().equals(element.getReservedUntil())));
		});
		if(interference) return null;
		return repo.save(reservation);
	}

	/**
	 * Persists(updates) the @param[reservation] entity in the DB following the next rules:
	 * 1.-Reservations cannot be updated for past dates
	 * 2.-Reservations should not overlap with other reservations
	 * 3.-Reservations cannot last more than 2 hours
	 * 4.-Reservations time range should be valid
	 * 5.-Reservations time range should not be out of meeting room working hours
	 * 
	 * @return the reservation if it does not violate the established rules otherwise null*/
	@Override
	public Reservation updateReservation(Reservation reservation) {
		/*1.-Reservations cannot be created for past dates*/
		LocalDate today = LocalDate.now();
		if(reservation.getDate().isBefore(today)) return null;
		/*3.-Reservations cannot last more than 2 hours*/
		if(reservation.getReservedUntil().isAfter(reservation.getReservedFrom().plusHours(2))) return null;
		/*4.-Reservations time range should be valid*/
		if(reservation.getReservedUntil().isBefore(reservation.getReservedFrom())) return null;
		if(reservation.getReservedUntil().equals(reservation.getReservedFrom())) return null;
		
		/*If we are trying to modify something that does not exist return null*/
		Reservation toBeUpdated = repo.findById(reservation.getId()).orElse(null);
		if(toBeUpdated == null) return null;
		/*The Reservation->MeetingRoom relationship could be invalid if the user somehow changed it
		 * check and prevent invalid Reservation->MeetingRoom relationship*/
		MeetingRoom room = repoMeetingRoom.findById(reservation.getRoom().getId()).orElse(null);
		if(room == null) return null;
		reservation.setRoom(room);
		/*5.-Reservations time range should not be out of meeting room working hours*/
		if(reservation.getReservedFrom().isBefore(reservation.getRoom().getAvailableFrom()))return null;
		if(reservation.getReservedUntil().isAfter(reservation.getRoom().getAvailableUntil()))return null;
		
		/*2.-Reservations should not overlap with other reservations*/
		/*Get all the reservations for the room*/
		List<Reservation> reservations = repo.findByRoomAndDate(reservation.getRoom(), reservation.getDate());
		reservations.remove(toBeUpdated);
		/*determine if the reservation we are trying to create overlaps with other reservations*/
		boolean interference = reservations.stream().anyMatch(element->{	
			return 
					(reservation.getReservedUntil().isAfter(element.getReservedFrom()) && 
							(reservation.getReservedFrom().isBefore(element.getReservedFrom()) || reservation.getReservedFrom().equals(element.getReservedFrom())))
							|| 
							(reservation.getReservedUntil().isAfter(element.getReservedUntil()) && 
							(reservation.getReservedFrom().isBefore(element.getReservedUntil()) || reservation.getReservedFrom().equals(element.getReservedUntil())));
		});

		if(interference) return null;
		/*Update once all the conditions have been checked*/
		toBeUpdated.setDate(reservation.getDate());
		toBeUpdated.setReservedFrom(reservation.getReservedFrom());
		toBeUpdated.setReservedUntil(reservation.getReservedUntil());
		toBeUpdated.setReservedFor(reservation.getReservedFor());
		toBeUpdated.setRoom(reservation.getRoom());
		return repo.save(reservation);
	}

	/**
	 * @return a Reservation whose id attribute member matches the @param[id] after it has been found and deleted
	 * if no match then null*/
	@Override
	public Reservation deleteReservation(Long id) {
		Reservation toBeDeleted = repo.findById(id).orElse(null);
		if(toBeDeleted == null) return null;
		repo.delete(toBeDeleted);
		return toBeDeleted;
		
	}
	/**
	 * @return A list of Reservations whose reservedFor member attribute matches the @param[reservedFor] if there is no match then an empty list
	 */
	@Override
	public List<Reservation> findByReservedFor(String name) {
		return repo.findByReservedFor(name);
	}
	/**
	 * @return A list of Reservations whose reservedFrom member attribute matches the @param[reservedFrom] if there is no match then an empty list
	 */
	@Override
	public List<Reservation> findByReservedFrom(LocalTime time) {
		return repo.findByReservedFrom(time);
	}
	/**
	 * @return A list of Reservations whose reservedUntil member attribute matches the @param[reservedUntil] if there is no match then an empty list
	 */
	@Override
	public List<Reservation> findByReservedUntil(LocalTime time) {
		return repo.findByReservedUntil(time);
	}
	/**
	 * @return A list of Reservations whose date member attribute matches the @param[date] if there is no match then an empty list
	 */
	@Override
	public List<Reservation> findByReservedDate(LocalDate date) {
		return repo.findByDate(date);
	}
	/**
	 * @return A list of Reservations whose room member attribute matches the @param[room] if there is no match then an empty list
	 */
	@Override
	public List<Reservation> findByRoom(MeetingRoom room) {
		return repo.findByRoom(room);
				
	}
	/**
	 * @return A list of Reservations whose room member attribute matches the @param[room] and 
	 * whose date member attribute is located in the present or in the future taking as reference
	 * the @param[date] if there is no match then an empty list
	 */
	@Override
	public List<Reservation> findByRoomAfterDate(MeetingRoom room,LocalDate date){
		return repo.findByRoomAndAfterDate(room, date);
	}
	
	/**
	 * @return A list of Reservations whose date member attribute is located in the present or 
	 * in the future taking as reference the @param[date] if there is no match then an empty list
	 */
	@Query("SELECT r FROM Reservation r WHERE r.date >= ?1")
	public List<Reservation> findAfterDate(LocalDate date){
		return repo.findAfterDate(date);
	}

}
