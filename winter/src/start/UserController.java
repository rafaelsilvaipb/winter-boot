package start;

import start.annotations.Autowired;
import start.annotations.DeleteMapping;
import start.annotations.GetMapping;
import start.annotations.PostMapping;
import start.annotations.PutMapping;
import start.annotations.RequestMapping;
import start.annotations.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getUsers() {
        return "Lista de usuários";
    }

    @GetMapping("/find")
    public String findUser() {
        return userService.findUser();
    }

    @PostMapping
    public String createUser() {
        return userService.createUser();
    }

    @PutMapping
    public String updateUser() {
        return userService.updateUser();
    }

    @DeleteMapping
    public String deleteUser() {
        return userService.deleteUser();
    }
}