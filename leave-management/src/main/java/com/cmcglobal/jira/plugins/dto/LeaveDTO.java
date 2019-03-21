package com.cmcglobal.jira.plugins.dto;

import java.util.Date;

public class LeaveDTO {
    private String subject;
    private String jobPoisition;
    private String contractType;
    private String leaveType;
    private Date startDayOff;
    private Double totalDayOff;
    private String reasonForLeave;
    private String workAuthorizationFor;
    private Double Phone;

    public LeaveDTO() {
    }

    public LeaveDTO(String subject, String jobPoisition, String contractType, String leaveType, Date startDayOff, Double totalDayOff, String reasonForLeave, String workAuthorizationFor, Double phone) {
        this.subject = subject;
        this.jobPoisition = jobPoisition;
        this.contractType = contractType;
        this.leaveType = leaveType;
        this.startDayOff = startDayOff;
        this.totalDayOff = totalDayOff;
        this.reasonForLeave = reasonForLeave;
        this.workAuthorizationFor = workAuthorizationFor;
        Phone = phone;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getJobPoisition() {
        return jobPoisition;
    }

    public void setJobPoisition(String jobPoisition) {
        this.jobPoisition = jobPoisition;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Date getStartDayOff() {
        return startDayOff;
    }

    public void setStartDayOff(Date startDayOff) {
        this.startDayOff = startDayOff;
    }

    public Double getTotalDayOff() {
        return totalDayOff;
    }

    public void setTotalDayOff(Double totalDayOff) {
        this.totalDayOff = totalDayOff;
    }

    public String getReasonForLeave() {
        return reasonForLeave;
    }

    public void setReasonForLeave(String reasonForLeave) {
        this.reasonForLeave = reasonForLeave;
    }

    public String getWorkAuthorizationFor() {
        return workAuthorizationFor;
    }

    public void setWorkAuthorizationFor(String workAuthorizationFor) {
        this.workAuthorizationFor = workAuthorizationFor;
    }

    public Double getPhone() {
        return Phone;
    }

    public void setPhone(Double phone) {
        Phone = phone;
    }
}
