package com.swapnildube.inventory_management.Controller;


import com.swapnildube.inventory_management.DTO.LoginRequest;
import com.swapnildube.inventory_management.DTO.RegisterRequest;
import com.swapnildube.inventory_management.Entity.User;
import com.swapnildube.inventory_management.impl.JwtService;
import com.swapnildube.inventory_management.repository.UserRespository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/public")
@RestController
public class PublicController {

    private final UserRespository userRespository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public PublicController(UserRespository userRespository,
                            PasswordEncoder passwordEncoder,
                            JwtService jwtService) {
        this.userRespository = userRespository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }




    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if(userRespository.findByEmail(request.Email()).isPresent()){
            throw new IllegalArgumentException("Email already present");
        }
        User user = User.builder()
                .email(request.Email())
                .password(passwordEncoder.encode(request.password()))
                .warehousename(request.warehousename())
                .build();
        userRespository.save(user);
        return "Account Created";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        User user = userRespository.findByEmail(request.Email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Details"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid Password");
        }
        return   jwtService.generateToken(user.getId());

    }


}
