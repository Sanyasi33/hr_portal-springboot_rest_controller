package com.dr.controller;

import com.dr.models.db.ResignRequest;
import com.dr.service.ResignRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Resign Request Controller", description = "In this controller all the resign related APIs developed")
public class ResignRequestController {

    @Autowired
    private ResignRequestService resignRequestService;

    //Employee Action
    @Operation(summary = "Put Resignation letter", description = "Employee will enter empId to put resignation letter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resignation letter submitted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/resignRequest/{id}")
    public ResponseEntity<ResignRequest> resignRequest(@PathVariable int id, @RequestParam String reason){
        ResignRequest resignRequest = resignRequestService.resignRequest(id, reason);
        if (resignRequest!=null){
            return new ResponseEntity<>(resignRequest, HttpStatus.CREATED);
        }
        else
            return ResponseEntity.internalServerError().build();
    }

    //HR Action
    @Operation(summary = "View all resignation requests", description = "Admin can view all resignation requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resignation requests fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/viewResignationRequests")
    public ResponseEntity<List<ResignRequest>> viewResignationRequests(){
        return new ResponseEntity<>(resignRequestService.viewResignationRequests(), HttpStatus.OK);
    }

    //HR Action
    @Operation(summary = "Approve resignation request", description = "HR can approve a resignation request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resignation request approved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/approveResign/{id}")
    public ResponseEntity<ResignRequest> approveResign(@PathVariable int id){
        ResignRequest resignRequest = resignRequestService.approveResign(id);
        if (resignRequest!=null){
            return new ResponseEntity<>(resignRequest, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //HR Action
    @Operation(summary = "Reject resignation request", description = "HR can reject a resignation request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resignation request rejected successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/rejectResign/{id}")
    public ResponseEntity<ResignRequest> rejectResign(@PathVariable int id){
        ResignRequest resignRequest=resignRequestService.rejectResign(id);
        if (resignRequest!=null){
            return new ResponseEntity<>(resignRequest, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/resignStatus/{id}")
    public List<ResignRequest> leaveStatus(@PathVariable int id, Model model){
        return resignRequestService.resignRequests(id);
    }

}
