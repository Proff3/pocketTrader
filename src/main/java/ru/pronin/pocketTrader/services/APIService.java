package ru.pronin.pocketTrader.services;

import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.SandboxService;

@Service
public class APIService {

    private InvestApi api;
    private SandboxService sandboxService;

    public InvestApi getApi() {
        return api;
    }

    public void setApi(InvestApi api) {
        if (api == null) {
            this.api = api;
        }
    }

    public SandboxService getSandboxService() {
        return sandboxService;
    }

    public void setSandboxService(SandboxService sandboxService) {
        if (sandboxService == null) {
            this.sandboxService = sandboxService;
        }
    }
}
