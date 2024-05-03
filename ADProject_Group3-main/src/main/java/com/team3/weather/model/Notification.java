package com.team3.weather.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int notificationId;

    @ManyToOne
    @JoinColumn(name = "recipientId")
    private AccountHolder recipient;

    private boolean isSent;
    private String remark;
    private String content;

    @ManyToOne
    @JoinColumn(name = "alertId")
    private Alert alert;

    private LocalDateTime createdDate;
    private String createdBy;
    private LocalDateTime sentTime;

    // Default constructor
    public Notification() {
    }

    // Parameterized constructor
    public Notification(AccountHolder recipient, boolean isSent, String remark, String content, Alert alert,
                        LocalDateTime createdDate, String createdBy, LocalDateTime sentTime) {
        this.recipient = recipient;
        this.isSent = isSent;
        this.remark = remark;
        this.content = content;
        this.alert = alert;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.sentTime = sentTime;
    }

    // Getters and Setters

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public AccountHolder getRecipient() {
        return recipient;
    }

    public void setRecipient(AccountHolder recipient) {
        this.recipient = recipient;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", recipient=" + recipient +
                ", isSent=" + isSent +
                ", remark='" + remark + '\'' +
                ", content='" + content + '\'' +
                ", alert=" + alert +
                ", createdDate=" + createdDate +
                ", createdBy='" + createdBy + '\'' +
                ", sentTime=" + sentTime +
                '}';
    }
}