package tallerM2.tallerM2.services;

import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;
import tallerM2.tallerM2.model.User;

import java.util.List;
import tallerM2.tallerM2.utils.dto.UserRequest;

public interface IUserService {

    public User findById(Long id) throws ValueNotFound, BadRequest;
    
    public List<User> findAll();
    
    public List<User> findAllOrderByIdAsc();
   
    public User save(UserRequest userRequest) throws Conflict, ValueNotFound, BadRequest;

    public User update(UserRequest from, Long id) throws ValueNotFound, BadRequest;

    public User delete(User user) throws ValueNotFound, BadRequest;

    public User deleteById(Long id) throws ValueNotFound, BadRequest;

    public List<User> deleteAll(List<User> users)throws ValueNotFound, BadRequest;

    public long count();

}
