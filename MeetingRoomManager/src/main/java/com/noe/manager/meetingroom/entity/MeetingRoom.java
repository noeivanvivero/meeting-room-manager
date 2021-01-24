/**
* <h1>MeetingRoom</h1>
* Description: Entity class to represent the meeting rooms
*
* @author  Noe Ivan Vivero
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.entity;

import java.time.LocalTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_meetingroom")
@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class MeetingRoom {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@NotEmpty(message = "Meeting room name cannot be empty")
	private String name;
	
	private String description;
	@NotNull(message = "Meeting room first available hour cannot be empty")
	@Column(name = "available_from", columnDefinition = "TIME")
	private LocalTime availableFrom;
	@NotNull(message = "Meeting room first available hour cannot be empty")
	@Column(name = "available_until", columnDefinition = "TIME")
	private LocalTime availableUntil;
	@JsonBackReference
	@OneToMany(mappedBy="room", cascade = CascadeType.ALL)
	private Set<Reservation> reservations;
}
