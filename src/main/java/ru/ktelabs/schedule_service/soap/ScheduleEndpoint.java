package ru.ktelabs.schedule_service.soap;

import lombok.RequiredArgsConstructor;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

@Endpoint
@RequiredArgsConstructor
public class ScheduleEndpoint {
/*
    private static final String NAMESPACE_URI = "https://www.java2blog.com/xml/book";

    private final TimeslotRepository timeslotRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getBookRequest")
    @ResponsePayload
    public AddScheduleResponse getCountry(@RequestPayload GetBookRequest request) {
        GetBookResponse response = new GetBookResponse();
        response.setBook(timeslotRepository.findBookById(request.getId()));
        return response;
    }*/
}
