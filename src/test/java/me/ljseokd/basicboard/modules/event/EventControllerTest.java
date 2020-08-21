package me.ljseokd.basicboard.modules.event;

import me.ljseokd.basicboard.infra.AbstractContainerBaseTest;
import me.ljseokd.basicboard.infra.MockMvcTest;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.AccountRepository;
import me.ljseokd.basicboard.modules.account.WithAccount;
import me.ljseokd.basicboard.modules.event.form.EventForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class EventControllerTest extends AbstractContainerBaseTest {

    @Autowired MockMvc mockMvc;

    @Autowired EventRepository eventRepository;
    @Autowired EventService eventService;
    @Autowired AccountRepository accountRepository;

    private Long createEvent() {
        Account ljseokd = accountRepository.findByNickname("ljseokd").get();
        LocalDateTime now = LocalDateTime.now();

        EventForm eventForm = new EventForm();
        eventForm.setTitle("title");
        eventForm.setContents("contents");
        eventForm.setLimitOfEnrollments(2);
        eventForm.setRecruitmentStartDate(now);
        eventForm.setRecruitmentEndDate(now.plusDays(1));
        eventForm.setEventsStartDate(now.plusDays(2));
        eventForm.setEventsEndDate(now.plusDays(3));
        Long newEventId = eventService.newEvent(ljseokd, eventForm);
        return newEventId;
    }

    @DisplayName("이벤트 목록")
    @Test
    void eventsList() throws Exception {
        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("eventPage"))
                .andExpect(view().name("event/list"));
    }
    @DisplayName("이벤트 등록 폼 성공")
    @Test
    @WithAccount("ljseokd")
    void new_event_success() throws Exception {
        mockMvc.perform(get("/events/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("eventForm"))
                .andExpect(view().name("event/new"))
                .andExpect(authenticated());
    }

    @DisplayName("이벤트 등록 폼 실패 (로그인 X)")
    @Test
    void new_event_fail() throws Exception {
        mockMvc.perform(get("/events/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(unauthenticated());
    }

    @DisplayName("이벤트 등록 성공")
    @Test
    @WithAccount("ljseokd")
    void create_new_event_success() throws Exception {
        String.valueOf(LocalDateTime.now().plusDays(1));
        mockMvc.perform(post("/events/new")
                .param("title", "title")
                .param("contents", "contents")
                .param("limitOfEnrollments", "100")
                .param("recruitmentStartDate", String.valueOf(LocalDateTime.now()))
                .param("recruitmentEndDate", String.valueOf(LocalDateTime.now().plusDays(1)))
                .param("eventsStartDate", String.valueOf(LocalDateTime.now().plusDays(2)))
                .param("eventsEndDate", String.valueOf(LocalDateTime.now().plusDays(3)))
                .param("eventType", "FCFS")
                .with(csrf())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/events"));

        assertEquals(1, eventRepository.findAll().size());
    }
    @WithAccount("ljseokd")
    @DisplayName("이벤트 등록 실패 잘못된 날짜 입력")
    @ParameterizedTest(name = "{0} {displayName}")
    @CsvSource({
            "'접수 마감일이 접수 시작일의 전','2020-08-22T09:30:21.568570300', '2020-08-21T09:30:21.568570300', '2020-08-21T09:30:21.568570300', '2020-08-21T09:30:21.568570300'",
            "'행사 시작일이 접수 마감일의 전','2020-08-21T09:30:21.568570300', '2020-08-22T09:30:21.568570300', '2020-08-21T09:30:21.568570300', '2020-08-21T09:30:21.568570300'",
            "'행사 마감일이 접수 마감일의 전','2020-08-20T09:30:21.568570300', '2020-08-22T09:30:21.568570300', '2020-08-23T09:30:21.568570300', '2020-08-21T09:30:21.568570300'",
            "'행사 마감일이 행사 시작일의 전','2020-08-21T09:30:21.568570300', '2020-08-22T09:30:21.568570300', '2020-08-23T09:30:21.568570300', '2020-08-22T09:30:21.568570300'"
    })
    void create_new_event_is_not_recruitmentStartDate_valid(
            String message,
            String recruitmentStartDate,
            String recruitmentEndDate,
            String eventsStartDate,
            String eventsEndDate
    ) throws Exception {
        mockMvc.perform(post("/events/new")
                .param("title", "title")
                .param("contents", "contents")
                .param("limitOfEnrollments", "100")
                .param("recruitmentStartDate", recruitmentStartDate)
                .param("recruitmentEndDate", recruitmentEndDate)
                .param("eventsStartDate", eventsStartDate)
                .param("eventsEndDate", eventsEndDate)
                .param("eventType", "FCFS")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("event/new"));

        assertEquals(0, eventRepository.findAll().size());
    }

    @DisplayName("행사 상세 화면")
    @Test
    @WithAccount("ljseokd")
    void view_event() throws Exception {
        //given
        Long newEventId = createEvent();

        //when
        mockMvc.perform(get("/events/" + newEventId +"/view"))
                .andExpect(model().attributeExists("eventViewDto"))
                .andExpect(view().name("event/view"))
                .andExpect(status().isOk());
    }

    @DisplayName("행사 상세 화면 실패 (없는 번호)")
    @Test
    void view_event_fail() throws Exception {
        assertThrows(IllegalArgumentException.class,
                ()-> mockMvc.perform(get("/events/1/view")));
    }


}