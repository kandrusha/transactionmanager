package com.ukrsibbank.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, nullable = false)
  private Long id;
  @Column(name = "place")
  private String place;
  @Column(name = "amount")
  private double amount;
  @Column(name = "currency")
  private String currency;
  @Column(name = "card")
  private String card;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
  private Client client;

//  public void setClientAndAdd(Client client) {
//    this.client = client;
//    if (client != null) {
//      client.addTransaction(this);
//    }
//  }
}
