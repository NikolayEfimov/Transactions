package services;

import dao.AccountDao;
import model.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AccountServiceImpl implements AccountService {

    @Inject private AccountDao accountDao;

    @Override
    public List<Account> accounts() {
        return accountDao.accounts();
    }

    @Override
    public Account accountById(Long id) {
        return accountDao.accountById(id);
    }

    @Override
    public Account create(Account account) {
        return (Account) accountDao.create(account);
    }

}
