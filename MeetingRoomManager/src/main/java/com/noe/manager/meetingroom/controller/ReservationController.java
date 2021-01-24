/**
* <h1>REservationController</h1>
* Description:
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noe.manager.meetingroom.entity.MeetingRoom;
import com.noe.manager.meetingroom.entity.Reservation;
import com.noe.manager.meetingroom.service.ReservationService;

@Controller
@RequestMapping(value = "/reservations")
public class ReservationController {
	@Autowired 
	private ReservationService service;
	
	@GetMapping
	public ResponseEntity<List<Reservation>> listReservation(@RequestParam(name = "roomId", required = false) Long roomId) {
		List<Reservation> reservations = new ArrayList<>();
		if(roomId == null) reservations = service.getAllReservation();
		else {
			reservations = service.findByRoom(MeetingRoom.builder().id(roomId).build());
		}
		if(reservations.isEmpty())  return ResponseEntity.noContent().build();
		return ResponseEntity.ok(reservations);
	}
	@GetMapping(value = "/{id}")
	public ResponseEntity<Reservation> getReservation(@PathVariable("id") Long id) {
		Reservation reservation = service.getReservation(id);
		if (null == reservation) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(reservation);
	}

	@PostMapping
	public ResponseEntity<Reservation> createReservation(@Valid @RequestBody Reservation reservation, BindingResult result) {
		if (result.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
		}
		Reservation reservationDB = service.createReservation(reservation);
		return ResponseEntity.status(HttpStatus.CREATED).body(reservationDB);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Reservation> updateReservation(@PathVariable("id") Long id, @RequestBody Reservation reservation) {
		reservation.setId(id);
		Reservation reservationDB = service.updateReservation(reservation);
		if (reservationDB == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(reservationDB);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Reservation> deleteReservation(@PathVariable("id") Long id) {
		Reservation reservationDeleted = service.deleteReservation(id);
		if (reservationDeleted == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(reservationDeleted);
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
