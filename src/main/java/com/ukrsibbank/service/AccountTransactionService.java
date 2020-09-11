package com.ukrsibbank.service;

import com.ukrsibbank.dto.TransactionDto;
import java.util.List;

public interface AccountTransactionService {

  void saveTransaction(TransactionDto transactionDto);

  void saveTransactions(List<TransactionDto> transactionDto);
}
