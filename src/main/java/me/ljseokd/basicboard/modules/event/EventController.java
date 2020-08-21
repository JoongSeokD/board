package me.ljseokd.basicboard.modules.event;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.CurrentAccount;
import me.ljseokd.basicboard.modules.event.dto.EventViewDto;
import me.ljseokd.basicboard.modules.event.form.EventForm;
import me.ljseokd.basicboard.modules.event.validator.EventFormValidator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;
    private final EventFormValidator eventFormValidator;
    private final ModelMapper modelMapper;

    @InitBinder("eventForm")
    public void initBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(eventFormValidator);
    }

    @GetMapping("/events")
    public String events(@PageableDefault Pageable pageable, Model model){
        model.addAttribute("eventPage", eventRepository.page(pageable));
        return "event/list";
    }

    @GetMapping("/events/new")
    public String newEventsForm(@CurrentAccount Account account,
                                Model model){
        model.addAttribute(new EventForm());
        return "event/new";
    }

    @PostMapping("/events/new")
    public String newEventsSubmit(@CurrentAccount Account account,
                                  @Valid @ModelAttribute EventForm eventForm,
                                  Errors errors,Model model){

        if (errors.hasErrors()){
            model.addAttribute(eventForm);
            return "event/new";
        }
        Long newEventId = eventService.newEvent(account, eventForm);
        return "redirect:/events/" + newEventId + "/view";
    }

    @GetMapping("/events/{eventId}/view")
    public String viewEvent(@PathVariable Long eventId, Model model,
                            @CurrentAccount Account account){

        Event event = eventRepository.findByAccountWithId(eventId)
                .orElseThrow(() -> new IllegalArgumentException("/events/" + eventId + "/view 는 없는 행사 번호입니다."));

        model.addAttribute("eventViewDto", modelMapper.map(event, EventViewDto.class));
        model.addAttribute("isEnrollmentValidate", isEnrollmentValidate(account, event));
        model.addAttribute("isOwner", isOwner(account, event));

        return "event/view";
    }

    // 행사 작성자가 아니고, 접수 마감 전인지 검증
    private boolean isEnrollmentValidate(Account account, Event event) {
        return !isOwner(account, event) && !LocalDateTime.now().isBefore(event.getRecruitmentEndDate());
    }

    private boolean isOwner(Account account, Event event) {
        return event.getCreatedBy().isOwner(account);
    }

}
