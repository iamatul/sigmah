package org.sigmah.shared.command.result;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.IndicatorGroup;

/**
 * IndicatorListResult.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class IndicatorListResult extends ListResult<IndicatorDTO> {

	private List<IndicatorGroup> groups = new ArrayList<IndicatorGroup>();
	private List<IndicatorDTO> ungroupedIndicators = new ArrayList<IndicatorDTO>();

	public IndicatorListResult() {
		super(); // Serialization.
	}

	public IndicatorListResult(List<IndicatorDTO> data) {
		super(data);
	}

	public List<IndicatorGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<IndicatorGroup> groups) {
		this.groups = groups;
	}

	public List<IndicatorDTO> getUngroupedIndicators() {
		return ungroupedIndicators;
	}

	public void setUngroupedIndicators(List<IndicatorDTO> ungroupedIndicators) {
		this.ungroupedIndicators = ungroupedIndicators;
	}

	public IndicatorDTO getIndicatorById(int id) {
		for (final IndicatorDTO indicator : getList()) {
			if (indicator.getId().equals(id)) {
				return indicator;
			}
		}
		throw new IllegalArgumentException();
	}

}
