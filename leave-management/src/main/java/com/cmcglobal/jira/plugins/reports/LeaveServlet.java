package com.cmcglobal.jira.plugins.reports;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.project.component.ProjectComponent;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchResults;
import com.atlassian.jira.jql.builder.JqlClauseBuilder;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.roles.ProjectRole;
import com.atlassian.jira.security.roles.ProjectRoleManager;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;
import com.cmcglobal.jira.plugins.dto.LeaveDTO;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LeaveServlet extends HttpServlet{
    private static final Logger log = LoggerFactory.getLogger(LeaveServlet.class);
    public Map<String, Object> data = new HashMap<>();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
//        resp.getWriter().write("UTF-8");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        ArrayList<LeaveDTO> reportLeaveDTOS = new ArrayList<>();
        Collection<Project> projects = ComponentAccessor.getProjectManager().getProjectObjects();
        try {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Date end = null;
            Date start = null;
            //Kiểm tra xem ngày nhận từ view về có null ko
            if(!startDate.isEmpty()) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                start = df.parse(startDate);
            }
            if(!endDate.isEmpty()) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                end = df.parse(endDate);
            } else {
                end= c.getTime();
            }
            for (Project project : projects) {
                PagerFilter pagerFilter = PagerFilter.getUnlimitedFilter();
                JqlClauseBuilder jqlClauseBuilder = JqlQueryBuilder.newClauseBuilder();
                SearchService searchService = ComponentAccessor
                        .getComponentOfType(SearchService.class);
                Query query = jqlClauseBuilder
                        .project(project.getId()).buildQuery();
                SearchResults searchResults = null;
                List<Issue> tempIssues;
                try {
                    searchResults = searchService.search(ComponentAccessor.getUserManager().getUserByName("admin"), query, pagerFilter);
                } catch (SearchException e) {
                    e.printStackTrace();
                }
                tempIssues = searchResults.getIssues();
                for (Issue issue:tempIssues) {
                    Date date= issue.getCreated();
                    if (date.after(start) && date.before(end)){
                        String name = issue.getIssueType().getName();
                        if (name.equalsIgnoreCase("Authorization Leave")){
                            LeaveDTO leaveDTO= new LeaveDTO();
                                leaveDTO.setContractType(  issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Contract Type")) != null ?
                                        (String) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Contract Type")): "");
                            leaveDTO.setSubject( issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Subject")) != null ?
                                    (String) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Subject")): "");
                            leaveDTO.setJobPoisition( issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Job Poisition")) != null ?
                                    (String) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Job Poisition")): "");
                            leaveDTO.setLeaveType( issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Leave Type")) != null ?
                                    (String) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Leave Type")): "");
                            leaveDTO.setStartDayOff( issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Start Day Off")) != null ?
                                    (Date) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Start Day Off")): null);
                            leaveDTO.setTotalDayOff(  issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Total Day Off") )!= null ?
                                    (Double)issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Total Day Off")): 0);
                            leaveDTO.setReasonForLeave( issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Reason For Leave")) != null ?
                                    (String) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Reason For Leave")): "");
                            leaveDTO.setWorkAuthorizationFor( issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Work Authorization For")) != null ?
                                    (String) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Work Authorization For")): null);
                            leaveDTO.setPhone( issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Phone")) != null ?
                                    (Double) issue.getCustomFieldValue(ComponentAccessor.getCustomFieldManager().getCustomFieldObjectByName("Phone")): 0);
                            reportLeaveDTOS.add(leaveDTO);}
                        }
                    }}
            data.put("listLeave", reportLeaveDTOS);
            resp.getWriter().write(new Gson().toJson(data));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (ParseException e) {
            data.put("error", e.getMessage());
            data.put("message", e.getStackTrace().toString() + e.getMessage() + e.toString() );
            e.printStackTrace();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(new Gson().toJson(data));
            resp.setStatus(HttpServletResponse.SC_OK);
        }



    }

}