
package acme.features.auditor.audit_record;

import java.sql.Date;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.audit.AuditRecord;
import acme.entities.audit.CodeAudit;
import acme.entities.audit.Mark;
import acme.features.auditor.code_audit.AuditorCodeAuditRepository;
import acme.roles.Auditor;

@Service
public class AuditorAuditRecordCreateService extends AbstractService<Auditor, AuditRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuditorAuditRecordRepository	auditorAuditRecordRepository;

	@Autowired
	private AuditorCodeAuditRepository		auditorCodeAuditRepository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AuditRecord object;
		CodeAudit codeAudit;
		int codeAuditId;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);
		codeAudit = this.auditorCodeAuditRepository.findOneCodeAuditById(codeAuditId);

		object = new AuditRecord();
		object.setCodeAudit(codeAudit);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final AuditRecord object) {
		assert object != null;

		CodeAudit codeAudit;
		codeAudit = object.getCodeAudit();
		super.bind(object, "code", "periodStart", "periodEnd", "mark", "optionalLink");
		object.setCodeAudit(codeAudit);
	}

	@Override
	public void validate(final AuditRecord object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("periodEnd")) {
			long diffInMili;
			long diffInHour;

			super.state(object.getPeriodEnd().after(Date.valueOf("2000-1-1")) || object.getPeriodEnd().equals(Date.valueOf("2000-1-1")), "periodEnd", "auditor.code-audit.error.executionDate");

			if (object.getPeriodStart() != null) {
				diffInMili = object.getPeriodEnd().getTime() - object.getPeriodStart().getTime();
				diffInHour = TimeUnit.MILLISECONDS.toHours(diffInMili);
				super.state(diffInHour >= 1, "periodEnd", "auditor.audit-record.error.duration");
				super.state(object.getPeriodStart() != null || object.getPeriodStart().before(object.getPeriodEnd()), "periodEnd", "auditor.audit-record.error.consecutiveDates");
			}
		}

		if (!super.getBuffer().getErrors().hasErrors("periodBeginning"))
			super.state(object.getPeriodStart().after(Date.valueOf("2000-1-1")) || object.getPeriodStart().equals(Date.valueOf("2000-1-1")), "periodBeginning", "auditor.code-audit.error.executionDate");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			AuditRecord existing;

			existing = this.auditorAuditRecordRepository.findOneAuditRecordByCode(object.getCode());
			super.state(existing == null, "code", "auditor.audit-record.error.code");
		}
	}

	@Override
	public void perform(final AuditRecord object) {
		assert object != null;
		this.auditorAuditRecordRepository.save(object);
	}

	@Override
	public void unbind(final AuditRecord object) {
		assert object != null;

		SelectChoices choices;
		Dataset dataset;

		CodeAudit codeAudit;
		codeAudit = object.getCodeAudit();

		choices = SelectChoices.from(Mark.class, object.getMark());
		dataset = super.unbind(object, "code", "periodStart", "periodEnd", "optionalLink", "draftMode");
		dataset.put("mark", choices);
		dataset.put("codeAudit", codeAudit);
		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<AuditRecord> objects) {
		assert objects != null;

		int codeAuditId;

		codeAuditId = super.getRequest().getData("codeAuditId", int.class);

		super.getResponse().addGlobal("codeAuditId", codeAuditId);
	}
}
