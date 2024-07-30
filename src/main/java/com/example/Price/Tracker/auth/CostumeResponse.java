package com.example.Price.Tracker.auth;

import com.example.Price.Tracker.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CostumeResponse {
    private String token;
    private Role role;
    private int id;

}
