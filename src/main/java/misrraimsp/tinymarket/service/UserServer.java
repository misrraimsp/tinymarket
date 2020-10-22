package misrraimsp.tinymarket.service;

import lombok.RequiredArgsConstructor;
import misrraimsp.tinymarket.data.UserRepository;
import misrraimsp.tinymarket.model.Cart;
import misrraimsp.tinymarket.model.Product;
import misrraimsp.tinymarket.model.User;
import misrraimsp.tinymarket.model.dto.UserDTO;
import misrraimsp.tinymarket.util.EntityNotFoundByIdException;
import misrraimsp.tinymarket.util.enums.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServer implements UserDetailsService {

    private final UserRepository userRepository;
    private final CartServer cartServer;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found");
        return user;
    }

    public User findById(Long userId) throws EntityNotFoundByIdException {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundByIdException(userId,User.class.getSimpleName()));
    }

    public User persist(UserDTO dto, PasswordEncoder passwordEncoder, Role role, Cart cart) {
        if (role == null) role = Role.CUSTOMER;
        if (cart == null) cart = new Cart();
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(role);
        user.setCart(cartServer.persist(cart));
        return userRepository.save(user);
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public void addProductToCart(Product product, Long userId) {
        cartServer.addProduct(product, this.findById(userId).getCart());
    }
}
