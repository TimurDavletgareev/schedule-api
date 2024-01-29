package ru.ktelabs.schedule_service.soap;

import https.www_schedule_ktelabs_ru.soap.gen.AddScheduleRequest;
import https.www_schedule_ktelabs_ru.soap.gen.AddScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.ktelabs.schedule_service.timeslot.dto.NewScheduleDto;
import ru.ktelabs.schedule_service.timeslot.dto.ScheduleResponseDto;
import ru.ktelabs.schedule_service.timeslot.service.TimeslotService;
import ru.ktelabs.schedule_service.util.Constants;

@Endpoint
@RequiredArgsConstructor
public class ScheduleEndpoint {

    private final TimeslotService timeslotService;

    @PayloadRoot(namespace = Constants.NAMESPACE_URI, localPart = "addScheduleRequest")
    @ResponsePayload
    public AddScheduleResponse setSchedule(@RequestPayload AddScheduleRequest request) {

        NewScheduleDto newScheduleDto = NewScheduleDto.builder()
                .doctorId(request.getSchedule().getDoctorId())
                .date(request.getSchedule().getDate())
                .startTime(request.getSchedule().getStartTime())
                .duration(request.getSchedule().getDuration())
                .amount(request.getSchedule().getAmount())
                .build();

        ScheduleResponseDto responseDto = timeslotService.addTimeslots(newScheduleDto);

        AddScheduleResponse response = new AddScheduleResponse();

        response.setResult(String.format("Schedule added: doctorId=%d, date=%s, added slots amount=%s",
                responseDto.getDoctorId(), responseDto.getDate(), responseDto.getAmount()));

        return response;
    }
}
