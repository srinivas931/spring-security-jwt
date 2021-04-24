package com.spakanati.springsecurityjwt;

import com.spakanati.springsecurityjwt.models.AuthenticationRequest;
import com.spakanati.springsecurityjwt.models.AuthenticationResponse;
import com.spakanati.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @GetMapping
    @RequestMapping({ "/hello" })
    public String hello() {
        return "Hello world";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch(BadCredentialsException ex) {
            throw new Exception("Incorrect username or password: " + ex);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
