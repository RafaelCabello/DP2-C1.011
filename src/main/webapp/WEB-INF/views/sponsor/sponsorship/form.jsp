<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="sponsor.sponsorship.form.label.code" path="code" placeholder="ABC-123"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.moment" path="moment"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.start-date" path="startDate"/>
	<acme:input-moment code="sponsor.sponsorship.form.label.end-date" path="endDate"/>	
	<acme:input-money code="sponsor.sponsorship.form.label.amount" path="amount"/>
	<acme:input-select code="sponsor.sponsorship.form.label.financial" path="financial" choices="${methods}"/>
	<acme:input-select code="sponsor.sponsorship.form.label.project" path="project" choices="${projects}"/>
	<acme:input-email code="sponsor.sponsorship.form.label.email" path="email"/>
	<acme:input-url code="sponsor.sponsorship.form.label.link" path="link"/>
	
	<jstl:choose>
	
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?sponsorshipId=${id}"/>		
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="sponsor.sponsorship.form.button.invoices" action="/sponsor/invoice/list?sponsorshipId=${id}"/>		
			<acme:submit code="sponsor.sponsorship.form.button.update" action="/sponsor/sponsorship/update"/>
			<acme:submit code="sponsor.sponsorship.form.button.delete" action="/sponsor/sponsorship/delete"/>
			<acme:submit code="sponsor.sponsorship.form.button.publish" action="/sponsor/sponsorship/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="sponsor.sponsorship.form.button.create" action="/sponsor/sponsorship/create"/>
			
		</jstl:when>	
	</jstl:choose>
</acme:form>
