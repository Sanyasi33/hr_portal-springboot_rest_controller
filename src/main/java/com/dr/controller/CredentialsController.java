package com.dr.controller;

import com.dr.models.dto.UserDto;
import com.dr.service.CredentialsService;
import com.dr.service.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Credential Controller", description = "In this controller all the security related APIs developed")
public class CredentialsController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private JWTService jwtService;


    //Employee Action
    @Operation(summary = "It will show the temp loin page to enter temp credential")
    @GetMapping("/tempLogin")
    public ResponseEntity<String> welcomeEmp(){
        return ResponseEntity.ok("Welcome to HR Portal !");
        //User will enter empId & pwd then redirect to "/tempLogin" endpoint with POST mode request
    }

    //Employee Action
    @Operation(summary = "Employee verifies the temporary password", description = "Enter empId and password which was sent to your email account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Temp credential verified successfully"),
        @ApiResponse(responseCode = "404", description = "Employee Id not found"),
        @ApiResponse(responseCode = "400", description = "Invalid password")
    })
    @PostMapping("/tempLogin")
    public ResponseEntity<String> tempLogin(@RequestParam int id, @RequestParam String pwd){
        String result=credentialsService.tempPwdVerify(id, pwd);
        if (result==null){
            return new ResponseEntity<>("Emp Id not found", HttpStatus.NOT_FOUND);
        }
        else{
            if (result.equals("valid")){
                return ResponseEntity.ok("Your temp password Verified successfully please Reset your password");
                //Redirect to "/resetPwd" endpoint with GET mode request
            }
            else {
                return ResponseEntity.ok("Invalid password");
            }
        }
    }

    //Employee Action
    @Operation(summary = "It will show the reset password page to enter new password")
    @GetMapping("/resetPwd")
    public ResponseEntity<String> resetPwd(){
        return ResponseEntity.ok("Please Reset your Password");
        //User will enter new pwd & confirmPwd then redirect to "/resetPwd" endpoint with POST mode request
    }

    //Employee Action
    @Operation(summary = "Employee needs to enter new password", description = "Enter new password and confirm password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "404", description = "Employee Id not found"),
            @ApiResponse(responseCode = "400", description = "Passwords are not matching")
    })
    @PostMapping("/resetPwd")
    public ResponseEntity<String> resetPwd(@RequestParam int id, @RequestParam String pwd, @RequestParam String confirmPwd){
        if (pwd.equals(confirmPwd)){
            boolean empExist = credentialsService.getIdPwd(id, pwd);
            if (empExist){
                return ResponseEntity.ok("Password Reset Successfully");
                //Now redirect to "/empLogin" endpoint
            }
            else
                return ResponseEntity.ok("Employee does not exist with id: "+id);
        }
        else
            return ResponseEntity.ok("Passwords are not matching");
    }

    @GetMapping("/denied")
    public ResponseEntity<String> denied(){
        return new  ResponseEntity<>("Access Denied", HttpStatus.FORBIDDEN);
    }



    //**************************** JWT Related... ****************************//

    // Get JWT Token
    @PostMapping("/loginForToken")
    public ResponseEntity<String> getJwtToken(@RequestBody UserDto userDto){
        boolean isValid = credentialsService.validateUser(userDto.getUsername(), userDto.getPassword());
        if(isValid){
            String token = jwtService.generateToken(userDto.getUsername());
            return ResponseEntity.ok(token);
        }
        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
}
