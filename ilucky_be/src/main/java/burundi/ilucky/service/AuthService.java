package burundi.ilucky.service;

import burundi.ilucky.payload.AuthRequest;
import burundi.ilucky.payload.AuthResponse;
import burundi.ilucky.payload.Response;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    Response register(AuthRequest authRequest);

    AuthResponse login(AuthRequest authRequest);

    Response logout(HttpServletRequest request);

}
