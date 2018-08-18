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

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.assertj.core.api.Assertions.assertThat;

public class TransactionServiceMultithreadTest {

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
        int nAccounts = 10000;
        int nTransfers = 100;

        createNAccounts(nAccounts);
        BigDecimal balanceBefore = totalBalanceOfAllAccounts();

        makeRandomTransactionsBetweenAccounts(nAccounts, nTransfers);

        BigDecimal balanceAfter = totalBalanceOfAllAccounts();

        assertThat(balanceAfter.compareTo(balanceBefore)).isEqualTo(0);
    }

    @Test
    public void totalBalanceAfterBiDirectionalTransfersBetweenShouldNotChange() throws InterruptedException {
        int nAccounts = 2;
        int nTransfers = 100;

        createNAccounts(nAccounts);
        BigDecimal balanceBefore = totalBalanceOfAllAccounts();

        makeRandomTransactionsBetweenTwoAccounts(1, 2, ONE, nTransfers);

        BigDecimal balanceAfter = totalBalanceOfAllAccounts();

        assertThat(balanceAfter.compareTo(balanceBefore)).isEqualTo(0);
    }

    @Test
    public void successfulTransferBetweenTwoAccounts() {
        Account remitter = new Account(new BigDecimal(10), "remitter");
        Account beneficiary = new Account(new BigDecimal(5), "beneficiary");

        remitter = accountService.create(remitter);
        beneficiary = accountService.create(beneficiary);

        Transaction tx = new Transaction();
        tx.fromAccountId = remitter.id;
        tx.toAccountId = beneficiary.id;
        tx.amount = new BigDecimal(10);

        transactionService.transfer(tx);

        remitter = accountService.accountById(remitter.id);
        beneficiary = accountService.accountById(beneficiary.id);

        assertThat(beneficiary.balance.compareTo(BigDecimal.valueOf(15))).isEqualTo(0);
        assertThat(remitter.balance.compareTo(ZERO)).isEqualTo(0);
    }

    private void createNAccounts(int nAccounts) {
        for (int i = 0; i < nAccounts; i++) {
            accountService.create(new Account(ONE, "account_" + i));
        }
    }

    private BigDecimal totalBalanceOfAllAccounts() {
        return accountService.accounts()
                .stream()
                .map(acc -> acc.balance)
                .reduce(ZERO, BigDecimal::add);
    }

    private void makeRandomTransactionsBetweenAccounts(int nAccounts, int nTransactions) throws InterruptedException {

        MultithreadedStressTester stressTester = new MultithreadedStressTester(nTransactions);
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

    private void makeRandomTransactionsBetweenTwoAccounts(long first, long second, BigDecimal amount, int nTransactions) throws InterruptedException {

        MultithreadedStressTester stressTester = new MultithreadedStressTester(nTransactions);

        stressTester.stress(() -> {
            Transaction tx = new Transaction();

            if (current().nextBoolean()) {
                tx.fromAccountId = first;
                tx.toAccountId = second;
            }
            else {
                tx.fromAccountId = second;
                tx.toAccountId = first;
            }

            tx.amount = amount;

            try {
                transactionService.transfer(tx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        stressTester.shutdown();
    }
}