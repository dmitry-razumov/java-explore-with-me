package ru.practicum.main_service.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.request.dto.ParticipationRequestDto;

import java.util.List;

@Data
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
