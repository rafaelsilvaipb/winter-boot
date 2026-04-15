package start;

import start.annotations.Autowired;
import start.annotations.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void execute() {
        System.out.println(userRepository.findUser());
    }
}