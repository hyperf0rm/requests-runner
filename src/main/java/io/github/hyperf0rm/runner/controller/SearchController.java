package io.github.hyperf0rm.runner.controller;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.scene.control.IndexRange;
import org.fxmisc.richtext.StyleClassedTextArea;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchController {

    private List<IndexRange> matches = new ArrayList<>();
    private int currentIndex = -1;
    private final ReadOnlyIntegerWrapper matchesCount = new ReadOnlyIntegerWrapper(0);

    public void search(StyleClassedTextArea textArea, String searchString) {
        clear(textArea);

        if (searchString == null || searchString.isBlank() || textArea.getLength() == 0) {
            return;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(textArea.getText());

        while (matcher.find()) {
            matches.add(new IndexRange(matcher.start(), matcher.end()));
            textArea.setStyleClass(matcher.start(), matcher.end(), ("search-highlight"));
        }

        matchesCount.set(matches.size());

        if (!matches.isEmpty()) {
            selectMatch(textArea, 0);
        }
    }

    private void clear(StyleClassedTextArea textArea) {
        textArea.clearStyle(0, textArea.getLength());
        matches.clear();
        matchesCount.set(0);
        currentIndex = -1;
    }

    public void selectMatch(StyleClassedTextArea textArea, int index) {

        if (currentIndex >=0 && currentIndex < matches.size()) {
            IndexRange previous = matches.get(currentIndex);
            textArea.setStyleClass(previous.getStart(), previous.getEnd(), ("search-highlight"));
        }

        currentIndex = index;
        IndexRange current = matches.get(index);
        textArea.setStyleClass(current.getStart(), current.getEnd(), ("search-highlight-active"));
        textArea.moveTo(current.getStart());
        textArea.requestFollowCaret();
    }

    public void moveToNextMatch(StyleClassedTextArea textArea) {
        if (matches.isEmpty()) return;

        int nextIndex = currentIndex + 1;
        if (nextIndex >= matches.size()) {
            nextIndex = 0;
        }
        selectMatch(textArea, nextIndex);
    }

    public void moveToPreviousMatch(StyleClassedTextArea textArea) {
        if (matches.isEmpty()) return;

        int previousIndex = currentIndex - 1;
        if (previousIndex < 0) {
            previousIndex = matches.size() - 1;
        }
        selectMatch(textArea, previousIndex);
    }

    public ReadOnlyIntegerProperty matchesCountProperty() {
        return matchesCount.getReadOnlyProperty();
    }
}
