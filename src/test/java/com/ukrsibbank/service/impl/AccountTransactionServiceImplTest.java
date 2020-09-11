package com.ukrsibbank.service.impl;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ukrsibbank.dto.ClientDto;
import com.ukrsibbank.dto.TransactionDto;
import com.ukrsibbank.entity.Client;
import com.ukrsibbank.entity.Transaction;
import com.ukrsibbank.repository.AccountClientRepository;
import com.ukrsibbank.repository.AccountTransactionRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class AccountTransactionServiceImplTest {

  @Mock
  private AccountTransactionRepository transactionRepository;
  @Mock
  private AccountClientRepository clientRepository;
  @Mock
  private ModelMapper mapper;
  @InjectMocks
  private AccountTransactionServiceImpl transactionService;

  private static final String INN1 = "iin1";
  private static final String INN2 = "iin2";

  @Test
  public void shouldSaveTransaction() {
    TransactionDto transactionDto = createTransactionDto();
    Transaction transactionEntity = createTransactionEntity();
    Client clientEntity = transactionEntity.getClient();
    when(mapper.map(transactionDto, Transaction.class)).thenReturn(transactionEntity);
    when(clientRepository.save(clientEntity)).thenReturn(clientEntity);

    transactionService.saveTransactions(Collections.singletonList(transactionDto));

    verify(clientRepository).findByInn(clientEntity.getInn());
    verify(clientRepository).save(clientEntity);
    verify(transactionRepository).saveAll(Collections.singletonList(transactionEntity));
  }

  @Test
  public void shouldSaveTransactionsWithSameUser() {
    ClientDto clientDto = createClientDto(INN1);
    TransactionDto transactionDto1 = createTransactionDto(clientDto);
    TransactionDto transactionDto2 = createTransactionDto(clientDto);
    Client clientEntity = createClientEntity(INN1);
    Transaction transactionEntity1 = createTransactionEntity(clientEntity);
    Transaction transactionEntity2 = createTransactionEntity(clientEntity);
    when(mapper.map(transactionDto1, Transaction.class)).thenReturn(transactionEntity1);
    when(mapper.map(transactionDto2, Transaction.class)).thenReturn(transactionEntity2);
    when(clientRepository.save(clientEntity)).thenReturn(clientEntity);

    transactionService.saveTransactions(Arrays.asList(transactionDto1, transactionDto2));

    verify(clientRepository).findByInn(clientEntity.getInn());
    verify(clientRepository).save(clientEntity);
    verify(transactionRepository).saveAll(Arrays.asList(transactionEntity1, transactionEntity2));
  }

  @Test
  public void shouldSaveTransactionsWithDifferentUsers() {
    ClientDto clientDto1 = createClientDto(INN1);
    ClientDto clientDto2 = createClientDto(INN2);
    TransactionDto transactionDto1 = createTransactionDto(clientDto1);
    TransactionDto transactionDto2 = createTransactionDto(clientDto2);
    Client clientEntity1 = createClientEntity(INN1);
    Client clientEntity2 = createClientEntity(INN2);
    Transaction transactionEntity1 = createTransactionEntity(clientEntity1);
    Transaction transactionEntity2 = createTransactionEntity(clientEntity2);
    when(mapper.map(transactionDto1, Transaction.class)).thenReturn(transactionEntity1);
    when(mapper.map(transactionDto2, Transaction.class)).thenReturn(transactionEntity2);
    when(clientRepository.save(clientEntity1)).thenReturn(clientEntity1);
    when(clientRepository.save(clientEntity2)).thenReturn(clientEntity2);

    transactionService.saveTransactions(Arrays.asList(transactionDto1, transactionDto2));

    verify(clientRepository).findByInn(INN1);
    verify(clientRepository).findByInn(INN2);
    verify(clientRepository).save(clientEntity1);
    verify(clientRepository).save(clientEntity2);
    verify(transactionRepository).saveAll(Arrays.asList(transactionEntity1, transactionEntity2));
  }

  @Test
  public void shouldDoNothingForEmptyCollection() {
    transactionService.saveTransactions(new ArrayList<>());

    verify(clientRepository, never()).findByInn(any());
    verify(clientRepository, never()).save(any());
    verify(transactionRepository, never()).saveAll(any());
  }

  @Test
  public void shouldDoNothingForNull() {
    transactionService.saveTransactions(null);

    verify(clientRepository, never()).findByInn(any());
    verify(clientRepository, never()).save(any());
    verify(transactionRepository, never()).saveAll(any());
  }

  private TransactionDto createTransactionDto() {
    return createTransactionDto(createClientDto(INN1));
  }

  private TransactionDto createTransactionDto(ClientDto clientDto) {
    return TransactionDto.builder()
      .amount(10)
      .card("1234")
      .currency("USD")
      .place("place")
      .client(clientDto)
      .build();
  }

  private ClientDto createClientDto(String inn) {
    return ClientDto.builder()
      .firstName("Ivan")
      .middleName("Ivanovich")
      .lastName("Ivanov")
      .inn(inn)
      .build();
  }

  private Transaction createTransactionEntity() {
    return createTransactionEntity(createClientEntity(INN1));
  }

  private Transaction createTransactionEntity(Client client) {
    return Transaction.builder()
      .amount(10)
      .card("1234")
      .currency("USD")
      .place("place")
      .client(client)
      .build();
  }

  private Client createClientEntity(String inn) {
    return Client.builder()
      .firstName("Ivan")
      .middleName("Ivanovich")
      .lastName("Ivanov")
      .inn(inn)
      .build();
  }


}
