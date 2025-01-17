<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="developer.training-session.list.label.code" path="code" width="10%"/>
	<acme:list-column code="developer.training-session.list.label.instructor" path="instructor" width="10%"/>
	<acme:list-column code="developer.training-session.list.label.start-moment" path="startMoment" width="10%"/>
	<acme:list-column code="developer.training-session.list.label.draftMode" path="draftMode" width="10%"/>
</acme:list>

<acme:button test = "${showCreate}" code="developer.training-session.list.button.create" action="/developer/training-session/create?trainingModuleId=${trainingModuleId}"/>