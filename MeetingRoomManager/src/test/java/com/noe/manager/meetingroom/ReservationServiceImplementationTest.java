/**
* <h1>ReservationServiceImplementationTest</h1>
* Description: Unit Test for the ReservationServiceImplementation class 
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;
import com.noe.manager.meetingroom.repository.MeetingRoomRepository;
import com.noe.manager.meetingroom.repository.ReservationRepository;
import com.noe.manager.meetingroom.service.ReservationService;
import com.noe.manager.meetingroom.service.ReservationServiceImplementation;

@SpringBootTest
public class ReservationServiceImplementationTest {
	@Mock
	private ReservationRepository repo;
	@Mock
	private MeetingRoomRepository repoMeetingRoom;
	
	private ReservationService service;
	
	MeetingRoom newRoom01;
	MeetingRoom newRoom02;
	
	Reservation reservation01; // valid reservation stored in db
	Reservation reservation02; // valid reservation stored in db
	Reservation reservation03; // valid reservation stored in db
	Reservation reservation04; // valid reservation stored in db
	Reservation reservation05; // reservation that last 1:59:59
	Reservation reservation06; // reservation that last 2:00:00
	Reservation reservation07; // reservation that lasts 2:00:01
	Reservation reservation08; // reservation with invalid reservedFrom
	Reservation reservation09; // reservation with invalid reservedUntil
	Reservation reservation10; // reservation when ReservedFrom is before firstAvailableHour  
	Reservation reservation11; // reservation when ReservedUntil is after lastAvailableHour  
	
	@BeforeEach
	public void setup() {
		service = new ReservationServiceImplementation(repo,repoMeetingRoom);
		newRoom01 = MeetingRoom.builder()
				.id(1L)
				.name("ServiceTestRoom01")
				.description("Service Test Meeting Room")
				.availableFrom(LocalTime.of(7, 0, 0))
				.availableUntil(LocalTime.of(20,59,0))
				.build();
		newRoom02 = MeetingRoom.builder()
				.id(2L)
				.name("ServiceTestRoom02")
				.description("Service Test Meeting Room")
				.availableFrom(LocalTime.of(7, 0, 0))
				.availableUntil(LocalTime.of(20,59,0))
				.build();
		reservation01 = Reservation.builder()
				.id(1L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(10, 0, 0))
				.reservedUntil(LocalTime.of(12,59,0))
				.reservedFor("Noe")
				.room(newRoom01)
				.build();
		reservation02 = Reservation.builder()
				.id(2L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(13, 0, 0))
				.reservedUntil(LocalTime.of(15,59,0))
				.reservedFor("Noe")
				.room(newRoom01)
				.build();
		reservation03 = Reservation.builder()
				.id(3L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(10, 0, 0))
				.reservedUntil(LocalTime.of(12,59,0))
				.reservedFor("Noe")
				.room(newRoom02)
				.build();
		reservation04 = Reservation.builder()
				.id(4L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(13, 0, 0))
				.reservedUntil(LocalTime.of(15,59,0))
				.reservedFor("Noe")
				.room(newRoom02)
				.build();
		reservation05 = Reservation.builder()
				.id(5L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(16, 0, 0))
				.reservedUntil(LocalTime.of(17,59,59))
				.reservedFor("Noe")
				.room(newRoom02)
				.build();
		reservation06 = Reservation.builder()
				.id(6L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(16,0,0))
				.reservedUntil(LocalTime.of(18,0,0))
				.reservedFor("Noe")
				.room(newRoom02)
				.build();
		reservation07 = Reservation.builder()
				.id(7L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(16,0,0))
				.reservedUntil(LocalTime.of(18,0,1))
				.reservedFor("Noe")
				.room(newRoom02)
				.build();
		reservation08 = Reservation.builder()
				.id(8L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(15, 58, 59))
				.reservedUntil(LocalTime.of(17,58,59))
				.reservedFor("Noe")
				.room(newRoom01)
				.build();
		reservation09 = Reservation.builder()
				.id(9L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(8, 30, 0))
				.reservedUntil(LocalTime.of(10,0,1))
				.reservedFor("Noe")
				.room(newRoom01)
				.build();
		reservation10 = Reservation.builder()
				.id(10L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(6, 59, 59))
				.reservedUntil(LocalTime.of(7,59,59))
				.reservedFor("Noe")
				.room(newRoom01)
				.build();
		reservation11 = Reservation.builder()
				.id(11L)
				.date(LocalDate.of(2021, 1, 22))
				.reservedFrom(LocalTime.of(19, 0, 0))
				.reservedUntil(LocalTime.of(21,0,0))
				.reservedFor("Noe")
				.room(newRoom01)
				.build();
		List<Reservation> allReservations = new ArrayList<Reservation>();
		allReservations.add(reservation01);
		allReservations.add(reservation02);
		allReservations.add(reservation03);
		allReservations.add(reservation04);
		
		List<Reservation> room01Reservations = new ArrayList<Reservation>();
		room01Reservations.add(reservation01);
		room01Reservations.add(reservation02);
		
		List<Reservation> room02Reservations = new ArrayList<Reservation>();
		room02Reservations.add(reservation03);
		room02Reservations.add(reservation04);
		
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(reservation01));
		Mockito.when(repo.findAll()).thenReturn(allReservations);
		Mockito.when(repo.findByRoomAndDate(newRoom01, LocalDate.of(2021, 1, 22))).thenReturn(room01Reservations);
		Mockito.when(repo.findByRoomAndDate(newRoom02, LocalDate.of(2021, 1, 22))).thenReturn(room01Reservations);
		Mockito.when(repo.findByDate(LocalDate.of(2021, 1, 22))).thenReturn(allReservations);
		Mockito.when(repo.save(reservation05)).thenReturn(reservation05);
		Mockito.when(repo.save(reservation06)).thenReturn(reservation06);
		Mockito.when(repo.save(reservation07)).thenReturn(reservation07);
		Mockito.when(repo.save(reservation08)).thenReturn(reservation08);
		Mockito.when(repo.save(reservation09)).thenReturn(reservation09);
		Mockito.when(repo.save(reservation10)).thenReturn(reservation10);
		Mockito.when(repo.save(reservation11)).thenReturn(reservation10);
		
	}
	
	@Test
	public void WhenReservationForSameDateAndHourAndRoomAlreadyExistsThenReturnNull() {
		Reservation testReservation = service.createReservation(reservation01);
		Assertions.assertThat(testReservation).isEqualTo(null);
	}
	@Test
	public void WhenCreatingReservationWithInvalidReservedUntilThenReturnNull() {
		Reservation testReservation = service.createReservation(reservation09);
		Assertions.assertThat(testReservation).isEqualTo(null);
	}
	@Test
	public void WhenCreatingReservationWithInvalidReservedFromThenReturnNull() {
		Reservation testReservation = service.createReservation(reservation08);
		Assertions.assertThat(testReservation).isEqualTo(null);
	}
	
	@Test
	public void WhenCreatingReservationThatLastsMoreThanTwoHoursThenReturnNull() {
		Reservation testReservation = service.createReservation(reservation07);
		Assertions.assertThat(testReservation).isEqualTo(null);
	}
	@Test
	public void WhenCreatingValidReservationThenReturnEntity() {
		reservation05.setDate(LocalDate.now());
		reservation05.setReservedFrom(LocalTime.now());
		reservation05.setReservedUntil(LocalTime.now().plusHours(2));
		Reservation testReservation = service.createReservation(reservation05);
		Assertions.assertThat(testReservation.getId()).isEqualTo(5L);
		reservation06.setDate(LocalDate.now());
		reservation06.setReservedFrom(LocalTime.now());
		reservation06.setReservedUntil(LocalTime.now().plusHours(2));
		testReservation = service.createReservation(reservation06);
		Assertions.assertThat(testReservation.getId()).isEqualTo(6L);
	}
	@Test
	public void WhenCreatingReservationThatEndsAfterLastAvailableHourThenReturnNull() {
		Reservation testReservation = service.createReservation(reservation10);
		Assertions.assertThat(testReservation).isEqualTo(null);
	}
	@Test
	public void WhenCreatingReservationThatStratsBeforeFirstAvailableHourThenReturnNull() {
		Reservation testReservation = service.createReservation(reservation11);
		Assertions.assertThat(testReservation).isEqualTo(null);
	}
}
