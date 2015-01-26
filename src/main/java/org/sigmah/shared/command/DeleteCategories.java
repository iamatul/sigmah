package org.sigmah.shared.command;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.command.base.AbstractCommand;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.category.CategoryElementDTO;
import org.sigmah.shared.dto.category.CategoryTypeDTO;

/**
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class DeleteCategories extends AbstractCommand<VoidResult> {

	private List<CategoryTypeDTO> categories;
	private List<CategoryElementDTO> categoryElements;

	protected DeleteCategories() {
		// Serialization.
	}

	public DeleteCategories(List<CategoryTypeDTO> categories, List<CategoryElementDTO> categoryElements) {
		this.setCategoryTypes(categories);
		this.setCategoryElements(categoryElements);
	}

	public void setCategoryTypes(List<CategoryTypeDTO> categories) {
		this.categories = categories;
	}

	public List<CategoryTypeDTO> getCategoryTypes() {
		return categories;
	}

	public void addCategoryType(CategoryTypeDTO category) {
		if (categories == null) {
			categories = new ArrayList<CategoryTypeDTO>();
		}
		this.categories.add(category);
	}

	public void removeCategoryType(CategoryTypeDTO category) {
		this.categories.remove(category);
	}

	public void setCategoryElements(List<CategoryElementDTO> categoryElements) {
		this.categoryElements = categoryElements;
	}

	public List<CategoryElementDTO> getCategoryElements() {
		return categoryElements;
	}

	public void addCategoryElement(CategoryElementDTO categoryElement) {
		if (categoryElements == null) {
			categoryElements = new ArrayList<CategoryElementDTO>();
		}
		this.categoryElements.add(categoryElement);
	}

	public void removeCategoryElement(CategoryElementDTO category) {
		this.categoryElements.remove(category);
	}
}
