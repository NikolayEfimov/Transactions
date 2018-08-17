package services;

import model.Account;

import java.util.List;

public interface AccountService {

    List<Account> accounts();

    Account accountById(Long id);

    Account create(Account account);
}
