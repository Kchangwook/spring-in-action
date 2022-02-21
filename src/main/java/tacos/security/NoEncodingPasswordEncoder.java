package tacos.security;

import org.springframework.security.crypto.password.PasswordEncoder;

public class NoEncodingPasswordEncoder implements PasswordEncoder {
	@Override
	public boolean upgradeEncoding(String encodedPassword) {
		return PasswordEncoder.super.upgradeEncoding(encodedPassword);
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.equals(encodedPassword);
	}
}
