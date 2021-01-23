/**
* <h1>Class name</h1>
* Description:
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;
import com.noe.manager.meetingroom.repository.ReservationRepository;

@DataJpaTest
public class ReservationRepositoryTest {
	@Autowired
	private ReservationRepository repo;
	
	/*Test if we can retrieve entities*/
	@Test public void WhenFindAll_ReturnsReservationList() {
		List<Reservation> reservations = repo.findAll();
		Assertions.assertThat(reservations.size()).isEqualTo(6);
	}
	
	/*Test if we can create entities*/
	@Test public void WhenSave_CreatesNewReservation() {
		List<Reservation> reservations = repo.findAll();
		Assertions.assertThat(reservations.size()).isEqualTo(6);
		Reservation newReservation = Reservation.builder()
				.id(7)
				.date(LocalDate.of(2022, 1, 22))
				.reservedFrom(LocalTime.of(10, 0, 0))
				.reservedUntil(LocalTime.of(12,59,0))
				.reservedFor("Noe").room(MeetingRoom.builder().id(1L).build())
				.build();
		newReservation = repo.save(newReservation);
		reservations = repo.findAll();
		Assertions.assertThat(reservations.size()).isEqualTo(7);
	}
	
	/*Test if we can delete entities*/
	@Test public void WhenDelete_ReturnsNewReservationList() {
		List<Reservation> reservations = repo.findAll();
		Assertions.assertThat(reservations.size()).isEqualTo(6);
		Reservation newReservation = Reservation.builder()
				.id(8)
				.date(LocalDate.of(2022, 1, 22))
				.reservedFrom(LocalTime.of(16, 0, 0))
				.reservedUntil(LocalTime.of(18,59,0))
				.reservedFor("Noe").room(MeetingRoom.builder().id(1).build())
				.build();
		newReservation = repo.save(newReservation);
		reservations = repo.findAll();
		Assertions.assertThat(reservations.contains(newReservation)).isTrue();
		repo.delete(newReservation);
		reservations = repo.findAll();
		Assertions.assertThat(reservations.contains(newReservation)).isFalse();
		
	}
	
	/*Test if we can update entities*/
	@Test public void WhenSave_UpdatesReservation() {
		List<Reservation> reservations = repo.findAll();
		Assertions.assertThat(reservations.size()).isGreaterThanOrEqualTo(6);
		Reservation toBeEdited = reservations.get(0);
		long id = toBeEdited.getId();
		toBeEdited.setDate(LocalDate.of(2022, 1, 24));
		toBeEdited.setReservedFrom(LocalTime.of(16, 0, 0));
		toBeEdited.setReservedUntil(LocalTime.of(18, 59, 0));
		toBeEdited.setReservedFor("New guest");
		toBeEdited.setRoom(MeetingRoom.builder().id(1L).build());
		repo.save(toBeEdited);
		Reservation afterEdition = repo.findById(id);
		Assertions.assertThat(afterEdition.getDate()).isEqualTo(LocalDate.of(2022, 1, 24));
		Assertions.assertThat(afterEdition.getReservedFrom()).isEqualTo(LocalTime.of(16, 0, 0));
		Assertions.assertThat(afterEdition.getReservedUntil()).isEqualTo(LocalTime.of(18, 59, 0));
		Assertions.assertThat(afterEdition.getReservedFor()).isEqualTo("New guest");
	}
	/*Test if we can find entities by id*/
	@Test public void WhenFindById_ReturnsReservationList() {
		Assertions.assertThat(repo.findById(1)).isNotNull();
		Assertions.assertThat(repo.findById(100)).isNull();
	}
	/*Test if we can find entities by reservation guest*/
	@Test public void WhenFindByReservedFor_ReturnsReservationList() {
		Assertions.assertThat(repo.findByReservedFor("Noe").size()).isGreaterThanOrEqualTo(6);
		Assertions.assertThat(repo.findByReservedFor("Unknown")).isEmpty();
	}
	/*Test if we can find entities by starting hour of reservation*/
	@Test public void WhenFindByReservedFrom_ReturnsReservationList() {
		Assertions.assertThat(repo.findByReservedFrom(LocalTime.of(10, 0, 0)).size()).isGreaterThanOrEqualTo(1);
		Assertions.assertThat(repo.findByReservedFrom(LocalTime.of(0, 0, 0))).isEmpty();
	}
	/*Test if we can find entities by last hour of reservation*/
	@Test public void WhenFindByReservedUntil_ReturnsReservationList() {
		Assertions.assertThat(repo.findByReservedUntil(LocalTime.of(12, 59, 00)).size()).isGreaterThanOrEqualTo(1);
		Assertions.assertThat(repo.findByReservedUntil(LocalTime.of(0, 0, 0))).isEmpty();
	}
	
	/*Test if we can find entities by date of reservation*/
	@Test public void WhenFindByDate_ReturnsReservationList() {
		Assertions.assertThat(repo.findByDate(LocalDate.of(2021,1,23)).size()).isGreaterThanOrEqualTo(6);
		Assertions.assertThat(repo.findByDate(LocalDate.of(2021,12,25))).isEmpty();
	}
}
