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

    private List<MatchLocation> matches = new ArrayList<>();
    private int currentIndex = -1;
    private final ReadOnlyIntegerWrapper matchesCount = new ReadOnlyIntegerWrapper(0);
    private record MatchLocation(StyleClassedTextArea area, IndexRange range) {}

    public void search(List<StyleClassedTextArea> areas, String searchString) {
        clear(areas);

        if (searchString == null || searchString.isBlank()) {
            return;
        }

        Pattern pattern = Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE);

        for (StyleClassedTextArea area : areas) {
            Matcher matcher = pattern.matcher(area.getText());
            while (matcher.find()) {
                matches.add(new MatchLocation(area, new IndexRange(matcher.start(), matcher.end())));
                area.setStyleClass(matcher.start(), matcher.end(), ("search-highlight"));
            }
        }

        matchesCount.set(matches.size());

        if (!matches.isEmpty()) {
            selectMatch(0);
        }
    }

    private void clear(List<StyleClassedTextArea> areas) {
        for (StyleClassedTextArea area : areas) {
            area.clearStyle(0, area.getLength());
        }
        matches.clear();
        matchesCount.set(0);
        currentIndex = -1;
    }

    public void selectMatch(int index) {

        if (currentIndex >=0 && currentIndex < matches.size()) {
            MatchLocation previous = matches.get(currentIndex);
            previous.area().setStyleClass(previous.range().getStart(), previous.range().getEnd(), ("search-highlight"));
        }

        currentIndex = index;
        MatchLocation current = matches.get(index);
        current.area().setStyleClass(current.range().getStart(), current.range().getEnd(), ("search-highlight-active"));
        current.area().moveTo(current.range().getStart());
        current.area().requestFollowCaret();
    }

    public void moveToNextMatch() {
        if (matches.isEmpty()) return;

        int nextIndex = currentIndex + 1;
        if (nextIndex >= matches.size()) {
            nextIndex = 0;
        }
        selectMatch(nextIndex);
    }

    public void moveToPreviousMatch() {
        if (matches.isEmpty()) return;

        int previousIndex = currentIndex - 1;
        if (previousIndex < 0) {
            previousIndex = matches.size() - 1;
        }
        selectMatch(previousIndex);
    }

    public ReadOnlyIntegerProperty matchesCountProperty() {
        return matchesCount.getReadOnlyProperty();
    }
}
