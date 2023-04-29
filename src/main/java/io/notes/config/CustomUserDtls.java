package io.notes.config;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.notes.entity.UserDtls;

public class CustomUserDtls implements UserDetails{
	
	private UserDtls userDtls;

	public CustomUserDtls(UserDtls userDetails) {
		super();
		this.userDtls = userDetails;
	}
	
	public CustomUserDtls() {
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userDtls.getRole());
		
		return Arrays.asList(simpleGrantedAuthority);
	}

	@Override
	public String getPassword() {
		return userDtls.getPassword();
	}

	@Override
	public String getUsername() {
		return userDtls.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
