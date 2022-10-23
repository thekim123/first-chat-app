package com.thekim12.chatapp;


import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRepository chatRepository;

    // 귓속말 할 때 사용하면 돼요!!
    @CrossOrigin
    @GetMapping(value = "/sender/{sender}/receiver/{receiver}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getMsg(@PathVariable String sender, @PathVariable String receiver) {
        return chatRepository.mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> setMsg(@RequestBody Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }

    @CrossOrigin
    @GetMapping(value = "chat/roomNum/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> findByRoomName(@PathVariable Long roomNum) {
        return chatRepository.mFindByRoomNum(roomNum).subscribeOn(Schedulers.boundedElastic());
    }
}
