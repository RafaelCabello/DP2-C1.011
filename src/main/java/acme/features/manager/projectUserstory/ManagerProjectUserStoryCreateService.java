
package acme.features.manager.projectUserstory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.project.Project;
import acme.entities.project.ProjectUserStory;
import acme.entities.project.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectUserStoryCreateService extends AbstractService<Manager, ProjectUserStory> {

	@Autowired
	private ManagerProjectUserStoryRepository createRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	@Override
	public void load() {
		int userStoryId = super.getRequest().getData("userStoryId", int.class);
		UserStory us = this.createRepository.findUserStoryById(userStoryId);

		ProjectUserStory object = new ProjectUserStory();
		object.setUserStory(us);

		super.getBuffer().addData(object);
	}
	@Override
	public void bind(final ProjectUserStory object) {
		assert object != null;
		int projectId;
		int userStoryId;
		Project project;
		projectId = super.getRequest().getData("project", int.class);
		project = this.createRepository.findProjectById(projectId);
		userStoryId = super.getRequest().getData("userStoryId", int.class);
		UserStory us = this.createRepository.findUserStoryById(userStoryId);
		object.setUserStory(us);
		object.setProject(project);
	}
	@Override
	public void validate(final ProjectUserStory object) {
		assert object != null;
		if (!super.getBuffer().getErrors().hasErrors("project")) {
			//int masterId;
			//masterId = super.getRequest().getData("masterId", int.class);
			final Collection<UserStory> us = this.createRepository.findUserStoryByProject(object.getProject().getId());
			super.state(us.isEmpty() || !us.contains(object.getUserStory()), "project", "manager.projectUserStory.form.error.userStory");
		}
		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(object.getProject().getDraftMode(), "project", "manager.projectUserStory.form.error.project");
	}
	@Override
	public void perform(final ProjectUserStory object) {
		assert object != null;
		this.createRepository.save(object);
	}
	@Override
	public void unbind(final ProjectUserStory object) {
		assert object != null;
		int userStoryId = super.getRequest().getData("userStoryId", int.class);
		Manager manager = this.createRepository.findOneManagerByUserStoryId(userStoryId);
		Collection<Project> projects = this.createRepository.findProjectsByManagerId(manager.getId());

		SelectChoices choices = SelectChoices.from(projects, "title", object.getProject());

		Dataset dataset = super.unbind(object, "userStory");

		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);
		dataset.put("userStoryId", userStoryId);

		super.getResponse().addData(dataset);

	}

}