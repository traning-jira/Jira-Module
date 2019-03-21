package com.cmcglobal.jira.plugins.jira.reports;

import com.atlassian.jira.plugin.report.impl.AbstractReport;
import com.atlassian.jira.web.action.ProjectActionSupport;

import java.util.HashMap;
import java.util.Map;

public class LeaveReport extends AbstractReport {
    public Map<String, Object> data = new HashMap<>();
    public String generateReportHtml(ProjectActionSupport projectActionSupport, Map map) throws Exception {

            return descriptor.getHtml("view", data);
        }


    @Override
    public boolean isExcelViewSupported() {
        return true;
    }
    @Override
    public String generateReportExcel(ProjectActionSupport action,
                                      Map reqParams) throws Exception {

        return descriptor.getHtml("excel", data);
    }
}
