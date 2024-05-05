package ru.practicum.main_service.request.enums;

import java.util.Optional;

public enum RequestStatus {
    PENDING, CONFIRMED, REJECTED, CANCELED;

    public static Optional<RequestStatus> getFromString(String string) {
        for (RequestStatus state: RequestStatus.values()) {
            if (state.name().equalsIgnoreCase(string))
                return Optional.of(state);
        }
        return Optional.empty();
    }
}
