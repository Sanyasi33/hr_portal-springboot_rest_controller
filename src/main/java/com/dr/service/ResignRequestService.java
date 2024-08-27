package com.dr.service;

import com.dr.enums.Status;
import com.dr.models.db.Employee;
import com.dr.models.db.ResignRequest;
import com.dr.repository.EmployeeRepository;
import com.dr.repository.ResignRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ResignRequestService {

    @Autowired
    private ResignRequestRepository resignRequestRepo;
    @Autowired
    private EmployeeRepository employeeRepo;

    //Employee Action
    public ResignRequest resignRequest(int id, String reason){
        Optional<Employee> opt=employeeRepo.findById(id);
        if(opt.isPresent()){
            Employee emp=opt.get();
            ResignRequest resignRequest=new ResignRequest();
            resignRequest.setResign(emp);
            resignRequest.setDate(LocalDate.now());
            resignRequest.setReason(reason);
            ResignRequest rr= resignRequestRepo.save(resignRequest);
            log.info("Resign request sent successfully");
            return rr;
        }
        else{
            log.error("Employee not found");
            return null;
        }
    }

    //HR Action
    public List<ResignRequest> viewResignationRequests(){
        List<ResignRequest> l= resignRequestRepo.findAll();
        log.info("All Resignation request fetched successfully");
        return l;
    }

    //HR Action
    public ResignRequest approveResign(int id){
        Optional<Employee> opt=employeeRepo.findById(id);
        if(opt.isPresent()){
            Employee employee=opt.get();
            ResignRequest resignRequest=employee.getResignRequest();
            resignRequest.setStatus(Status.APPROVED);
            ResignRequest rr= resignRequestRepo.save(resignRequest);

            // After approving resign request, updating the status to RESIGNED
            Employee emp=resignRequest.getResign();
            emp.setStatus(Status.RESIGNED);
            employeeRepo.save(emp);

            log.info("Resign request approved successfully");
            return rr;
        }
        else{
            log.error("Resign request not found for approval");
            return null;
        }
    }

    //HR Action
    public ResignRequest rejectResign(int id){
        Optional<Employee> opt=employeeRepo.findById(id);
        if(opt.isPresent()){
            Employee employee=opt.get();
            ResignRequest resignRequest=employee.getResignRequest();
            resignRequest.setStatus(Status.REJECTED);
            ResignRequest rr= resignRequestRepo.save(resignRequest);
            log.info("Resign request rejected successfully");
            return rr;
        }
        else{
            log.error("Resign request not found for rejection");
            return null;
        }
    }


    // ************************** View Leave requests status *************************
    public List<ResignRequest> resignRequests(int id){
        return resignRequestRepo.findResignRequestsByEmployeeId(id);
    }

}
