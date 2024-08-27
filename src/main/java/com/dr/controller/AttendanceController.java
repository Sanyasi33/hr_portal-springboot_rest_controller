package com.dr.controller;

import com.dr.models.db.Attendance;
import com.dr.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Attendance Controller", description = "In this controller all attendance related APIs developed")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;


    //Employee Action
    @Operation(summary = "Add check-in", description = "Enter empId to add check-in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Check-in added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/addCheckIn/{id}")
    public ResponseEntity<?> addCheckIn(@PathVariable int id){
        Attendance attendance=attendanceService.addCheckIn(id);
        if (attendance!=null){
            return new ResponseEntity<>(attendance, HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>("Today's Check-In already raised",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Employee Action
    @Operation(summary = "Add check-out", description = "Enter empId to add check-out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-out added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/addCheckOut/{id}")
    public ResponseEntity<Attendance> addCheckOut(@PathVariable int id){
        Attendance attendance=attendanceService.addCheckOut(id);
        if (attendance!=null){
            return new ResponseEntity<>(attendance, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
