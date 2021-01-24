/**
* <h1>MeetingRoomController</h1>
* Description: Binds http requests to provide the MeetingRoom service 
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.service.MeetingRoomService;

@Controller
@RequestMapping(value = "/meetingrooms")
public class MeetingRoomController {
	@Autowired 
	private MeetingRoomService service;

	@GetMapping
	public ResponseEntity<List<MeetingRoom>> listMeetingRoom() {
		List<MeetingRoom> rooms = service.getAllMeetingRooms();
		if(rooms.isEmpty())  return ResponseEntity.noContent().build();
		return ResponseEntity.ok(rooms);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<MeetingRoom> getMeetingRoom(@PathVariable("id") Long id) {
		MeetingRoom room = service.getMeetingRoom(id);
		if (null == room) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(room);
	}

	@PostMapping
	public ResponseEntity<MeetingRoom> createMeetingRoom(@Valid @RequestBody MeetingRoom room, BindingResult result) {
		if (result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
		}
		MeetingRoom roomDB = service.createMeetingRoom(room);
		return ResponseEntity.status(HttpStatus.CREATED).body(roomDB);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<MeetingRoom> updateMeetingRoom(@PathVariable("id") Long id, @RequestBody MeetingRoom room) {
		room.setId(id);
		MeetingRoom roomDB = service.updateMeetingRoom(room);
		if (roomDB == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(roomDB);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<MeetingRoom> deleteMeetingRoom(@PathVariable("id") Long id) {
		MeetingRoom roomDeleted = service.deleteMeetingRoom(id);
		if (roomDeleted == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(roomDeleted);
	}

	private String formatMessage(BindingResult result) {
		List<Map<String, String>> errors = result.getFieldErrors().stream().map(err -> {
			Map<String, String> error = new HashMap<>();
			error.put(err.getField(), err.getDefaultMessage());
			return error;

		}).collect(Collectors.toList());
		ErrorMessage errorMessage = ErrorMessage.builder().code("01").messages(errors).build();
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = mapper.writeValueAsString(errorMessage);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
}
