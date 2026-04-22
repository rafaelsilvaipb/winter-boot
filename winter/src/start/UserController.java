package start;

import start.annotations.Autowired;
import start.annotations.DeleteMapping;
import start.annotations.GetMapping;
import start.annotations.PathVariable;
import start.annotations.PostMapping;
import start.annotations.PutMapping;
import start.annotations.RequestBody;
import start.annotations.RequestMapping;
import start.annotations.RequestParam;
import start.annotations.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers() {
        return "Lista de usuários";
    }

    @GetMapping("/find")
    public String findByQueryParam(@RequestParam("id") int id,
                                   @RequestParam("name") String name) {
        return "Buscando user por query param. id=" + id + ", name=" + name;
    }

    @GetMapping("/{id}")
    public UserResponse findUser(@PathVariable("id") int id) {
        return new UserResponse(id, "Rafael", "rafa@email.com");
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest request) {
        return new UserResponse(request.id, request.name, request.email);
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") int id,
                             @RequestBody UserRequest request) {
        return "Atualizando user " + id + " para name=" + request.name;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        return "Removendo user " + id;
    }
}