package com.example.myapplication2;

class Message {
    private final String content;
    private final String senderName;
    private final boolean sent;

    public Message(String content, String senderName, boolean sent) {
        this.content = content;
        this.senderName = senderName;
        this.sent = sent;
    }

    public String getContent() {
        return content;
    }

    public String getSenderName() {
        return senderName;
    }

    public boolean isSent() {
        return sent;
    }
}
