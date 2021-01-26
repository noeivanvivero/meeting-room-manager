/**
* <h1>MeetingRoomServiceImplementationTest</h1>
* Description: Unit Test for the MeetingRoomServiceImplementation class 
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.repository.MeetingRoomRepository;
import com.noe.manager.meetingroom.repository.ReservationRepository;
import com.noe.manager.meetingroom.service.MeetingRoomService;
import com.noe.manager.meetingroom.service.MeetingRoomServiceImplementation;

@SpringBootTest
public class MeetingRoomServiceImplementationTest {
	@Mock
	private MeetingRoomRepository repo;
	@Mock
	private ReservationRepository repoReservations;
	
	private MeetingRoomService service;
	
	MeetingRoom newRoom01;
	MeetingRoom newRoom02;
	
	@BeforeEach
	public void setup() {
		service = new MeetingRoomServiceImplementation(repo, repoReservations);
		newRoom01 = MeetingRoom.builder()
				.id(1L)
				.name("ServiceTestRoom01")
				.description("Service Test Meeting Room")
				.availableFrom(LocalTime.of(7, 0, 0))
				.availableUntil(LocalTime.of(20,59,0))
				.build();
		newRoom02 = MeetingRoom.builder()
				.id(1L)
				.name("ServiceTestRoom02")
				.description("Service Test Meeting Room")
				.availableFrom(LocalTime.of(7, 0, 0))
				.availableUntil(LocalTime.of(20,59,0))
				.build();
		List<MeetingRoom> list= new ArrayList<MeetingRoom>();
		list.add(newRoom01);
		list.add(newRoom02);
		Mockito.when(repo.findById(1L)).thenReturn(Optional.of(newRoom01));
		//Mockito.when(repo.save(newRoom01)).thenReturn(newRoom01);
		//Mockito.when(repo.findByName("ServiceTestRoom01")).thenReturn(list);
		//Mockito.when(repo.findByAvailableFrom(LocalTime.of(7, 0, 0))).thenReturn(list);
		//Mockito.when(repo.findByAvailableUntil(LocalTime.of(20, 59, 0))).thenReturn(list);
		Mockito.when(repo.findAll()).thenReturn(list);
		
	}
	@Test
	public void WhenValidGetMeetingRoomThenReturnMeetingRoom(){
		MeetingRoom room = service.getMeetingRoom(1L);
		Assertions.assertThat(room.getName()).isEqualTo("ServiceTestRoom01");
		
	}
	@Test
	public void WhenValidGetAllMeetingRoomThenReturnMeetingRoom(){
		List<MeetingRoom> rooms = service.getAllMeetingRooms();
		rooms = rooms.stream().filter(element->element.getId()==1L).collect(Collectors.toList());
		Assertions.assertThat(rooms.isEmpty()).isFalse();
		
	}
	
	
}
