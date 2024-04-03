package rocketseat.com.passin.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rocketseat.com.passin.domain.attendee.Attendee;
import rocketseat.com.passin.domain.checkin.CheckIn;
import rocketseat.com.passin.dto.attendee.AttendeeDetail;
import rocketseat.com.passin.dto.attendee.AttendeesListResponseDTO;
import rocketseat.com.passin.repositories.AttendeeRepository;
import rocketseat.com.passin.repositories.CheckinRepository;

@Service
@RequiredArgsConstructor
public class AttendeeService {
    private final AttendeeRepository attendeeRepository;
    private final CheckinRepository checkinRepository;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeesListResponseDTO getEventAttendees(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);
        List<AttendeeDetail> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<CheckIn> checkIn = this.checkinRepository.findByAttendeeId(attendee.getId());
            LocalDateTime checkInAt = checkIn.isPresent() ? checkIn.get().getCreatedAt() : null;
            return new AttendeeDetail(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkInAt);
        }).toList();

        return new AttendeesListResponseDTO(attendeeDetailsList);
    }
}
