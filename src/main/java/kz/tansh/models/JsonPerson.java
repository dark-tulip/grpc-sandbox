package kz.tansh.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class JsonPerson {
  private String  lastName;
  private int     age;
  private String  email;
  private boolean employed;
  private double  salary;
  private long    bankAccountNumber;
  private int     balance;
}
