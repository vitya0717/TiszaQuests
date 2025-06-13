package org.vitya0717.tiszaQuests.quest.inventory.questspage;

import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;

import java.util.*;

public class QuestsPage {

    private final Main instance;
    private final List<Quest> pageContents = new ArrayList<>();
    private Integer pageNumber;
    private Integer minPageContents;
    private Integer maxPageContents;

    public QuestsPage(Main instance) {
        this.instance=instance;
    }

    public Main getInstance() {
        return instance;
    }

    public List<Quest> getPageContents() {
        return pageContents;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getMinPageContents() {
        return minPageContents;
    }

    public void setMinPageContents(Integer minPageContents) {
        this.minPageContents = minPageContents;
    }

    public Integer getMaxPageContents() {
        return maxPageContents;
    }

    public void setMaxPageContents(Integer maxPageContents) {
        this.maxPageContents = maxPageContents;
    }

    @Override
    public String toString() {
        return "QuestsPage{" +
                "instance=" + instance +
                ", pageContents=" + pageContents +
                ", pageNumber=" + pageNumber +
                ", minPageContents=" + minPageContents +
                ", maxPageContents=" + maxPageContents +
                '}';
    }
}
