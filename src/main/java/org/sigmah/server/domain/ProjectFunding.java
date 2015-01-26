package org.sigmah.server.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.sigmah.server.domain.base.AbstractEntityId;
import org.sigmah.server.domain.util.EntityConstants;

/**
 * <p>
 * Project funding domain entity.
 * </p>
 * <p>
 * Represent a funding link between two projects.
 * </p>
 * 
 * @author tmi
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
@Entity
@Table(name = EntityConstants.PROJECT_FUNDING_TABLE)
public class ProjectFunding extends AbstractEntityId<Integer> {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -4772203000134747144L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = EntityConstants.PROJECT_FUNDING_COLUMN_ID)
	private Integer id;

	@Column(name = EntityConstants.PROJECT_FUNDING_COLUMN_PERCENTAGE)
	private Double percentage;

	// --------------------------------------------------------------------------------
	//
	// FOREIGN KEYS.
	//
	// --------------------------------------------------------------------------------

	@ManyToOne(optional = false)
	@JoinColumn(name = EntityConstants.PROJECT_FUNDING_COLUMN_PROJECT_FUNDING, nullable = false)
	@NotNull
	private Project funding;

	@ManyToOne(optional = false)
	@JoinColumn(name = EntityConstants.PROJECT_FUNDING_COLUMN_PROJECT_FUNDED, nullable = false)
	@NotNull
	private Project funded;

	// --------------------------------------------------------------------------------
	//
	// METHODS.
	//
	// --------------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void appendToString(final ToStringBuilder builder) {
		builder.append("percentage", percentage);
	}

	// --------------------------------------------------------------------------------
	//
	// GETTERS & SETTERS.
	//
	// --------------------------------------------------------------------------------

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Project getFunding() {
		return funding;
	}

	public void setFunding(Project funding) {
		this.funding = funding;
	}

	public Project getFunded() {
		return funded;
	}

	public void setFunded(Project funded) {
		this.funded = funded;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

}
