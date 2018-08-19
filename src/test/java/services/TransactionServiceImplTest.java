package services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import guice.BasicModule;
import model.Account;
import model.Transaction;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static model.Transaction.Status.CANCELED;
import static model.Transaction.Status.DONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class TransactionServiceImplTest {

    private TransactionServiceImpl transactionService;

    private static Injector injector = Guice.createInjector(new BasicModule());

    @Before
    public void init() {
        transactionService = injector.getInstance(TransactionServiceImpl.class);
        transactionService.accountService = mock(AccountService.class);
    }

    @Test
    public void transactionStatusShouldBeCanceledWhenRemitterHaveNotEnoughMoney() {
        Transaction tx = new Transaction();
        tx.fromAccountId = 1L;
        tx.toAccountId = 2L;
        tx.amount = new BigDecimal(10);
        Account account = new Account(new BigDecimal(5), "myAcc");
        doReturn(account).when(transactionService.accountService).accountById(1L);

        transactionService.transfer(tx);

        assertThat(transactionService.transactionById(tx.id).status).isEqualTo(CANCELED);
    }

    @Test
    public void transactionStatusShouldBeDoneWhenTransactionSucceed() {
        Transaction tx = new Transaction();
        tx.fromAccountId = 1L;
        tx.toAccountId = 2L;
        tx.amount = new BigDecimal(10);
        Account remitter = new Account(new BigDecimal(15), "myAcc");
        Account beneficiary = new Account(new BigDecimal(10), "anotherAcc");

        doReturn(remitter).when(transactionService.accountService).accountById(tx.fromAccountId);
        doReturn(beneficiary).when(transactionService.accountService).accountById(tx.toAccountId);

        transactionService.transfer(tx);

        assertThat(transactionService.transactionById(tx.id).status).isEqualTo(DONE);
    }

}