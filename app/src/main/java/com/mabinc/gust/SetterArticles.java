package com.mabinc.gust;

public class SetterArticles {

    public SetterArticles() {
    }

    private String key, accountKey, sourceKey, writerName, headline, story, tags, imageLink, sourceLink;
    private long timestamp, originalTimestamp;
    private boolean isAvailable;

    public SetterArticles(String key, String accountKey, String sourceKey, String writerName,
                          String headline, String story, String tags, String imageLink, String sourceLink,
                          long timestamp, long originalTimestamp,
                          boolean isAvailable) {
        this.key = key;
        this.accountKey = accountKey;
        this.sourceKey = sourceKey;
        this.writerName = writerName;
        this.headline = headline;
        this.story = story;
        this.tags = tags;
        this.imageLink = imageLink;
        this.sourceLink = sourceLink;
        this.timestamp = timestamp;
        this.originalTimestamp = originalTimestamp;
        this.isAvailable = isAvailable;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getOriginalTimestamp() {
        return originalTimestamp;
    }

    public void setOriginalTimestamp(long originalTimestamp) {
        this.originalTimestamp = originalTimestamp;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

}
