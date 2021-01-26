/**
* <h1>ReservationService</h1>
* Description: Defines the interface that the ReservationService providers need to implement
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;

public interface ReservationService {
	public List<Reservation> getAllReservation();
    public Reservation getReservation(Long id);
    public Reservation createReservation(Reservation reservation);
    public Reservation updateReservation(Reservation reservation);
    public Reservation deleteReservation(Long id);
    
    public List<Reservation> findByReservedFor(String name);
    public List<Reservation> findByReservedFrom(LocalTime time);
    public List<Reservation> findByReservedUntil(LocalTime time);
    public List<Reservation> findByReservedDate(LocalDate date);
    public List<Reservation> findByRoom(MeetingRoom room);
    public List<Reservation> findAfterDate(LocalDate date);
    public List<Reservation> findByRoomAfterDate(MeetingRoom room, LocalDate date);
    
}
