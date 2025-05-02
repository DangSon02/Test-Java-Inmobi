package burundi.ilucky.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import burundi.ilucky.Exeption.UsernameAlreadyExistsException;
import burundi.ilucky.jwt.JwtTokenProvider;
import burundi.ilucky.model.User;
import burundi.ilucky.payload.AuthRequest;
import burundi.ilucky.payload.AuthResponse;
import burundi.ilucky.payload.Response;
import burundi.ilucky.service.AuthService;
import burundi.ilucky.service.BlackListService;
import burundi.ilucky.service.RedisTokenService;
import burundi.ilucky.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthRegisterServiceImpl implements AuthService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTokenService redisTokenService;

    @Autowired
    private BlackListService blackListService;

    @Override
    public Response register(AuthRequest authRequest) {

        if (userService.findByUserName(authRequest.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("Username already exists!");
        }

        String password = hashPassword(authRequest.getPassword());

        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(password);
        user.setAddTime(new Date());
        user.setLastUpdate(new Date());

        userService.saveUser(user);

        return new Response("Success", "User register successfully", null);
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();

        String jwt = tokenProvider.generateToken(user);

        // save token to redis
        String redisKey = "auth:token:" + user.getId();
        long ttlSeconds = tokenProvider.getTTLFromToken(jwt);
        redisTokenService.saveToken(redisKey, jwt, ttlSeconds);

        return new AuthResponse(jwt);

    }

    @Override
    public Response logout(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        User user = (User) authentication.getPrincipal();

        // lay token tu header Authoriztion
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);

        // xoa token active redis
        String redisKey = "auth:token:" + user.getId();
        redisTokenService.deleteToken(redisKey);

        // tinh thoi gian song con lai token
        long ttlSeconds = tokenProvider.getTTLFromToken(token);

        // them token vao redis blacklist
        blackListService.addTokenToBlacklist(token, ttlSeconds);

        return new Response("Success", "Log out is success", null);
    }

}
