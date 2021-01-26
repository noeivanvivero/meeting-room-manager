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
import org.springframework.data.jpa.repository.Query;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

	/**
	 * @return A list of Reservations whose date member attribute matches the @param[date] if there is no match then an empty list
	 */
	public List<Reservation> findByDate(LocalDate date);
	/**
	 * @return A list of Reservations whose room member attribute matches the @param[room] if there is no match then an empty list
	 */
	public List<Reservation> findByRoom(MeetingRoom room);
	/**
	 * @return A list of Reservations whose reservedFor member attribute matches the @param[reservedFor] if there is no match then an empty list
	 */
	public List<Reservation> findByReservedFor(String reservedFor);
	/**
	 * @return A list of Reservations whose reservedFrom member attribute matches the @param[reservedFrom] if there is no match then an empty list
	 */
	public List<Reservation> findByReservedFrom(LocalTime reservedFrom);
	/**
	 * @return A list of Reservations whose reservedUntil member attribute matches the @param[reservedUntil] if there is no match then an empty list
	 */
	public List<Reservation> findByReservedUntil(LocalTime reservedUntil);
	/**
	 * @return A list of Reservations whose room member attribute matches the @param[room] and 
	 * whose date member attribute matches the @param[date] if there is no match then an empty list
	 */
	public List<Reservation> findByRoomAndDate(MeetingRoom room, LocalDate date);
	
	
	/**
	 * @return A list of Reservations whose room member attribute matches the @param[room] and 
	 * whose date member attribute is located in the present or in the future taking as reference
	 * the @param[date] if there is no match then an empty list
	 */
	@Query("SELECT r FROM Reservation r WHERE r.date >= ?2 and r.room = ?1")
	public List<Reservation> findByRoomAndAfterDate(MeetingRoom room, LocalDate date);
}
