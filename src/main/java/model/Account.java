package model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public BigDecimal balance;
    public String name;

    public Account(BigDecimal balance, String name) {
        this.balance = balance;
        this.name = name;
    }

    public Account() {
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        if (!id.equals(account.id)) return false;
        if (balance.compareTo(account.balance) != 0) return false;
        return name.equals(account.name);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + balance.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
