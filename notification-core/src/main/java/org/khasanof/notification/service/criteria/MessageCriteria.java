package org.khasanof.notification.service.criteria;

import org.khasanof.core.service.criteria.base.ICriteria;
import org.khasanof.notification.domain.enumeration.Status;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.filter.*;

import java.util.Objects;

/**
 * Criteria class for the {@link uz.devops.notification.domain.Message} entity. This class is used
 * in {@link uz.devops.notification.web.rest.MessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageCriteria implements ICriteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter id;

    private StringFilter title;

    private StringFilter body;

    private StringFilter from;

    private StringFilter to;

    private BooleanFilter deleted;

    private InstantFilter date;

    private StringFilter metaData;

    private StatusFilter status;

    private Boolean distinct;

    public MessageCriteria() {}

    public MessageCriteria(MessageCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.body = other.body == null ? null : other.body.copy();
        this.from = other.from == null ? null : other.from.copy();
        this.to = other.to == null ? null : other.to.copy();
        this.deleted = other.deleted == null ? null : other.deleted.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.metaData = other.metaData == null ? null : other.metaData.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MessageCriteria copy() {
        return new MessageCriteria(this);
    }

    public UUIDFilter getId() {
        return id;
    }

    public UUIDFilter id() {
        if (id == null) {
            id = new UUIDFilter();
        }
        return id;
    }

    public void setId(UUIDFilter id) {
        this.id = id;
    }

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getBody() {
        return body;
    }

    public StringFilter body() {
        if (body == null) {
            body = new StringFilter();
        }
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public StringFilter getFrom() {
        return from;
    }

    public StringFilter from() {
        if (from == null) {
            from = new StringFilter();
        }
        return from;
    }

    public void setFrom(StringFilter from) {
        this.from = from;
    }

    public StringFilter getTo() {
        return to;
    }

    public StringFilter to() {
        if (to == null) {
            to = new StringFilter();
        }
        return to;
    }

    public void setTo(StringFilter to) {
        this.to = to;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public BooleanFilter deleted() {
        if (deleted == null) {
            deleted = new BooleanFilter();
        }
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public InstantFilter getDate() {
        return date;
    }

    public InstantFilter date() {
        if (date == null) {
            date = new InstantFilter();
        }
        return date;
    }

    public void setDate(InstantFilter date) {
        this.date = date;
    }

    public StringFilter getMetaData() {
        return metaData;
    }

    public StringFilter metaData() {
        if (metaData == null) {
            metaData = new StringFilter();
        }
        return metaData;
    }

    public void setMetaData(StringFilter metaData) {
        this.metaData = metaData;
    }

    public StatusFilter getStatus() {
        return status;
    }

    public StatusFilter status() {
        if (status == null) {
            status = new StatusFilter();
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MessageCriteria that = (MessageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(title, that.title) &&
            Objects.equals(body, that.body) &&
            Objects.equals(from, that.from) &&
            Objects.equals(to, that.to) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(date, that.date) &&
            Objects.equals(metaData, that.metaData) &&
            Objects.equals(status, that.status) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body, from, to, deleted, date, metaData, status, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (body != null ? "body=" + body + ", " : "") +
            (from != null ? "from=" + from + ", " : "") +
            (to != null ? "to=" + to + ", " : "") +
            (deleted != null ? "deleted=" + deleted + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (metaData != null ? "metaData=" + metaData + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
