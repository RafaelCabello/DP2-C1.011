
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.audit.AuditRecord;
import acme.entities.audit.CodeAudit;
import acme.entities.project.ParticipatesIn;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.features.manager.userstory.ManagerUserStoryRepository;
import acme.roles.Manager;

@Service
public class ManagerProjectDeleteService extends AbstractService<Manager, Project> {

	@Autowired
	ManagerProjectRepository	repository;

	@Autowired
	ManagerUserStoryRepository	mur;


	//Lo que hace authorise es traer la id del projecto a publicar, si este existe y tiene manager se podra publicar
	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Project project;
		Manager manager;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findProjectById(projectId);
		manager = project == null ? null : project.getManager();
		status = project != null && project.getDraftMode() && super.getRequest().getPrincipal().hasRole(manager);

		super.getResponse().setAuthorised(status);
	}

	//Nos traemos los datos del proyecto que hemos seleccionado para publicar
	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findProjectById(id);

		super.getBuffer().addData(object);
	}
	//Coge los datos del formulario y los mete en un objeto
	@Override
	public void bind(final Project object) {
		assert object != null;
		super.bind(object, "code", "title", "abstracto", "fatalError", "cost", "link");
	}

	// Comprueba que se cumplen las condiciones para poder publicar el proyecto
	@Override
	public void validate(final Project object) {
		assert object != null;

	}

	@Override
	public void perform(final Project object) {
		assert object != null;
		int projectId;
		projectId = super.getRequest().getData("id", int.class);
		Collection<ProjectUserStory> pus;
		pus = this.repository.findProjectUserStoryByProjectId(projectId);
		Collection<ParticipatesIn> pi;
		pi = this.repository.findParticipatesInByProjectId(projectId);

		// Eliminar todos los CodeAudit relacionados con el proyecto
		Collection<CodeAudit> codeAudits = this.repository.findCodeAuditByProjectId(projectId);
		for (CodeAudit codeAudit : codeAudits) {
			// Para cada CodeAudit encontrado, eliminar todos los AuditRecord asociados
			Collection<AuditRecord> auditRecords = this.repository.findManyAuditRecordsByCodeAuditId(codeAudit.getId());
			this.repository.deleteAll(auditRecords); // Eliminar todos los AuditRecord

			// Finalmente, eliminar el CodeAudit
			this.repository.delete(codeAudit);
		}

		this.repository.deleteAll(pi);
		this.repository.deleteAll(pus);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "abstracto", "fatalError", "cost", "link", "draft-mode");
		super.getResponse().addData(dataset);
	}

}
