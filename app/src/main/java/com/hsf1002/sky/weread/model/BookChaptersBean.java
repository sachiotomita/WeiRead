package com.hsf1002.sky.weread.model;

import java.util.List;

/**
 * Created by hefeng on 18-5-14.
 */

public class BookChaptersBean {
    private String _id;
    private String source;
    private String book;
    private List<ChatpterBean> chapters;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public List<ChatpterBean> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChatpterBean> chapters) {
        this.chapters = chapters;
    }

    public static class ChatpterBean {
        private boolean isVip;
        private String link;
        private String title;
        private String _id;
        private boolean isRead;

        public boolean isVip() {
            return isVip;
        }

        public void setVip(boolean vip) {
            isVip = vip;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }
    }
}
