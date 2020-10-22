package misrraimsp.tinymarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.tinymarket.util.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BasicEntity implements UserDetails {

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "fk_cart")
    private Cart cart;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Pedido> pedidos = new ArrayList<>();

    public void addPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setUser(this);
    }

    public String getName() {
        String[] parts = email.split("@");
        return (parts.length == 2) ? parts[0] : email;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.getText()));
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
