// package com.example.demo.service.impl;

// import com.example.demo.entity.User;
// import com.example.demo.exception.BadRequestException;
// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.repository.UserRepository;
// import com.example.demo.service.UserService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;

// @Service   // ✅ ADD THIS
// public class UserServiceImpl implements UserService {

//     private final UserRepository userRepository;
//     private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//     public UserServiceImpl(UserRepository userRepository) {
//         this.userRepository = userRepository;
//     }

//     @Override
//     public User register(User user) {
//         if (userRepository.findByEmail(user.getEmail()).isPresent()) {
//             throw new BadRequestException("Email already in use");
//         }
//         user.setPassword(encoder.encode(user.getPassword()));
//         user.setRole(User.Role.CUSTOMER.name());
//         return userRepository.save(user);
//     }

//     @Override
//     public User getById(Long id) {
//         return userRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//     }

//     @Override
//     public User findByEmail(String email) {
//         return userRepository.findByEmail(email).orElse(null);
//     }
// }


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        // ✅ FIX IS HERE
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {
            throw new BadRequestException("Invalid credentials");
        }

        // generate token etc
        String token = jwtUtil.generateToken(user.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}
