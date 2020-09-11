package com.ukrsibbank.service.impl;

import com.ukrsibbank.dto.TransactionDto;
import com.ukrsibbank.service.AccountTransactionFileService;
import com.ukrsibbank.service.AccountTransactionService;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountTransactionFileServiceImpl implements AccountTransactionFileService {

  @Autowired
  private AccountTransactionService transactionService;

  private int bufferSize = 100;

  public void saveTransactions(String fileName) {
    try {
      FileReader fileReader = new FileReader(fileName);
      XMLStreamReader xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(fileReader);
      JAXBContext context = JAXBContext.newInstance(TransactionDto.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      List<TransactionDto> transactionBuffer = new ArrayList<>();
      while (xmlReader.nextTag() == XMLStreamConstants.START_ELEMENT) {
        if (!TransactionDto.TAG_NAME.equals(xmlReader.getLocalName())) {
          continue;
        }
        TransactionDto transaction = (TransactionDto) unmarshaller.unmarshal(xmlReader);
        transactionBuffer.add(transaction);
        if (transactionBuffer.size() >= bufferSize) {
          transactionService.saveTransactions(transactionBuffer);
          transactionBuffer = new ArrayList<>();
        }
      }
      if (!transactionBuffer.isEmpty()) {
        transactionService.saveTransactions(transactionBuffer);
      }
    } catch (XMLStreamException | FileNotFoundException | JAXBException e) {
      throw new RuntimeException("Incorrect file or file name", e);
    }
  }

  public void setBufferSize(int bufferSize) {
    this.bufferSize = bufferSize;
  }

  public int getBufferSize() {
    return this.bufferSize;
  }
}
