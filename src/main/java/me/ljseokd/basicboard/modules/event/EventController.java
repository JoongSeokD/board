package me.ljseokd.basicboard.modules.event;

import lombok.RequiredArgsConstructor;
import me.ljseokd.basicboard.modules.account.Account;
import me.ljseokd.basicboard.modules.account.CurrentAccount;
import me.ljseokd.basicboard.modules.event.form.EventForm;
import me.ljseokd.basicboard.modules.event.validator.EventFormValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventRepository eventRepository;

    private final EventFormValidator eventFormValidator;

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
        eventService.newEvent(account, eventForm);
        return "redirect:/events";
    }
}
