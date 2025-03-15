package org.khasanof.notification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.khasanof.core.service.dto.base.IDto;
import org.khasanof.notification.domain.enumeration.Status;

import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link uz.devops.notification.domain.Message} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MessageDTO implements IDto {

    private Long id;

    /**
     * Message title
     */
    @Schema(description = "Message title")
    @Size(max = 128)
    private String title;

    /**
     * Message body
     */
    @Schema(description = "Message body")
    @Size(max = 512)
    @NotBlank
    private String body;

    /**
     * Sender
     */
    @Schema(description = "Sender")
    @Size(max = 128)
    @NotBlank
    private String from;

    /**
     * Recipient
     */
    @Schema(description = "Recipient")
    @Size(max = 128)
    @NotBlank
    private String to;

    /**
     * Soft delete
     */
    @Schema(description = "Soft delete")
    private Boolean deleted;

    /**
     * Date of send message
     */
    @Schema(description = "Date of send message")
    private Instant date;

    /**
     * Additional information
     */
    @Schema(description = "Additional information")
    @Size(max = 512)
    @NotBlank
    private String metaData;

    /**
     * Message status
     */
    @NotNull
    @Schema(description = "Message status", required = true)
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }

        MessageDTO messageDTO = (MessageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, messageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MessageDTO{" +
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
