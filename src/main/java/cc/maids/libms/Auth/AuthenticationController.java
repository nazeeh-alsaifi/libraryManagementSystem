package cc.maids.libms.Auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("{$apiPrefix}/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginRequest request) {        
        return ResponseEntity.ok(service.authenticate(request));
    }
    
    
}
