package com.cmcglobal.jira.plugins.hook;


import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.screen.FieldScreen;
import com.atlassian.jira.issue.fields.screen.FieldScreenTab;
import com.atlassian.jira.project.template.hook.*;
import com.atlassian.jira.workflow.JiraWorkflow;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.cmcglobal.jira.plugins.utilities.Utilities;
import com.opensymphony.workflow.loader.ActionDescriptor;
import com.atlassian.plugin.spring.scanner.annotation.component.Scanned;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Scanned
public class MyAddProjectHook implements AddProjectHook
{
    @JiraImport
    private final CustomFieldManager customFieldManager;

    public MyAddProjectHook(CustomFieldManager customFieldManager) {
        this.customFieldManager = customFieldManager;
    }

    @Override
    public ValidateResponse validate(final ValidateData validateData)
    {
        ValidateResponse validateResponse = ValidateResponse.create();
        if (validateData.projectKey().equals("TEST"))
        {
            validateResponse.addErrorMessage("Invalid Project Key");
        }

        return validateResponse;
    }
    private void leaveScreen(Map<String, FieldScreen> createdScreens, StringBuilder listCustomFieldCreateScreen) {
        FieldScreen leaveDefaultScreen = createdScreens.get("LEAVEDEFAULTSCREEN");
        FieldScreenTab defaultTab = leaveDefaultScreen.getTab(0);
        for (String attribute : Utilities.listLeaveAttribute()) {
            CustomField customField = this.customFieldManager.getCustomFieldObjectByName(attribute);
            if (customField != null){
                defaultTab.addFieldScreenLayoutItem(customField.getId());
                listCustomFieldCreateScreen.append(customField.getId());
            }
        }
    }

    @Override
    public ConfigureResponse configure(final ConfigureData configureData)
    {
        Map<String, FieldScreen> createdScreens = configureData.createdScreens();
        StringBuilder listCustomFieldCreateScreen = new StringBuilder();
        leaveScreen(createdScreens,listCustomFieldCreateScreen );
        editWorkflow(configureData);
        return ConfigureResponse.create();
    }
    private void editWorkflow(ConfigureData configureData) {
        Map<String, FieldScreen> createdScreens = configureData.createdScreens();
        Map<String, JiraWorkflow> createdWorkflows = configureData.createdWorkflows();
        JiraWorkflow leaveWorkflow = createdWorkflows.get("WF1");
        if(leaveWorkflow !=null){
        Collection<ActionDescriptor> allLeaveActions = leaveWorkflow.getAllActions();
        Map<String, String> metaAttributes;
        FieldScreen fieldScreen;
        for (ActionDescriptor action : allLeaveActions) {
            metaAttributes = new HashMap<>();
            if (action.getName()== "Create") {
                fieldScreen = createdScreens.get("LEAVEDEFAULTSCREEN");
                metaAttributes.put("jira.fieldscreen.id", String.valueOf(fieldScreen.getId()));
                action.setView("Create");
                action.setMetaAttributes(metaAttributes);
            }
        }}
    }

}
