/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.utils.dto;

import lombok.Data;

/**
 *
 * @author Admin
 */
@Data
public class UserSaveRequest {

    private String username;
    private String email;
    private String taller;
    private String password;
    private String rol;
}
