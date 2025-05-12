package models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessfulRegisterBodyModel {

    String email, password;

}
