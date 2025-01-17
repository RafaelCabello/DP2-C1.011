<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:message code="developer.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.num-tm-updated"/>
		</th>
		<td>
			<acme:print value="${numTrainingModulesUpdated}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.num-ts-link"/>
		</th>
		<td>
			<acme:print value="${numTrainingSessionsLink}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.avg-tm-time"/>
		</th>
		<td>
			<acme:print value="${avgTrainingModulesTime}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.dev-tm-time"/>
		</th>
		<td>
			<acme:print value="${devTrainingModulesTime}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.min-tm-time"/>
		</th>
		<td>
			<acme:print value="${minTrainingModulesTime}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:message code="developer.dashboard.form.label.max-tm-time"/>
		</th>
		<td>
			<acme:print value="${maxTrainingModulesTime}"/>
		</td>
	</tr>
</table>
<acme:return/>