package com.ukrsibbank.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

  private String firstName;
  private String lastName;
  private String middleName;
  private String inn;
}
