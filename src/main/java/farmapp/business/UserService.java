package farmapp.business;

import farmapp.domain.User;

import java.util.List;

public interface UserService {

    User authenticate(String username, String password) throws BusinessException;

    void registration(User user) throws BusinessException;

    void modifyProfile(User user) throws BusinessException;

    void deleteAccount(int id);

    List<User> findAllUsers () throws BusinessException;
}
