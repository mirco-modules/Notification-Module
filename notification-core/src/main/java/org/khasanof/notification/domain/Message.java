package org.khasanof.notification.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.khasanof.core.domain.AbstractAuditingEntity;
import org.khasanof.notification.domain.enumeration.Status;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Message extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    /**
     * Message title
     */
    @Column(name = "title")
    private String title;

    /**
     * Message body
     */
    @Column(name = "body")
    private String body;

    /**
     * Sender
     */
    @Column(name = "jhi_from")
    private String from;

    /**
     * Recipient
     */
    @Column(name = "jhi_to")
    private String to;

    /**
     * Soft delete
     */
    @Column(name = "deleted")
    private Boolean deleted;

    /**
     * Date of send message
     */
    @Column(name = "date")
    private Instant date;

    /**
     * Additional information
     */
    @Column(name = "meta_data")
    private String metaData;

    /**
     * Message status
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Message id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Message title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public Message body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return this.from;
    }

    public Message from(String from) {
        this.setFrom(from);
        return this;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public Message to(String to) {
        this.setTo(to);
        return this;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Message deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDate() {
        return this.date;
    }

    public Message date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getMetaData() {
        return this.metaData;
    }

    public Message metaData(String metaData) {
        this.setMetaData(metaData);
        return this;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public Status getStatus() {
        return this.status;
    }

    public Message status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message)) {
            return false;
        }
        return id != null && id.equals(((Message) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Message{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", date='" + getDate() + "'" +
            ", metaData='" + getMetaData() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
