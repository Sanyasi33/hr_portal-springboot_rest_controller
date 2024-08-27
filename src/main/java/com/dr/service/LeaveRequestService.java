package com.dr.service;

import com.dr.enums.Status;
import com.dr.models.db.Employee;
import com.dr.models.db.LeaveRequest;
import com.dr.models.dto.LeaveRequestDto;
import com.dr.repository.EmployeeRepository;
import com.dr.repository.LeaveRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LeaveRequestService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepo;
    @Autowired
    private EmployeeRepository employeeRepo;

    //Employee Action
    public LeaveRequest applyLeave(int id, LeaveRequestDto leaveRequestDto){
        Optional<Employee> opt=employeeRepo.findById(id);
        if(opt.isPresent()){
            Employee emp=opt.get();
            LeaveRequest leaveRequest=new LeaveRequest();
            leaveRequest.setLeave(emp);
            BeanUtils.copyProperties(leaveRequestDto, leaveRequest);
            LeaveRequest lr= leaveRequestRepo.save(leaveRequest);
            log.info("Leave applied successfully");
            return lr;
        }
        else
            return null;
    }

    //HR Action
    public List<LeaveRequest> viewAppliedLeaves(){
        List<LeaveRequest> l=leaveRequestRepo.findAll();
        log.info("All leave requests fetched successfully");
        return l;
    }

    //HR Action
    public LeaveRequest approveLeave(int leaveId){
        Optional<LeaveRequest> opt=leaveRequestRepo.findById(leaveId);
        if (opt.isPresent()){
            LeaveRequest leaveRequest=opt.get();
            leaveRequest.setStatus(Status.APPROVED);
            LeaveRequest lr= leaveRequestRepo.save(leaveRequest);
            log.info("Leave approved successfully");
            return lr;
        }
        else{
            log.error("Leave request not found and unable to approve");
            return null;
        }
    }

    //HR Action
    public LeaveRequest rejectLeave(int leaveId){
        Optional<LeaveRequest> opt=leaveRequestRepo.findById(leaveId);
        if (opt.isPresent()){
            LeaveRequest leaveRequest=opt.get();
            leaveRequest.setStatus(Status.REJECTED);
            LeaveRequest lr= leaveRequestRepo.save(leaveRequest);
            log.info("Leave rejected successfully");
            return lr;
        }
        else{
            log.error("Leave request not found and unable to reject");
            return null;
        }
    }

    // ************************** View Leave requests status *************************
    public List<LeaveRequest> leaveRequests(int id){
        return leaveRequestRepo.findLeaveRequestsByEmployeeId(id);
    }

}
