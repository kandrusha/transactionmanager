package com.ukrsibbank.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.ukrsibbank.entity.Client;
import com.ukrsibbank.entity.Transaction;
import com.ukrsibbank.repository.AccountClientRepository;
import com.ukrsibbank.repository.AccountTransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration
public class AccountTransactionFileServiceImplIT {

  @Autowired
  private AccountTransactionFileServiceImpl transactionFileService;

  @Autowired
  private AccountTransactionRepository transactionRepository;
  @Autowired
  private AccountClientRepository clientRepository;

  @Test
  @Transactional
  public void shouldSaveTransactionsFromFile() {
    transactionFileService.saveTransactions("src/test/resources/Java_test.xml");

    Iterable<Transaction> transactions = transactionRepository.findAll();
    transactions.forEach(transaction -> {
      transaction.setId(null);
      transaction.getClient().setId(null);
    });

    Client client1 = createClient("Ivan", "Ivanoff", "Ivanoff", "1234567890");
    Client client2 = createClient("Ivan", "Petroff", "Petroff", "1234567891");

    Transaction transaction1 = createTransaction(10.01, "A PLACE 1", client1);
    Transaction transaction2 = createTransaction(9876.01, "A PLACE 2", client1);
    Transaction transaction3 = createTransaction(10.01, "A PLACE 1", client2);
    Transaction transaction4 = createTransaction(9876.01, "A PLACE 2", client2);

    assertThat(transactions).containsExactlyInAnyOrder(transaction1, transaction2, transaction3, transaction4);
  }

  @Test
  @Transactional
  public void shouldSaveClientsFromFile() {
    transactionFileService.saveTransactions("src/test/resources/Java_test.xml");

    Iterable<Client> clients = clientRepository.findAll();
    clients.forEach(client -> client.setId(null));

    Client client1 = createClient("Ivan", "Ivanoff", "Ivanoff", "1234567890");
    Client client2 = createClient("Ivan", "Petroff", "Petroff", "1234567891");

    assertThat(clients).containsExactlyInAnyOrder(client1, client2);
  }

  private Client createClient(String firstName, String middleName, String lastName, String inn) {
    return Client.builder()
      .firstName(firstName)
      .middleName(middleName)
      .lastName(lastName)
      .inn(inn)
      .build();
  }

  private Transaction createTransaction(double amount, String place, Client client) {
    return Transaction.builder()
      .amount(amount)
      .place(place)
      .currency("UAH")
      .card("123456****1234")
      .client(client)
      .build();
  }
}
