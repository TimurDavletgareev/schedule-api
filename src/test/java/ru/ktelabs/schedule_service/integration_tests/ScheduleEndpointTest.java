package ru.ktelabs.schedule_service.integration_tests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.AutoConfigureMockWebServiceClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;
import ru.ktelabs.schedule_service.doctor.dto.NewDoctorDto;
import ru.ktelabs.schedule_service.doctor.service.DoctorService;
import ru.ktelabs.schedule_service.timeslot.model.Timeslot;
import ru.ktelabs.schedule_service.timeslot.repository.TimeslotRepository;
import ru.ktelabs.schedule_service.util.Constants;
import ru.ktelabs.schedule_service.util.CustomFormatter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.ws.test.server.RequestCreators.withPayload;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

@ActiveProfiles("test-h2")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockWebServiceClient
public class ScheduleEndpointTest {

    private final MockWebServiceClient client;
    private final DoctorService doctorService;
    private final TimeslotRepository timeslotRepository;
    private static Long doctorId;
    private static final String DATE_STRING = CustomFormatter.dateToString(LocalDate.now().plusDays(10));
    private static final String START_TIME_STRING = "10:00";
    private static final int DURATION = 60;
    private static final int AMOUNT = 5;

    @BeforeEach
    void setUp() {

        NewDoctorDto properNewDoctorDto = NewDoctorDto.builder()
                .firstName("ProperFirstName")
                .secondName("ProperSecondName")
                .lastName("ProperLastName")
                .build();

        doctorId = doctorService.addDoctor(properNewDoctorDto).getId();
    }

    @Test
    void shouldAddTimeslotsWithXmlRequest() {

        StringSource request = new StringSource(
                "<bd:addScheduleRequest xmlns:bd='" + Constants.NAMESPACE_URI + "'>" +
                        "<bd:Schedule>" +
                        "<bd:doctorId>" + doctorId + "</bd:doctorId>" +
                        "<bd:date>" + DATE_STRING + "</bd:date>" +
                        "<bd:startTime>" + START_TIME_STRING + "</bd:startTime>" +
                        "<bd:duration>" + DURATION + "</bd:duration>" +
                        "<bd:amount>" + AMOUNT + "</bd:amount>" +
                        "</bd:Schedule>" +
                        "</bd:addScheduleRequest>"
        );

        System.out.println("\n" + request + "\n");

        StringSource expectedResponse = new StringSource(
                "<bd:addScheduleResponse xmlns:bd='" + Constants.NAMESPACE_URI + "'>" +
                        "<bd:result>" +
                        "Schedule added: " +
                        "doctorId=" + doctorId +
                        ", date=" + DATE_STRING +
                        ", added slots amount=" + AMOUNT +
                        "</bd:result>" +
                        "</bd:addScheduleResponse>"
        );

        System.out.println("\n" + request + "\n");

        client.sendRequest(withPayload(request))
                .andExpect(noFault())
                .andExpect(payload(expectedResponse));

        List<Timeslot> foundSlots = timeslotRepository.findAll();

        foundSlots.sort(Comparator.comparing(Timeslot::getId));

        LocalTime inputStartTime = CustomFormatter.stringToTime(START_TIME_STRING);
        assert inputStartTime != null;

        assertEquals(AMOUNT, foundSlots.size());
        assertEquals(CustomFormatter.stringToDate(DATE_STRING), foundSlots.get(0).getDate());
        assertEquals(inputStartTime, foundSlots.get(0).getStartTime());
        assertEquals(inputStartTime.plusMinutes(DURATION * (AMOUNT - 1)),
                foundSlots.get(foundSlots.size() - 1).getStartTime());
    }
}
