package com.cmcglobal.jira.plugins.hook;


import com.atlassian.jira.project.template.hook.*;

public class MyAddProjectHook implements AddProjectHook
{
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

    @Override
    public ConfigureResponse configure(final ConfigureData configureData)
    {
        return ConfigureResponse.create();
    }
}
