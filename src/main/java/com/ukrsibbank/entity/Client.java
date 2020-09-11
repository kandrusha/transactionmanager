package com.ukrsibbank.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  @EqualsAndHashCode.Exclude
  private Long id;
  @NaturalId
  @Column(name = "inn", unique = true, nullable = false)
  private String inn;
  @Column(name = "first_name")
  private String firstName;
  @Column(name = "last_name")
  private String lastName;
  @Column(name = "middle_name")
  private String middleName;
  @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, orphanRemoval=true)
  @Default
  @EqualsAndHashCode.Exclude
  private List<Transaction> transactions = new ArrayList<>();

  public void addTransaction(Transaction transaction) {
    if (transaction != null) {
      transactions.add(transaction);
      transaction.setClient(this);
    }
  }

  public void removeTransaction(Transaction transaction) {
    if (transaction != null) {
      transaction.setClient(null);
    }
    transactions.remove(transaction);
  }
}
