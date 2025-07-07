package com.blogapi.payload;

import com.blogapi.model.ROLE_TYPE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    private Long id;
    private String username;
    private String email;
    private ROLE_TYPE role;


}
