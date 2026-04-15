package start.impl;

import start.UserRepository;
import start.annotations.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public String findUser() {
        return "Usuário encontrado no banco";
    }
}