
package acme.features.manager.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.project.Project;
import acme.features.manager.user_story.ManagerUserStoryRepository;
import acme.roles.Manager;

@Service
public class ManagerProjectUpdateService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository	repository;

	@Autowired
	ManagerUserStoryRepository			mur;


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
		super.bind(object, "code", "title", "abstracto", "fatal-error", "cost", "link");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("draft-mode"))
			super.state(object.getDraftMode(), "draft-mode", "manager.project.form.error.draft-mode");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing;

			existing = this.repository.findOneProjectByCode(object.getCode());
			super.state(existing == null, "code", "manager.project.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost"))
			super.state(object.getCost().getAmount() > 0, "cost", "manager.project.form.error.negative-salary");
	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "title", "abstracto", "fatal-error", "cost", "link", "draft-mode");
		super.getResponse().addData(dataset);
	}

}