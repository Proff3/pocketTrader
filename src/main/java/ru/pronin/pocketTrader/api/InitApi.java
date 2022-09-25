package ru.pronin.pocketTrader.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pronin.pocketTrader.services.InitService;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class InitApi {
    private final InitService initService;

    @Autowired
    public InitApi(InitService initService) {
        this.initService = initService;
    }

    @GetMapping("/init")
    public String init() throws ExecutionException, InterruptedException {
        initService.init();
        return "Initialization is successful";
    }
}
