package com.ukrsibbank.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ukrsibbank.dto.ClientDto;
import com.ukrsibbank.dto.TransactionDto;
import com.ukrsibbank.service.AccountTransactionService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AccountTransactionFileServiceImplTest {

  @Mock
  private AccountTransactionService transactionService;
  @InjectMocks
  private AccountTransactionFileServiceImpl transactionFileService;
  @Captor
  private ArgumentCaptor<List<TransactionDto>> transactionsCaptor;


  @Test
  public void shouldSaveTransactionsFromFile() {
    transactionFileService.saveTransactions("src/test/resources/Java_test.xml");

    verify(transactionService).saveTransactions(transactionsCaptor.capture());
    assertThat(transactionsCaptor.getValue()).containsExactlyElementsOf(createTransactions());
  }

  @Test
  public void shouldSaveTransactionsWithBuffer() {
    transactionFileService.setBufferSize(2);
    transactionFileService.saveTransactions("src/test/resources/Java_test.xml");

    verify(transactionService, times(2)).saveTransactions(transactionsCaptor.capture());
    List<TransactionDto> transactionDtos = transactionsCaptor.getAllValues().stream()
      .flatMap(Collection::stream)
      .collect(Collectors.toList());
    assertThat(transactionDtos).containsExactlyElementsOf(createTransactions());
  }

  @Test(expected = RuntimeException.class)
  public void shouldThrowRuntimeExceptionOnFileName() {
    transactionFileService.saveTransactions("incorrectPath");
  }

  private List<TransactionDto> createTransactions() {
    ClientDto client1 = createClient("Ivan", "Ivanoff", "Ivanoff", "1234567890");
    ClientDto client2 = createClient("Ivan", "Petroff", "Petroff", "1234567891");

    TransactionDto transaction1 = createTransaction(10.01, "A PLACE 1", client1);
    TransactionDto transaction2 = createTransaction(9876.01, "A PLACE 2", client1);
    TransactionDto transaction3 = createTransaction(10.01, "A PLACE 1", client2);
    TransactionDto transaction4 = createTransaction(9876.01, "A PLACE 2", client2);

    return Arrays.asList(transaction1, transaction2, transaction3, transaction4);
  }

  private ClientDto createClient(String firstName, String middleName, String lastName, String inn) {
    return ClientDto.builder()
      .firstName(firstName)
      .middleName(middleName)
      .lastName(lastName)
      .inn(inn)
      .build();
  }

  private TransactionDto createTransaction(double amount, String place, ClientDto client) {
    return TransactionDto.builder()
      .amount(amount)
      .place(place)
      .currency("UAH")
      .card("123456****1234")
      .client(client)
      .build();
  }
}
