package com.agilesolutions.poc.rest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("stocks")
public class StockController {

    private final ChatService chatService;


    @GetMapping("/best-trend")
    public ResponseEntity<String> getBom(@RequestParam("version") String version) {
        return ResponseEntity.ok(chatService.getBestDeal(version));
    }


}
