package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    public Long id;
    public Long fromAccountId;
    public Long toAccountId;
    public Double amount;
    public String state;

}
