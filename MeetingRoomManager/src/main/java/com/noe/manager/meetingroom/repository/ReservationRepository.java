/**
* <h1>ReservstionRepository</h1>
* Description: Provides a persistence layer for Reservation entities
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	public List<Reservation> findByDate(LocalDate date);
	public List<Reservation> findByRoom(MeetingRoom room);
	public List<Reservation> findByReservedFor(String reservedFor);
	public List<Reservation> findByReservedFrom(LocalTime reservedFrom);
	public List<Reservation> findByReservedUntil(LocalTime reservedUntil);
	public List<Reservation> findByRoomAndDate(MeetingRoom room, LocalDate date);
}
