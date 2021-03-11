package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.models.AuthenticationRequest;
import com.models.AuthenticationResponse;

@RestController
public class HomeSource {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private jwtutil jwtTokenUtil;
	
	@RequestMapping("/user")
	public String home()
	{
		return "Welcome you have now learnt JWT";
	}


	@RequestMapping(value="/authenticate",method=RequestMethod.POST)
	public ResponseEntity<?>createAuthenticationToken(@RequestBody 	AuthenticationRequest authenticationRequest) throws Exception
	{
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword()));
		}
		catch(BadCredentialsException e)
		{
            throw new Exception("Incorrect username",e);
		}
		
		final UserDetails userDetails= myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt=jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}	