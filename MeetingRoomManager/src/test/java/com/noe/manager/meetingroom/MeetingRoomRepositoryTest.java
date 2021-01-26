/**
* <h1>MeetingRoomRepositoryTest</h1>
* Description: Unit tests for the MeetingRoom repository
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom;

import java.time.LocalTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.repository.MeetingRoomRepository;

@DataJpaTest
public class MeetingRoomRepositoryTest {
	@Autowired
	private MeetingRoomRepository repo;
	
	
	
	/*Test if we can retrieve entities*/
	@Test public void WhenFindAll_ReturnsMeetingRoomList() {
		List<MeetingRoom> rooms = repo.findAll();
		Assertions.assertThat(rooms.size()).isEqualTo(3);
	}
	
	/*Test if we can create entities*/
	@Test public void WhenSave_CreatesNewMeetingRoom() {
		List<MeetingRoom> rooms = repo.findAll();
		Assertions.assertThat(rooms.size()).isEqualTo(3);
		MeetingRoom newRoom = MeetingRoom.builder()
				.id(4)
				.name("TestRoom")
				.description("Test Meeting Room")
				.availableFrom(LocalTime.of(7, 0, 0))
				.availableUntil(LocalTime.of(20,59,0))
				.build();
		newRoom = repo.save(newRoom);
		rooms = repo.findAll();
		Assertions.assertThat(rooms.size()).isEqualTo(4);
	}
	
	/*Test if we can delete entities*/
	@Test public void WhenDelete_ReturnsNewMeetingRoomList() {
		List<MeetingRoom> rooms = repo.findAll();
		Assertions.assertThat(rooms.size()).isEqualTo(3);
		MeetingRoom newRoom = MeetingRoom.builder()
				.id(4)
				.name("DeelteRoom")
				.description("Test Meeting Room")
				.availableFrom(LocalTime.of(7, 0, 0))
				.availableUntil(LocalTime.of(20,59,0))
				.build();
		newRoom = repo.save(newRoom);
		rooms = repo.findAll();
		Assertions.assertThat(rooms.contains(newRoom)).isTrue();
		repo.delete(newRoom);
		rooms = repo.findAll();
		Assertions.assertThat(rooms.contains(newRoom)).isFalse();
		
	}
	
	/*Test if we can update entities*/
	@Test public void WhenSave_UpdatesRoom() {
		List<MeetingRoom> rooms = repo.findAll();
		Assertions.assertThat(rooms.size()).isGreaterThanOrEqualTo(3);
		MeetingRoom toBeEdited = rooms.get(0);
		long id = toBeEdited.getId();
		toBeEdited.setName("New Name");
		toBeEdited.setDescription("New Description");
		toBeEdited.setAvailableFrom(LocalTime.of(8, 0, 0));
		toBeEdited.setAvailableUntil(LocalTime.of(22, 59, 59));
		repo.save(toBeEdited);
		MeetingRoom afterEdition = repo.findById(id).orElse(null);
		Assertions.assertThat(afterEdition.getName()).isEqualTo("New Name");
		Assertions.assertThat(afterEdition.getDescription()).isEqualTo("New Description");
		Assertions.assertThat(afterEdition.getAvailableFrom()).isEqualTo(LocalTime.of(8, 0, 0));
		Assertions.assertThat(afterEdition.getAvailableUntil()).isEqualTo(LocalTime.of(22, 59, 59));
	}
	/*Test if we can find entities by id*/
	@Test public void WhenFindById_ReturnsMeetingRoomList() {
		Assertions.assertThat(repo.findById(1L).orElse(null)).isNotNull();
		Assertions.assertThat(repo.findById(100L).orElse(null)).isNull();
	}
	/*Test if we can find entities by name*/
	@Test public void WhenFindByName_ReturnsMeetingRoomList() {
		Assertions.assertThat(repo.findByName("Ocean").size()).isGreaterThanOrEqualTo(1);
		Assertions.assertThat(repo.findByName("Unknown")).isEmpty();
	}
	/*Test if we can find entities by starting hour of availability*/
	@Test public void WhenFindByAvailableFrom_ReturnsMeetingRoomList() {
		Assertions.assertThat(repo.findByAvailableFrom(LocalTime.of(7, 0, 0)).size()).isGreaterThanOrEqualTo(3);
		Assertions.assertThat(repo.findByAvailableFrom(LocalTime.of(2, 0, 0))).isEmpty();
	}
	/*Test if we can find entities by last hour of availability*/
	@Test public void WhenFindByAvailableUntil_ReturnsMeetingRoomList() {
		Assertions.assertThat(repo.findByAvailableUntil(LocalTime.of(20, 59, 59)).size()).isGreaterThanOrEqualTo(3);
		Assertions.assertThat(repo.findByAvailableFrom(LocalTime.of(0, 0, 0))).isEmpty();
	}
	
	
	
	
}
