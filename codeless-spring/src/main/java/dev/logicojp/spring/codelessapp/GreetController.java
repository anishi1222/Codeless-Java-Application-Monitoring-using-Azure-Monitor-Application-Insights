package dev.logicojp.spring.codelessapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/greet", produces = MediaType.APPLICATION_JSON_VALUE)
public class GreetController {

    private final GreetingProvider greetingProvider;
    private final MpGreetingClient mpGreetingClient;

    public GreetController(GreetingProvider greetingProvider, MpGreetingClient mpGreetingClient) {
        this.greetingProvider = greetingProvider;
        this.mpGreetingClient = mpGreetingClient;
    }

    @GetMapping
    public GreetingMessage getDefaultMessage() {
        return createResponse("World");
    }

    @GetMapping("/{name}")
    public GreetingMessage getMessage(@PathVariable String name) {
        return createResponse(name);
    }

    @GetMapping("/greet2mp/{name}")
    public GreetingMessage getMpMessage(@PathVariable String name) {
        return createResponse(mpGreetingClient.getGreeting(name).message());
    }

    @PutMapping(path = "/greeting", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateGreeting(@RequestBody(required = false) GreetingUpdate update) {
        if (update == null || update.greeting() == null) {
            return ResponseEntity.badRequest().body(new ErrorMessage("No greeting provided"));
        }

        greetingProvider.setMessage(update.greeting());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private GreetingMessage createResponse(String who) {
        return new GreetingMessage("%s %s!".formatted(greetingProvider.getMessage(), who));
    }
}
