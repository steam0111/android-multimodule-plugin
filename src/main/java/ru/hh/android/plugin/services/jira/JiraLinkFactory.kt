package ru.hh.android.plugin.services.jira

import com.atlassian.jira.rest.client.api.domain.input.LinkIssuesInput
import com.intellij.openapi.components.Service
import ru.hh.android.plugin.core.model.jira.JiraLinkType

@Service
class JiraLinkFactory {

    fun issueConsistsInAnotherIssue(consistingIssueKey: String, includingIssueKey: String): LinkIssuesInput {
        return LinkIssuesInput(
            includingIssueKey,
            consistingIssueKey,
            JiraLinkType.INCLUSION.remoteName,
            null
        )
    }

}