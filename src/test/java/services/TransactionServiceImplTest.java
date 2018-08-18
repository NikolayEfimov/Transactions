package services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.BasicModule;
import model.Account;
import model.Transaction;
import org.junit.Before;
import org.junit.Test;
import util.MultithreadedStressTester;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionServiceImplTest {

    private TransactionService transactionService;
    private AccountService accountService;

    private static Injector injector = Guice.createInjector(new BasicModule());

    @Before
    public void init() {
        accountService = injector.getInstance(AccountService.class);
        transactionService = injector.getInstance(TransactionService.class);
    }

    @Test
    public void totalBalanceShouldNotChange() throws InterruptedException {
        BigDecimal balanceBefore = ZERO;
        int nAccounts = 100000;
        int nThreads = 100;

        long start = System.currentTimeMillis();

        for (int i = 0; i < nAccounts; i++) {
            Account account = accountService.create(new Account(ONE, "acc" + i));
            balanceBefore = balanceBefore.add(account.balance);
        }

        test(nAccounts, new MultithreadedStressTester(nThreads));

        BigDecimal balanceAfter = accountService.accounts()
                .stream()
                .map(acc -> acc.balance)
                .reduce(ZERO, BigDecimal::add);

        long finish = System.currentTimeMillis();
        System.out.println("time: " + (finish - start));
        assertThat(balanceAfter.compareTo(balanceBefore)).isEqualTo(0);
    }

    private void test(int nAccounts, MultithreadedStressTester stressTester) throws InterruptedException {

        stressTester.stress(() -> {
            long randomFromId = current().nextLong(1, nAccounts + 1);
            long randomToId = current().nextLong(1, nAccounts + 1);
            long toId = randomToId == randomFromId
                    ? randomToId > 1 ? randomToId - 1 : randomToId + 1
                    : randomToId;

            Transaction tx = new Transaction();
            tx.fromAccountId = randomFromId;
            tx.toAccountId = toId;
            tx.amount = ONE;

            try {
                transactionService.transfer(tx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stressTester.shutdown();
    }
}