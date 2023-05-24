package com.example.chatapplication.controller;

import com.example.chatapplication.repo.OrderDetailRepository;
import com.example.chatapplication.service.read.ChartQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/chart")
@RequiredArgsConstructor
public class ChartController {

    private final ChartQueryService chartQueryService;

    @GetMapping("data")
    public ResponseEntity<?> getDataChart(@RequestParam("month") Integer month){


        return ResponseEntity.ok(chartQueryService.getDataChartCategory(month));
    }
}
