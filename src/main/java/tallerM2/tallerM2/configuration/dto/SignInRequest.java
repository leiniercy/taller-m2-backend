/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.configuration.dto;

import lombok.Data;

@Data
public class SignInRequest {
    private String username;
    private String password;
}