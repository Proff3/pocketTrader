package ru.pronin.pocketTrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.MoneyValue;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.SandboxService;

@Service
public class InitService {

    @Value("tinkoff.token")
    private String token;
    private final APIService apiService;

    @Autowired
    public InitService(APIService api) {
        this.apiService = api;
    }

    public void init() {
        InvestApi api = InvestApi.create(token);
        apiService.setApi(api);
        SandboxService sandboxService = api.getSandboxService();
        String accountId = sandboxService.openAccountSync();
        MoneyValue rubles = MoneyValue.newBuilder().setUnits(10_000_000).setCurrency("RUB").build();
        MoneyValue dollars = MoneyValue.newBuilder().setUnits(1_000_000).setCurrency("USD").build();
        sandboxService.payInSync(accountId, rubles);
        sandboxService.payInSync(accountId, dollars);
    }
}
