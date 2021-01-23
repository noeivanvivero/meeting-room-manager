/**
* <h1>Reservation</h1>
* Description: Entity class to represent the meeting room reservations
*
* @author  Noe Ivan Vivero
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "tbl_reservation")
@Data
@AllArgsConstructor @NoArgsConstructor @Builder
public class Reservation {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	@Column(name = "reserved_date", columnDefinition = "DATE")
	private LocalDate date;
	@Column(name = "reserved_from", columnDefinition = "TIME")
	private LocalTime reservedFrom;
	@Column(name = "reserved_until", columnDefinition = "TIME")
	private LocalTime reservedUntil;
	@Column(name = "reserved_for")
	private String reservedFor;
	@ManyToOne
	@JoinColumn(name="room_id")
	private MeetingRoom room;
}
