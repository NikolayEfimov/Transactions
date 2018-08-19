package services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import exceptions.AccountNotFoundException;
import guice.BasicModule;
import model.Account;
import org.junit.Before;
import org.junit.Test;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class AccountServiceImplTest {

    private AccountService accountService;

    private static Injector injector = Guice.createInjector(new BasicModule());

    @Before
    public void init() {
        accountService = injector.getInstance(AccountService.class);
    }

    @Test
    public void throwExceptionWhenAccountNotFound() {
        accountService.deleteAll();
        assertThatExceptionOfType(AccountNotFoundException.class).isThrownBy(() -> accountService.accountById(100L));
    }

    @Test
    public void accountById() {
        Account account = accountService.create(new Account(ZERO, "myAcc"));

        assertThat(accountService.accountById(account.id)).isEqualTo(account);
    }

    @Test
    public void accounts() {
        accountService.deleteAll();

        Account firstAccount = accountService.create(new Account(ONE, "first"));
        Account secondAccount = accountService.create(new Account(ONE, "second"));

        assertThat(accountService.accounts()).hasSize(2);
        assertThat(accountService.accounts()).contains(firstAccount, secondAccount);
    }

    @Test
    public void deleteAllAccounts() {
        accountService.deleteAll();
        assertThat(accountService.accounts()).hasSize(0);

        createNAccounts(3);
        assertThat(accountService.accounts()).hasSize(3);

        accountService.deleteAll();
        assertThat(accountService.accounts()).hasSize(0);
    }

    @Test
    public void updateAccount() {
        Account account = accountService.create(new Account(ZERO, "myAcc"));

        assertThat(account.name).isEqualTo("myAcc");

        account.name = "newName";

        accountService.update(account);
        assertThat(account.name).isEqualTo("newName");
    }

    private void createNAccounts(int nAccounts) {
        for (int i = 0; i < nAccounts; i++) {
            accountService.create(new Account(ONE, "account_" + i));
        }
    }

}