package com.cmcglobal.jira.plugins.api;

import com.atlassian.jira.avatar.AvatarManager;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.ModifiedValue;
import com.atlassian.jira.issue.context.GlobalIssueContext;
import com.atlassian.jira.issue.context.JiraContextNode;
import com.atlassian.jira.issue.customfields.CustomFieldSearcher;
import com.atlassian.jira.issue.customfields.CustomFieldType;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.customfields.option.Option;
import com.atlassian.jira.issue.customfields.option.Options;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.cmcglobal.jira.plugins.utilities.Utilities;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class JiraCustomPluginListener implements InitializingBean, DisposableBean {
    @JiraImport
    private final CustomFieldManager customFieldManager;
    @JiraImport
    private IssueTypeManager issueTypeManager;
    @JiraImport
    private final AvatarManager avatarManager;

    private static List<CustomField> customFields = new ArrayList<>();

    @Autowired
    public JiraCustomPluginListener(CustomFieldManager customFieldManager, IssueTypeManager issueTypeManager, AvatarManager avatarManager) {
        this.customFieldManager = customFieldManager;
        this.issueTypeManager = issueTypeManager;
        this.avatarManager = avatarManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        IssueType issueTypeLeave = getIssueType("Authorization Leave", "The Authorization Leave Description ",
                10320L);
        List<JiraContextNode> contexts = new ArrayList<>();
        contexts.add(GlobalIssueContext.getInstance());
        CustomFieldType fieldTypeText = this.customFieldManager.getCustomFieldType(
                "com.atlassian.jira.plugin.system.customfieldtypes:textfield");
        CustomFieldType fieldTypeSelect = this.customFieldManager.getCustomFieldType(
                "com.atlassian.jira.plugin.system.customfieldtypes:select");
        CustomFieldType fieldTypeTextarea = this.customFieldManager.getCustomFieldType(
                "com.atlassian.jira.plugin.system.customfieldtypes:textarea");
        CustomFieldType fieldTypeDate = this.customFieldManager.getCustomFieldType(
                "com.atlassian.jira.plugin.system.customfieldtypes:datepicker");
        CustomFieldType fieldTypeMultiSelect = this.customFieldManager.getCustomFieldType(
                "com.atlassian.jira.plugin.system.customfieldtypes:multiselect");
        CustomFieldType fieldTypeNumber = this.customFieldManager.getCustomFieldType(
                "com.atlassian.jira.plugin.system.customfieldtypes:float");
        CustomFieldSearcher fieldTypeTextSearcher = this.customFieldManager.getCustomFieldSearcher(
                "com.atlassian.jira.plugin.system.customfieldtypes:textsearcher");
        CustomFieldSearcher fieldTypeDateSearcher = this.customFieldManager.getCustomFieldSearcher(
                "com.atlassian.jira.plugin.system.customfieldtypes:daterange");
        CustomFieldSearcher fieldTypeMultiSelectSearcher = this.customFieldManager.getCustomFieldSearcher(
                "com.atlassian.jira.plugin.system.customfieldtypes:multiselectsearcher");
        CustomFieldSearcher fieldTypeNumberSearcher = this.customFieldManager.getCustomFieldSearcher(
                "com.atlassian.jira.plugin.system.customfieldtypes:exactnumber");
        for (String attribute : Utilities.listLeaveAttribute()) {
            CustomField customField;
            if (this.customFieldManager.getCustomFieldObjectsByName(attribute).isEmpty()) {
                if (attribute.equalsIgnoreCase("Total Day Off") || attribute.equalsIgnoreCase("Phone") ){
                    customField = this.customFieldManager.createCustomField(attribute, null, fieldTypeNumber, fieldTypeNumberSearcher,
                            contexts, Collections.singletonList(issueTypeLeave));}
                else if (attribute.equalsIgnoreCase("Contract Type") || attribute.equalsIgnoreCase("Leave Type") ){
                    customField = this.customFieldManager.createCustomField(attribute, null, fieldTypeSelect, fieldTypeMultiSelectSearcher,
                            contexts, Collections.singletonList(issueTypeLeave));}
                else if (attribute.equalsIgnoreCase("Start Day Off")){
                    customField = this.customFieldManager.createCustomField(attribute, null, fieldTypeDate, fieldTypeDateSearcher,
                            contexts, Collections.singletonList(issueTypeLeave));}
                else if (attribute.equalsIgnoreCase("Reason For Leave")){
                    customField = this.customFieldManager.createCustomField(attribute, null, fieldTypeTextarea, fieldTypeTextSearcher,
                            contexts, Collections.singletonList(issueTypeLeave));}

                else {customField = this.customFieldManager.createCustomField(attribute, null, fieldTypeText,
                        fieldTypeTextSearcher, contexts,
                        Collections.singletonList(issueTypeLeave));}
                customFields.add(customField);

//                for (CustomField cusoption: customFields) {
//                    OptionsManager optManager = ComponentAccessor.getOptionsManager();
//                    Options options = optManager.getOptions(customField.getRelevantConfig(issue), null));
//
//                    Option newOption = options.getOptionById("high option"); // Check your option id
//
//                    ModifiedValue mVal = new ModifiedValue(issue.getCustomFieldValue(customField), newOption );
//
//                    customField.updateValue(null, issue, mVal, new DefaultIssueChangeHolder());
//                }

            }

        }
    }
    public IssueType getIssueType(String issueTypeName, String issueTypeDescription, Long avatarId) {
        IssueType issueType = null;
        boolean issueTypeExist = false;
        Collection<IssueType> listIssueType = this.issueTypeManager.getIssueTypes();
        for (IssueType issue : listIssueType) {
            if (issue.getName().equals(issueTypeName)) {
                issueType = issue;
                issueTypeExist = true;
            }
        }
        if (!issueTypeExist) {
            issueType = this.issueTypeManager.createIssueType(issueTypeName, issueTypeDescription,
                    this.avatarManager.getById(avatarId) != null ?
                            avatarId : 0L);
        }

        return issueType;
    }


    @Override
    public void destroy() throws Exception {

    }
}
