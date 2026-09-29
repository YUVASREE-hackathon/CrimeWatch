package com.crimewatch.web;

import com.crimewatch.repository.UserRepository;
import com.crimewatch.service.*;
import com.crimewatch.web.dto.AuthDtos.UserView;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/users") @RequiredArgsConstructor
public class UserController {
    private final CurrentUserService current;
    private final UserRepository users;
    public record ProfileRequest(@NotBlank @Size(min = 2, max = 100) String fullName,
                                 @Pattern(regexp = "^$|^[0-9+() -]{7,20}$") String phone,
                                 @Size(max = 300) String address) {}
    @GetMapping("/me") UserView me() { return AuthService.view(current.get()); }
    @PutMapping("/me") @Transactional UserView update(@jakarta.validation.Valid @RequestBody ProfileRequest request) {
        var user = current.get(); user.setFullName(request.fullName().trim()); user.setPhone(request.phone()); user.setAddress(request.address());
        return AuthService.view(users.save(user));
    }
}

