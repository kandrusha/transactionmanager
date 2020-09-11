package com.ukrsibbank;

import com.ukrsibbank.service.AccountTransactionFileService;
import java.util.Scanner;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccountConfig {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(AccountConfig.class, args);

    System.out.println("Please provide file/folder name");
    Scanner scanner = new Scanner(System.in);
    String fileName = scanner.next();
    AccountTransactionFileService transactionManager = context.getBean(AccountTransactionFileService.class);
    transactionManager.saveTransactions(fileName);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
