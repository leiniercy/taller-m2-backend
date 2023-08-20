package tallerM2.tallerM2.services.servicesImpl;

import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.User;
import tallerM2.tallerM2.repository.UserRepository;
import tallerM2.tallerM2.services.IUserService;
import tallerM2.tallerM2.utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import tallerM2.tallerM2.model.ERole;
import tallerM2.tallerM2.model.Role;
import tallerM2.tallerM2.repository.RoleRepository;
import tallerM2.tallerM2.utils.dto.UserEditRequest;
import tallerM2.tallerM2.utils.dto.UserSaveRequest;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    public EntityManager em;

    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return Usuario
     */
    @Override
    public User findById(Long id) throws ValueNotFound, BadRequest {
        Optional<User> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("User not found");
        }
        return op.get();
    }

    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL NOMBRE DE USUARIO
     *
     * @param username no debe ser vacio.
     * @return Usuario
     */
    @Override
    public User findByUsername(String username) throws ValueNotFound, BadRequest {
        Optional<User> op = repository.findByUsername(username);
        if (!repository.existsByUsername(username)) {
            throw new ValueNotFound("User not found");
        }
        return op.get();
    }
    
    /**
     * METODO PARA VERIFICAR SI EL OBJETO EXISTE, PREGUNTANDO POR EL NOMBRE DE USUARIO
     *
     * @param email no debe ser vacio.
     * @return Usuario
     */
    @Override
    public User findByEmail(String email) throws ValueNotFound, BadRequest {
        Optional<User> op = repository.findByEmail(email);
        if (!repository.existsByEmail(email)) {
            throw new ValueNotFound("User not found");
        }
        return op.get();
    }
    
    /**
     * METODO QUE DEVUELVE UNA LISTA CON TODOS LOS OBJETOS DE UN MISMO TIPO
     * ESPECIFICADO PREVIAMENTE
     *
     * @return List<User>
     */
    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    /**
     * METODO QUE DEVUELVE UNA LISTA ORDENADA ASCENDENTEMENTE CON TODOS LOS
     * OBJETOS DE UN MISMO TIPO ESPECIFICADO PREVIAMENTE
     *
     * @return List<User>
     */
    @Override
    public List<User> findAllOrderByIdAsc() {
        return em.createQuery("SELECT u FROM User u ORDER BY u.id")
                .getResultList();
    }

    /**
     * PRIMERO SE VERIFICA QUE EL OBJETO NO EXISTA, Y LUEGO SE GURADA LA
     * INFORMACION
     *
     * @param userRequest contiene la informacion del nuevo usuario
     * @return User el usuario almacenado
     */
    @Override
    public User save(UserSaveRequest userRequest) throws Conflict, ValueNotFound, BadRequest {
        if (repository.existsByUsername(userRequest.getUsername())
                || repository.existsByEmail(userRequest.getEmail())) {
            throw new Conflict("This user is aviable");
        }
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setTaller(userRequest.getTaller());
        user.setPassword(Util.encrypteMe(userRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Optional<Role> opRole;
        if (userRequest.getRol().equals("ROLE_ADMIN")) {
            opRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        } else if (userRequest.getRol().equals("ROLE_MODERATOR")) {
            opRole = roleRepository.findByName(ERole.ROLE_MODERATOR);
        } else {
            opRole = roleRepository.findByName(ERole.ROLE_USER);
        }
        if (opRole.isEmpty()) {
            throw new ValueNotFound("Rol not found");
        }
        roles.add(opRole.get());
        user.setRoles(roles);
        return repository.saveAndFlush(user);
    }

    /**
     * METODO PARA ACTUALIZAR TODA LA INFORMACION DE UN OBJETO MEDIANTE SU
     * IDENTIFICADOR
     *
     * @param from informacion actualizada de la venta que se quiere modificar.
     * @param id identificador deL usaurio a modificar.
     * @return to infomracion actualizada del usuario.
     */
    @Override
    public User update(UserEditRequest from, Long id) throws ValueNotFound, BadRequest {

        Optional<User> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("User not found");
        }
        User user = op.get();
        user.setUsername(from.getUsername());
        user.setEmail(from.getEmail());
        user.setTaller(from.getTaller());
        user.setPassword(op.get().getPassword());
        Set<Role> roles = new HashSet<>();
        Optional<Role> opRole;
        if (from.getRol().equals("ROLE_ADMIN")) {
            opRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        } else if (from.getRol().equals("ROLE_MODERATOR")) {
            opRole = roleRepository.findByName(ERole.ROLE_MODERATOR);
        } else {
            opRole = roleRepository.findByName(ERole.ROLE_USER);
        }
        if (opRole.isEmpty()) {
            throw new ValueNotFound("Rol not found");
        }
        roles.add(opRole.get());
        user.setRoles(roles);
        return repository.saveAndFlush(user);
    }

    public User changePassword(Long id, String password) throws ValueNotFound, BadRequest{
        Optional<User> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("User not found");
        }
        User user = op.get();
        user.setUsername(op.get().getUsername());
        user.setEmail(op.get().getEmail());
        user.setTaller(op.get().getTaller());
        user.setPassword(Util.encrypteMe(password));
        user.setRoles(op.get().getRoles());
        return repository.saveAndFlush(user);
    }

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param user Informacion del usuario que se quiere eliminar. No debe ser
     * vacio
     * @return User toda la informacion del objeto elimnado.
     */
    @Override
    public User delete(User user) throws ValueNotFound, BadRequest {
        if (!repository.existsByUsername(user.getUsername())) {
            throw new ValueNotFound("User not found");
        }
        User u = user;
        repository.delete(user);
        return u;
    }

    /**
     * METODO PARA VERIFICAR SI EL MOVIL EXISTE, PREGUNTANDO POR EL ID
     *
     * @param id no debe ser vacio {@literal null}.
     * @return User toda la informacion del objeto elimnado.
     */
    @Override
    public User deleteById(Long id) throws ValueNotFound, BadRequest {
        Optional<User> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new ValueNotFound("User not found");
        }
        repository.deleteById(id);
        return op.get();
    }

    /**
     * METODO PARA ELIMINAR UN CONJUNTO DE OBJETOS.
     *
     * @param users listado de objetos a eliminar
     * @return List<User> listado actualizado con la informacion que queda en la
     * BD
     */
    @Override
    public List<User> deleteAll(List<User> users) throws ValueNotFound, BadRequest {
        repository.deleteAll(users);
        return findAllOrderByIdAsc();
    }

    @Override
    public long count() {
        Query query = em.createQuery("select coalesce(sum(u.id),0) from User u");
        Long result = (Long) query.getSingleResult();
        return result != null ? result : 0;
    }
}
