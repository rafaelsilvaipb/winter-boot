package start;

import start.annotations.Autowired;
import start.annotations.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String findUser() {
        return userRepository.findUser();
    }

    public String createUser() {
        return "Usuário criado com sucesso";
    }

    public String updateUser() {
        return "Usuário atualizado com sucesso";
    }

    public String deleteUser() {
        return "Usuário removido com sucesso";
    }
}