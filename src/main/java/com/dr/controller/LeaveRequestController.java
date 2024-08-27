package com.dr.controller;

import com.dr.models.db.LeaveRequest;
import com.dr.models.dto.LeaveRequestDto;
import com.dr.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Leave Request Controller", description = "In this controller all the leave related APIs developed")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    //Employee
    @Operation(summary = "Apply for leave", description = "Employee will enter empId to send leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request sent successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/applyLeave/{id}")
    public ResponseEntity<LeaveRequest> applyLeave(@PathVariable int id, @RequestBody LeaveRequestDto leaveRequestDto){
        LeaveRequest leaveRequest=leaveRequestService.applyLeave(id, leaveRequestDto);
        /*if (leaveRequestDto.getStartDate().isBefore(LocalDate.now())){
            throw  new RuntimeException("Start date should not be before today's date");
        }
        else if (leaveRequestDto.getEndDate().isBefore(leaveRequestDto.getStartDate())){
            throw  new RuntimeException("End date should be after start date");
        }*/
        if (leaveRequest!=null){
            return new ResponseEntity<>(leaveRequest, HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //HR Action
    @Operation(summary = "View all applied leave requests", description = "Admin can see all the applied leaves")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All leave requests retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/viewAppliedLeaves")
    public ResponseEntity<List<LeaveRequest>> viewAppliedLeaves(){
        return new ResponseEntity<>(leaveRequestService.viewAppliedLeaves(), HttpStatus.OK);
    }

    //HR Action
    @Operation(summary = "Approve leave request", description = "Admin will enter leave id to approve leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request approved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/approveLeave/{id}")
    public ResponseEntity<LeaveRequest> approveLeave(@PathVariable int id){
        LeaveRequest leaveRequest= leaveRequestService.approveLeave(id);
        if (leaveRequest!=null){
            return new ResponseEntity<>(leaveRequest, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //HR Action
    @Operation(summary = "Reject leave request", description = "Admin will enter leave id to reject leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request rejected successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/rejectLeave/{id}")
    public ResponseEntity<LeaveRequest> rejectLeave(@PathVariable int id){
        LeaveRequest leaveRequest=leaveRequestService.rejectLeave(id);
        if (leaveRequest!=null){
            return new ResponseEntity<>(leaveRequest, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ************************** View Leave requests status *************************
    @GetMapping("/leaveStatus/{id}")
    public List<LeaveRequest> leaveStatus(@PathVariable int id){
        return leaveRequestService.leaveRequests(id);
    }

}
