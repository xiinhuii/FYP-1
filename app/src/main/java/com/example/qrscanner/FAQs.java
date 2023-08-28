package com.example.qrscanner;

public class FAQs {

    private String FAQ_Title, description;
    private boolean expandable;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public FAQs(String FAQ_Title, String description) {
        this.FAQ_Title = FAQ_Title;
        this.description = description;
        this.expandable = false;
    }

    public String getFAQ_Title() {
        return FAQ_Title;
    }

    public void setFAQ_Title(String FAQ_Title) {
        this.FAQ_Title = FAQ_Title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FAQs{" +
                "FAQ_Title='" + FAQ_Title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
