package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    public Long id;
    public Long fromAccountId;
    public Long toAccountId;
    public Double amount;
    public String state;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        if (!id.equals(that.id)) return false;
        if (!fromAccountId.equals(that.fromAccountId)) return false;
        if (!toAccountId.equals(that.toAccountId)) return false;
        if (!amount.equals(that.amount)) return false;
        return state.equals(that.state);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + fromAccountId.hashCode();
        result = 31 * result + toAccountId.hashCode();
        result = 31 * result + amount.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }
}
