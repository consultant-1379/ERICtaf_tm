package com.ericsson.cifwk.tm.fun.ui.tms.operator.helper.comments;

import com.beust.jcommander.internal.Lists;
import com.ericsson.cifwk.taf.ui.BrowserTab;
import com.ericsson.cifwk.taf.ui.core.UiComponent;
import com.ericsson.cifwk.taf.ui.core.UiComponentPredicates;
import com.ericsson.cifwk.tm.fun.ui.common.operator.UiCommonOperator;
import com.ericsson.cifwk.tm.presentation.dto.PostInfo;

import java.util.List;

public class CommentComponentHelper {

    public static final String ICON_SELECTOR = ".ebIcon";
    public static final String CLASS_NAME_PROPERTY = "className";
    public static final String ASSOCIATED_COMMENT_MESSAGE = ".eaTM-AssociatedCommentMessage-text";
    public static final String ACCORDION_SELECTOR = ".ebAccordion";

    private BrowserTab browserTab;

    private UiComponent commentsHolder;

    public CommentComponentHelper(BrowserTab browserTab) {
        this.browserTab = browserTab;
    }

    public UiComponent getCommentsHolder() {
        return commentsHolder;
    }

    public void setCommentsHolder(UiComponent commentsHolder) {
        this.commentsHolder = commentsHolder;
    }

    public UiComponent getRemoveButton(UiComponent commentComponent) {
        return getRemoveButtonHolder(commentComponent).getDescendantsBySelector(ICON_SELECTOR).get(0);
    }

    public boolean visibleRemoveButton(UiComponent commentComponent) {
        return !getRemoveButtonHolder(commentComponent).getDescendantsBySelector(ICON_SELECTOR).isEmpty();
    }

    public UiComponent getRemoveButtonConfirm(UiComponent commentComponent) {
        UiComponent confirmDeleteHolder = getConfirmDeleteHolder(commentComponent);
        return findRemoveButtonConfirm(confirmDeleteHolder);
    }

    public int getCommentsCount() {
        return getCommentsHolder().getDescendantsBySelector(ACCORDION_SELECTOR).size();
    }

    public PostInfo getDtoFromComponent(UiComponent commentComponent) {
        PostInfo postInfo = new PostInfo();
        postInfo.setMessage(getMessageFromComponent(commentComponent));
        return postInfo;
    }

    public List<PostInfo> getDtosFromComponents() {
        List<UiComponent> commentComponent = getCommentComponents();
        List<PostInfo> result = Lists.newArrayList();
        for (UiComponent component : commentComponent) {
            PostInfo post = getDtoFromComponent(component);
            result.add(post);
        }
        return result;
    }

    public UiComponent findCommentComponentByMessage(String message) {
        List<UiComponent> components = getCommentsHolder().getDescendantsBySelector(ACCORDION_SELECTOR);
        for (UiComponent item : components) {
            if (message.equals(getMessageFromComponent(item))) {
                return item;
            }
        }
        return null;
    }

    public UiComponent getLatestCommentComponent() {
        //Last in array! For check comments order
        int size = getCommentsHolder().getDescendantsBySelector(ACCORDION_SELECTOR).size();
        return getCommentsHolder().getDescendantsBySelector(ACCORDION_SELECTOR).get(size - 1);
    }

    public void waitForMessageShow(UiComponent commentComponent) throws Exception {
        waitForComponent(commentComponent.getDescendantsBySelector(ASSOCIATED_COMMENT_MESSAGE).get(0));
    }

    public void waitForEachMessageShow(int commentsCount) throws Exception {
        browserTab.waitUntil(
                getCommentsHolder(),
                UiComponentPredicates.CHILD_ADDED.withCurrentChildrenCount(commentsCount),
                UiCommonOperator.WAIT_TIMEOUT
        );

        List<UiComponent> components = getCommentsHolder().getDescendantsBySelector(ACCORDION_SELECTOR);
        for (UiComponent item : components) {
            waitForMessageShow(item);
        }
    }

    public void waitForDeleteConfirmShow(UiComponent commentComponent) throws Exception {
        waitForComponent(getConfirmDeleteHolder(commentComponent));
        UiComponent confirmDeleteHolder = getConfirmDeleteHolder(commentComponent);
        waitForComponent(confirmDeleteHolder.getDescendantsBySelector(ICON_SELECTOR).get(0));
    }

    private UiComponent getRemoveButtonHolder(UiComponent commentComponent) {
        return commentComponent.getDescendantsBySelector(".eaTM-AccordionTitleHolder-removeAction").get(0);
    }

    private UiComponent getConfirmDeleteHolder(UiComponent commentComponent) {
        return commentComponent.getDescendantsBySelector(".eaTM-AccordionTitleHolder-confirmAction").get(0);
    }

    private void waitForComponent(UiComponent component) {
        browserTab.waitUntilComponentIsDisplayed(component, UiCommonOperator.WAIT_TIMEOUT);
    }

    private String getMessageFromComponent(UiComponent commentComponent) {
        UiComponent text = commentComponent.getDescendantsBySelector(ASSOCIATED_COMMENT_MESSAGE).get(0);
        return text.getText();
    }

    private UiComponent findRemoveButtonConfirm(UiComponent confirmDeleteHolder) {
        List<UiComponent> confirmDeleteButtons = confirmDeleteHolder.getDescendantsBySelector(ICON_SELECTOR);

        for (UiComponent component : confirmDeleteButtons) {
            String deleteIconClass = "ebIcon_delete";
            if (component.getProperty(CLASS_NAME_PROPERTY).contains(deleteIconClass)) {
                return component;
            }
        }
        return null;
    }

    private List<UiComponent> getCommentComponents() {
        return getCommentsHolder().getDescendantsBySelector(ACCORDION_SELECTOR);
    }

}
