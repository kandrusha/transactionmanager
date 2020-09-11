package com.ukrsibbank.service.impl;

import com.ukrsibbank.dto.TransactionDto;
import com.ukrsibbank.entity.Client;
import com.ukrsibbank.entity.Transaction;
import com.ukrsibbank.repository.AccountClientRepository;
import com.ukrsibbank.repository.AccountTransactionRepository;
import com.ukrsibbank.service.AccountTransactionService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AccountTransactionServiceImpl implements AccountTransactionService {
  @Autowired
  private AccountTransactionRepository transactionRepository;
  @Autowired
  private AccountClientRepository clientRepository;
  @Autowired
  private ModelMapper mapper;

  @Override
  @Transactional
  public void saveTransaction(TransactionDto transactionDto) {
    saveTransactions(Collections.singletonList(transactionDto));
  }

  @Override
  @Transactional
  public void saveTransactions(List<TransactionDto> transactionDtos) {
    if (transactionDtos == null || transactionDtos.isEmpty()) {
      return;
    }

    HashMap<String, List<Transaction>> groupedTransactions = transactionDtos.stream()
      .map(transactionDto -> mapper.map(transactionDto, Transaction.class))
      .collect(Collectors.groupingBy(o -> o.getClient().getInn(), HashMap::new, Collectors.toList()));

    groupedTransactions.forEach((inn, transactions) -> {
      Client client = clientRepository.findByInn(inn);
      if (client == null) {
        client = clientRepository.save(transactions.get(0).getClient());
      }
      final Client savedClient = client;
      transactions.forEach(transaction -> transaction.setClient(savedClient));
    });

    List<Transaction> transactions = groupedTransactions.values().stream()
      .flatMap(Collection::stream)
      .collect(Collectors.toList());
    transactionRepository.saveAll(transactions);
  }
}
