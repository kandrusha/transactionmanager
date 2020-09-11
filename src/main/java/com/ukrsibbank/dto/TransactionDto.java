package com.ukrsibbank.dto;

import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "transaction")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

  public static final String TAG_NAME = "transaction";

  private String place;
  private double amount;
  private String currency;
  private String card;
  private ClientDto client;

}
