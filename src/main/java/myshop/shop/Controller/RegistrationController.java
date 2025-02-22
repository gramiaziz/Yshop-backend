package myshop.shop.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import myshop.shop.Models.Customer;
import myshop.shop.Models.Role;
import myshop.shop.Repository.CustomerRep;
import myshop.shop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RegistrationController {

    @Autowired
    private CustomerRep customerRep;
    @Autowired
    private UserService authService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public String createUser(@RequestBody Customer user) {
        Optional<Customer> c = customerRep.findByUsername(user.getUsername());
        if (c.isPresent()) {
            return "usename exist";
        }
        else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.USER);
            customerRep.save(user);
            return "user created";
        }

    }
    @GetMapping("/user/username")
    public String getLoggedInUsername() {
        return authService.getLoggedInUsername();
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Clear the security context
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "You have been logged out successfully.";
    }

}