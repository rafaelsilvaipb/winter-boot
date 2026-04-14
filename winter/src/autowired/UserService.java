package autowired;

public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void execute() {
        System.out.println(userRepository.findUser());
    }
}
