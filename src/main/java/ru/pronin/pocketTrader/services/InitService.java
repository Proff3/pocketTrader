package ru.pronin.pocketTrader.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.pronin.pocketTrader.services.strategy.processor.StrategyProcessor;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.SandboxService;
import ru.tinkoff.piapi.core.stream.StreamProcessor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class InitService {

    @Value("${tinkoff.token.sandbox}")
    private String token;
    private final APIService apiService;
    private final StrategyProcessor strategyProcessor;

    @Autowired
    public InitService(APIService api, StrategyProcessor strategyProcessor) {
        this.apiService = api;
        this.strategyProcessor = strategyProcessor;
    }

    public void init() throws ExecutionException, InterruptedException {
        System.out.println("token " + token);
        InvestApi api = InvestApi.createSandbox(token);
        apiService.setApi(api);
        SandboxService sandboxService = api.getSandboxService();
//        sandboxService.openAccount().thenApply(setBalance(sandboxService)).get();

        List<Share> shares = api.getInstrumentsService().getTradableSharesSync();
        shares.stream().filter(Share::getApiTradeAvailableFlag).forEach(System.out::println);

        HistoricCandle historicCandle = api.getMarketDataService().getCandlesSync(
                "BBG012YQ6P43",
                Instant.now().minus(5L, ChronoUnit.DAYS),
                Instant.now(),
                CandleInterval.CANDLE_INTERVAL_DAY
        ).get(0);
        StreamProcessor<MarketDataResponse> processor = response -> {
            if (response.hasCandle()) {
                System.out.println("Новые данные по свече");
                strategyProcessor.process(response.getCandle());
            } else if (response.hasOrderbook()) {
                System.out.println("Новые данные по стакану: " + response);
            }
        };
        Consumer<Throwable> onErrorCallback = error -> System.out.println(error.toString());
        api.getMarketDataStreamService().newStream("candles_stream", processor, onErrorCallback)
                .subscribeCandles(List.of("BBG00ZNSSKP3", "BBG000F0R4N9", "BBG002WMH2F2", "BBG000BQVTF5", "BBG000BWRTS3"));
    }

    private Function<String, String> setBalance(SandboxService sandboxService) {
        return (accountId) -> {
            MoneyValue rubles = MoneyValue.newBuilder().setUnits(10_000_000).setCurrency("RUB").build();
            MoneyValue dollars = MoneyValue.newBuilder().setUnits(1_000_000).setCurrency("USD").build();
            sandboxService.payInSync(accountId, rubles);
            sandboxService.payInSync(accountId, dollars);
            return accountId;
        };
    }
}
