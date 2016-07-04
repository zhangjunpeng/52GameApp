package com.view.myreport;

import java.util.List;

/**
 * Created by Administrator on 2016/6/22.
 */
public class ItemEva {
    private String name;
    private String content;
    private List<ItemEvaTable> itemEvaTables;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<ItemEvaTable> getItemEvaTables() {
        return itemEvaTables;
    }

    public void setItemEvaTables(List<ItemEvaTable> itemEvaTables) {
        this.itemEvaTables = itemEvaTables;
    }
}
