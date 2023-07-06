package dev.cyberser.labj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // USERS ENDPOINTS
    @GetMapping("/all")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // USER ENDPOINTS
    @PostMapping(value = "/post", consumes = "application/json")
    public User createUser(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setUsername(userDetails.getUsername());
        String newPass = userDetails.getPassword();
        if (newPass != null) user.setPassword(userDetails.getPassword());
        
        List<PDF> updatedPDFs = userDetails.getPdfs();
        if (updatedPDFs != null) {
            user.getPdfs().clear();
            for (PDF pdf : updatedPDFs) {
                pdf.setAuthor(user);
                user.getPdfs().add(pdf);
            }
        }
        return userRepository.save(user);
    }
     
    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepository.delete(user);
        return ResponseEntity.ok().build();
    }
}
