package com.bank;

import com.bank.utils.Timer;
import org.instancio.Instancio;
import static org.instancio.Select.*;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
public class PerformanceTest {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceTest.class);

    private final TransactionProcessor transactionProcessor;
    private final Timer timer;

    @Autowired
    public PerformanceTest(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
        timer = new Timer();
    }

    @Test
    public void oldProcessTransactions() {
        List<Transaction> transactions = Instancio.ofList(Transaction.class).size(300)
                .generate(field(Transaction::getId), gen -> gen.intSeq().start(1).asString())
                .generate(field(Transaction::getAmount), gen -> gen.doubles().range(100d, 50_000d))
                .generate(field(Transaction::getDate), gen -> gen.temporal().localDate().range(LocalDate.of(2021, 1, 1), LocalDate.now()).as(a -> a.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .generate(field(Transaction::getStatus), gen -> gen.oneOf("PENDING", "COMPLETED"))
                .create();

        timer.startTimer();

        transactionProcessor.processTransactions(transactions);

        timer.stopTimer();

        logger.info("{} {} нс", "oldProcessTransactions", timer.getTime());
    }
}
