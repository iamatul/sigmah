package org.sigmah.client.ui.view.reports;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.ui.res.icon.reports.ToolbarImages;
import org.sigmah.client.ui.widget.panel.FoldPanel;
import org.sigmah.shared.dto.report.KeyQuestionDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RichTextArea;

/**
 * Key question dialog component.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr) (v1.3)
 * @author Mehdi Benabdeslam (mehdi.benabdeslam@netapsys.fr) (v2.0)
 */
final class KeyQuestionDialog {

	private static Dialog keyQuestionDialog;

	private static Dialog getDialog() {

		if (keyQuestionDialog == null) {
			final Dialog dialog = new Dialog();
			dialog.setButtons(Dialog.OKCANCEL);
			dialog.setModal(true);

			dialog.setWidth("640px");
			dialog.setResizable(false);

			dialog.setLayout(new RowLayout(Orientation.VERTICAL));

			// Question label
			final Label questionLabel = new Label("key-question");
			questionLabel.addStyleName("project-report-key-question-label");
			dialog.add(questionLabel);

			// Text area
			final RichTextArea textArea = new RichTextArea();
			textArea.setStyleName("project-report-key-question");
			dialog.add(textArea);

			// Toolbar
			final ToolBar toolBar = new ToolBar();
			ReportsView.createRichTextToolbar(toolBar, textArea.getFormatter());
			dialog.setTopComponent(toolBar);

			// Cancel button
			dialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					dialog.hide();
				}
			});

			keyQuestionDialog = dialog;
		}
		return keyQuestionDialog;
	}

	public static Dialog getDialog(final KeyQuestionDTO keyQuestion, final RichTextArea textArea, final FoldPanel panel, final int toolButtonIndex,
			final KeyQuestionState keyQuestionState, boolean enabled) {
		final Dialog dialog = getDialog();
		dialog.setHeadingHtml(I18N.MESSAGES.reportKeyQuestionDialogTitle(Integer.toString(keyQuestion.getNumber())));

		// Question label
		final Label question = (Label) dialog.getWidget(0);
		question.setTitle(keyQuestion.getLabel());

		// Rich text editor
		final RichTextArea dialogTextArea = (RichTextArea) dialog.getWidget(1);
		dialogTextArea.setHTML(textArea.getHTML());

		final boolean wasValid = !"".equals(textArea.getText());

		// OK Button
		final Button okButton = dialog.getButtonById(Dialog.OK);

		okButton.removeAllListeners();

		if (enabled) {

			dialog.getTopComponent().enable();
			dialogTextArea.setEnabled(true);

			okButton.setVisible(true);
			okButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

				@Override
				public void componentSelected(ButtonEvent ce) {
					dialog.hide();
					textArea.setHTML(dialogTextArea.getHTML());

					final boolean isValid = !"".equals(dialogTextArea.getText());

					final ToolbarImages images = GWT.create(ToolbarImages.class);
					if (isValid) {
						panel.setToolButtonImage(toolButtonIndex, images.compasGreen());

						if (!wasValid)
							keyQuestionState.increaseValids();

					} else {
						panel.setToolButtonImage(toolButtonIndex, images.compasRed());

						if (wasValid)
							keyQuestionState.decreaseValids();
					}
				}
			});

		} else {

			okButton.setVisible(false);

			dialog.getTopComponent().disable();
			dialogTextArea.setEnabled(false);
		}

		return dialog;
	}
}
