package com.gerenciadorlehsa;

import com.gerenciadorlehsa.entity.Role;
import com.gerenciadorlehsa.entity.User;
import com.gerenciadorlehsa.permissions.PermissionEnum;
import com.gerenciadorlehsa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class GerenciadorLehsaApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;

	public static void main(String[] args) {

		SpringApplication.run(GerenciadorLehsaApplication.class, args);
	}

	// O usuário admin vai ter permissão de administrador e usuário comum
	@Override
	public void run (String... args) throws Exception {
		User user = new User ();
		user.setUsername ("administrador");
		user.setPassword ("123456");
		user.setFullName ("Luciano Melo");
		user.setCpf ("01643634089");
		user.setEmail ("luciano-IFS@gmail.com");

		List<Role> roles = new ArrayList<> ();
		Role roleAdmin = new Role ();
		roleAdmin.setNome (PermissionEnum.ADMIN);

		Role roleUser = new Role ();
		roleUser.setNome (PermissionEnum.USER);

		roles.add (roleAdmin);
		roles.add (roleUser);

		user.setRoles (roles);

		userService.create (user);
	}
}
