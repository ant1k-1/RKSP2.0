package org.mokin.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/books")
public class FireForgetController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public FireForgetController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteBook(@PathVariable Long id) {
        return rSocketRequester
            .route("deleteBook")
            .data(id)
            .send();
    }
}
