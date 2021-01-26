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
	
	@Override
	public List<Reservation> getAllReservation() {
		return repo.findAll();
	}

	@Override
	public Reservation getReservation(Long id) {
		return repo.findById(id).orElse(null);
	}

	@Override
	public Reservation createReservation(Reservation reservation) {
		
		LocalDate today = LocalDate.now();
		if(reservation.getDate().isBefore(today)) return null;
		LocalTime now = LocalTime.now();
		/*The tolerance can be changed if the system response is too slow*/
		if(reservation.getReservedFrom().isBefore(now.minusMinutes(2))) return null;
		if(reservation.getReservedUntil().isAfter(reservation.getReservedFrom().plusHours(2))) return null;
		if(reservation.getReservedUntil().isBefore(reservation.getReservedFrom())) return null;
		if(reservation.getReservedUntil().equals(reservation.getReservedFrom())) return null;
		if(reservation.getReservedFrom().isBefore(reservation.getRoom().getAvailableFrom()))return null;
		if(reservation.getReservedUntil().isAfter(reservation.getRoom().getAvailableUntil()))return null;
		
		List<Reservation> reservations = repo.findByRoomAndDate(reservation.getRoom(), reservation.getDate());
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

	@Override
	public Reservation updateReservation(Reservation reservation) {
		LocalDate today = LocalDate.now();
		if(reservation.getDate().isBefore(today)) return null;
		if(reservation.getReservedUntil().isAfter(reservation.getReservedFrom().plusHours(2))) return null;
		if(reservation.getReservedUntil().isBefore(reservation.getReservedFrom())) return null;
		if(reservation.getReservedUntil().equals(reservation.getReservedFrom())) return null;
		
		
		Reservation toBeUpdated = repo.findById(reservation.getId()).orElse(null);
		if(toBeUpdated == null) return null;
		MeetingRoom room = repoMeetingRoom.findById(reservation.getRoom().getId()).orElse(null);
		if(room == null) return null;
		reservation.setRoom(room);
		if(reservation.getReservedFrom().isBefore(reservation.getRoom().getAvailableFrom()))return null;
		if(reservation.getReservedUntil().isAfter(reservation.getRoom().getAvailableUntil()))return null;
		
		
		List<Reservation> reservations = repo.findByRoomAndDate(reservation.getRoom(), reservation.getDate());
		reservations.remove(toBeUpdated);
		boolean interference = reservations.stream().anyMatch(element->{	
			return 
					(reservation.getReservedUntil().isAfter(element.getReservedFrom()) && 
							(reservation.getReservedFrom().isBefore(element.getReservedFrom()) || reservation.getReservedFrom().equals(element.getReservedFrom())))
							|| 
							(reservation.getReservedUntil().isAfter(element.getReservedUntil()) && 
							(reservation.getReservedFrom().isBefore(element.getReservedUntil()) || reservation.getReservedFrom().equals(element.getReservedUntil())));
		});

		if(interference) return null;
		toBeUpdated.setDate(reservation.getDate());
		toBeUpdated.setReservedFrom(reservation.getReservedFrom());
		toBeUpdated.setReservedUntil(reservation.getReservedUntil());
		toBeUpdated.setReservedFor(reservation.getReservedFor());
		toBeUpdated.setRoom(reservation.getRoom());
		return repo.save(reservation);
	}

	@Override
	public Reservation deleteReservation(Long id) {
		Reservation toBeDeleted = repo.findById(id).orElse(null);
		if(toBeDeleted == null) return null;
		repo.delete(toBeDeleted);
		return toBeDeleted;
		
	}

	@Override
	public List<Reservation> findByReservedFor(String name) {
		return repo.findByReservedFor(name);
	}

	@Override
	public List<Reservation> findByReservedFrom(LocalTime time) {
		return repo.findByReservedFrom(time);
	}

	@Override
	public List<Reservation> findByReservedUntil(LocalTime time) {
		return repo.findByReservedUntil(time);
	}

	@Override
	public List<Reservation> findByReservedDate(LocalDate date) {
		return repo.findByDate(date);
	}

	@Override
	public List<Reservation> findByRoom(MeetingRoom room) {
		return repo.findByRoom(room);
				
	}

}
