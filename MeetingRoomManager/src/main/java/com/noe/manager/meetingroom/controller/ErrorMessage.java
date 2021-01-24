/**
* <h1>ErrorMessgae</h1>
* Description: Stores the errors during entity binding process
*
* @author  Noe Ivan
* @version 1.0
* @since   2021-01-22 
*/


package com.noe.manager.meetingroom.controller;
import java.util.List;
import java.util.Map;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Builder
public class ErrorMessage {
	private String code ;
    private List<Map<String, String >> messages ;
}
