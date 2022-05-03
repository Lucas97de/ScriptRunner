import java.lang.Double
import com.atlassian.jira.issue.Issue
import com.atlassian.jira.event.issue.IssueEvent;
import com.atlassian.jira.component.ComponentAccessor
import com.atlassian.jira.bc.issue.IssueService
import com.atlassian.jira.issue.IssueInputParametersImpl
import com.atlassian.jira.issue.MutableIssue
import com.atlassian.jira.issue.IssueManager
import org.codehaus.groovy.runtime.StringGroovyMethods
import com.atlassian.jira.issue.util.DefaultIssueChangeHolder
import com.atlassian.jira.issue.ModifiedValue

def issueManager = ComponentAccessor.issueManager
def issue = event.issue as Issue
def CustomFieldManager = ComponentAccessor.customFieldManager
MutableIssue issueToUpdate = (MutableIssue) issue;

def CFprobability = CustomFieldManager.getCustomFieldObject(15201)
def CFimpactvalue = CustomFieldManager.getCustomFieldObject(15202)
def CFriskexposure = CustomFieldManager.getCustomFieldObject(15207)
def CFriskexposurevalue = CustomFieldManager.getCustomFieldObject(15206)

def probabilityValue = issue.getCustomFieldValue(CFprobability).toString() as Double
def impactvalueValue = issue.getCustomFieldValue(CFimpactvalue).toString().charAt(0).toString() as Double

log.warn(probabilityValue)
log.warn(impactvalueValue)

def value = probabilityValue*impactvalueValue
def CFriskexposurevalueValue = issue.getCustomFieldValue(CFriskexposurevalue)
def CFriskexposureValue = issue.getCustomFieldValue(CFriskexposure)

log.warn(value)
def changeHolder = new DefaultIssueChangeHolder()
CFriskexposurevalue.updateValue(null, issue, new ModifiedValue(CFriskexposurevalueValue, value.doubleValue()),changeHolder)
issueToUpdate.setCustomFieldValue(CFriskexposurevalue, value)



def optionsManager = ComponentAccessor.getOptionsManager()
def fieldConfig = CFriskexposure.getRelevantConfig(issue)
def currentOptions = optionsManager.getOptions(fieldConfig)


def changeHolder2 = new DefaultIssueChangeHolder()
def category

// impactvalueValue
// probabilityValue

if(impactvalueValue <= 2 && probabilityValue < 0.7){
    category = currentOptions.getOptionForValue("Low", null)
}
else if(impactvalueValue <= 2 && probabilityValue >= 0.7){
    category = currentOptions.getOptionForValue("Medium", null)
}
else if(impactvalueValue == 3 && probabilityValue < 0.4){
    category = currentOptions.getOptionForValue("Low", null)
}
else if(impactvalueValue == 3 && probabilityValue >= 0.4 && probabilityValue < 0.7){
    category = currentOptions.getOptionForValue("Medium", null)
}
else if(impactvalueValue == 3 && probabilityValue > 0.6){
    category = currentOptions.getOptionForValue("High", null)
}
else if(impactvalueValue >= 4 && probabilityValue < 0.4){
    category = currentOptions.getOptionForValue("Medium", null)
}
else if(impactvalueValue >= 4 && probabilityValue >= 0.4){
    category = currentOptions.getOptionForValue("High", null)
}

log.warn(category)

CFriskexposure.updateValue(null, issue, new ModifiedValue(CFriskexposureValue, category),changeHolder2)
issueToUpdate.setCustomFieldValue(CFriskexposurevalue, value)

