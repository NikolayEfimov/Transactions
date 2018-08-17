package guice;

import com.google.inject.AbstractModule;
import services.AccountService;
import services.AccountServiceImpl;
import services.TransactionService;
import services.TransactionServiceImpl;

public class BasicModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountService.class).to(AccountServiceImpl.class);
        bind(TransactionService.class).to(TransactionServiceImpl.class);
    }
}
