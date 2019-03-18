package com.cmcglobal.jira.plugins.entity;

import net.java.ao.Entity;
import net.java.ao.Preload;
import org.springframework.core.annotation.Order;

import java.util.Date;

@Preload
public interface Leave extends Entity {
    @Order(value=1)
    String getSubject();
    void setSubject(String subject);

    @Order(value=2)
    String getJobPoisition();
    void setJobPoisition(String jobPoisition);

    @Order(value=3)
    String getContractType();
    void setContractType(String contractType);

    @Order(value=4)
    String getLeaveType();
    void setLeaveType(String leaveType);

    @Order(value=5)
    Date getStartDayOff();
    void setStartDayOff(Date startDayOff);

    @Order(value=6)
    int getTotalDayOff();
    void setTotalDayOff(int totalDayOff);

    @Order(value=7)
    String getReasonForLeave();
    void setReasonForLeave(String reasonForLeave);

    @Order(value=8)
    String getWorkAuthorizationFor();
    void setWorkAuthorizationFor(String workAuthorizationFor);


    @Order(value=9)
    int getPhone();
    void setPhone(int phone);
}
