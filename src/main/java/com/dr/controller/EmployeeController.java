package com.dr.controller;

import com.dr.models.db.Employee;
import com.dr.models.dto.EmployeeDto;
import com.dr.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name ="Employee Controller", description = "This is Employee APIs for Employee management operations ")
public class EmployeeController {

    @Autowired
    private EmployeeService empService;

    //Admin Action
    @Operation(summary = "Add new Employee", description = "Only admin can add new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/addEmployee")
    public ResponseEntity<Employee> addEmployee(@RequestBody EmployeeDto employeeDto){
        Employee employee = empService.addEmployee(employeeDto);
        if (employee!=null){
            return new ResponseEntity<>(employee, HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Admin Action
    @Operation(summary = "Update employee details", description = "Only admin can update the employee details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/updateEmployee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable int id, @RequestBody EmployeeDto employeeDto){
        Employee employee = empService.updateEmployee(id, employeeDto);
        if (employee!=null){
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Admin Action
    @Operation(summary = "Get all employee details", description = "Only admin can see the all employee details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getAllEmployees")
    public ResponseEntity<List<Employee>> getAllEmployees(){
        List<Employee> employees = empService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    //Admin Action
    @Operation(summary = "Get single emp record by using empId", description = "Only admin can see the emp details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable int id){
        Employee employee = empService.getEmployee(id);
        if (employee!=null){
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Employee Action
    @Operation(summary = "View employee details", description = "Only employee can view their details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/viewSelfDetails/{id}")
    public ResponseEntity<Employee> viewSelfDetails(@PathVariable int id){
        Employee employee=empService.viewSelfDetails(id);
        if (employee!=null){
            return new ResponseEntity<>(employee, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
