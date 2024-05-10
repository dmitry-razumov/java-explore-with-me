package ru.practicum.main_service.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.request.enums.RequestStatus;
import ru.practicum.main_service.request.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Integer countAllByEventIdAndStatusIs(Long eventId, RequestStatus confirmed);

    boolean existsByRequesterIdAndEventId(Long userId, Long eventId);

    Integer countAllByEventIdAndStatusNotAndIdIn(Long eventId, RequestStatus requestStatus, List<Long> requestIds);

    List<ParticipationRequest> findAllByEventIdAndStatusAndIdIn(Long eventId, RequestStatus requestStatus,
                                                              List<Long> requestIds);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long requestId, Long userId);

    List<ParticipationRequest> findAllByEventId(Long eventId);
}
