package com.ericsson.cifwk.tm.fun.ui.tms.models.common;

import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentMapping;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.FileSelector;
import com.ericsson.cifwk.taf.ui.sdk.GenericViewModel;

public class DialogViewModel extends GenericViewModel {

    @UiComponentMapping(".ebDialog")
    private UiComponent dialog;

    @UiComponentMapping(".ebDialog .ebDialogBox-actionBlock > .ebBtn.ebBtn_color_blue")
    private Button submitBlueButton;

    @UiComponentMapping(".ebDialog .ebDialogBox-actionBlock > .ebBtn.ebBtn_color_green")
    private Button submitButton;

    @UiComponentMapping(".ebDialog .ebDialogBox-actionBlock > .ebBtn.ebBtn_color_red")
    private Button removeButton;

    @UiComponentMapping(".ebDialog .ebDialogBox-actionBlock > .ebBtn.ebBtn_color_darkBlue")
    private Button createButton;

    @UiComponentMapping(".ebDialog .ebDialogBox-actionBlock > .ebBtn:last-child")
    private Button cancelButton;

    @UiComponentMapping(".ebDialog .eaTM-ImportTestCaseForm-input")
    private FileSelector fileSelector;

    @UiComponentMapping(".ebDialog .eaTM-ProgressBlock-messages.eaTM-ProgressBlock-messages_show")
    private UiComponent messagesBlock;

    public Button getSubmitBlueButton() {
        return submitBlueButton;
    }

    public Button getSubmitButton() {
        return submitButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public FileSelector getFileSelector() {
        return fileSelector;
    }

    public UiComponent getMessagesBlock() {
        return messagesBlock;
    }

    public UiComponent getDialog() {
        return dialog;
    }

    public Button getCreateButton() {
        return createButton;
    }
}
