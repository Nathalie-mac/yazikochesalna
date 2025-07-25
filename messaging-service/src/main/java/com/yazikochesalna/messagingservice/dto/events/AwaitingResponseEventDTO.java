package com.yazikochesalna.messagingservice.dto.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.yazikochesalna.messagingservice.dto.deserializer.AwaitingResponseEventDTODeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonDeserialize(using = AwaitingResponseEventDTODeserializer.class)
@Accessors(chain = true)
public class AwaitingResponseEventDTO extends EventDTO {
    @JsonIgnore
    private Long requestId;

}
