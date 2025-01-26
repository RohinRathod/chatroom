package in.group.chatapp.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.group.chatapp.Entity.User;
import in.group.chatapp.Entity.UserDto;
import in.group.chatapp.repo.UserRepository;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	public PasswordEncoder passwordEncoder;


	public boolean findByUsername(String user) {
		return userRepository.existsByUsername(user);
	}
	
	
	public Optional<User> findByUsernameObj(String user) {
		return userRepository.findByUsername(user);
	}
	
	

	public boolean registerUser(UserDto userDto) {
		userDto.setRole("ROLE_USER");
		
		if (userRepository.existsByUsername(userDto.getUsername())) {
			return false; // User already exists
		}
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setRole(userDto.getRole());
		userRepository.save(user);
		return true;
	}
}
