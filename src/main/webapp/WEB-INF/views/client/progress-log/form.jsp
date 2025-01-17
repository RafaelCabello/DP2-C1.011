<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="client.progressLog.form.label.recordId" path="recordId"/>	
	<acme:input-integer code="client.progressLog.form.label.completeness" path="completeness"/>
	<acme:input-textarea code="client.progressLog.form.label.comment" path="comment"/>
	<acme:input-moment code="client.progressLog.form.label.registartionMoment" path="registrationMoment" readonly="true"/>
	<acme:input-textbox code="client.progressLog.form.label.responsible" path="responsible"/>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="client.progressLog.form.button.update" action="/client/progress-log/update"/>
			<acme:submit code="client.progressLog.form.button.delete" action="/client/progress-log/delete"/>
			<acme:submit code="client.progressLog.form.button.publish" action="/client/progress-log/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="client.progressLog.form.button.create" action="/client/progress-log/create?masterId=${masterId}"/>
		</jstl:when>		
	</jstl:choose>		

</acme:form>
