# Winter Boot

Esse projeto tem o objetivo de criar um Mini Spring Boot. Vou recriar as anotações mais usadas para fins ditáticos.
Usando somente o java puro.

- [x] 1 - @Autowired
- [X] 2 - @Component, @Service, @Repository
- [x] 3 - @RestControler, @RequestMapping, (GET, POST, PUT, DELETE)
- [x] 4 - @RequestBody, @PathVariable, @RequestParam
- [ ] 5 - @WinterBootApplication, Scanner automático de classes
- [ ] 6 - Winter Data - Repository o mais pŕoximo do real


## 1 - Autowired
Na prmeira parte, temos a criação da anotação @Autowired, que usa o padrão de pradrão de projetos Singleton, 
assim não precisamos usar o new toda vez, só pedir o objeto já criado.

```
@Autowired
private UserRepository userRepository;
```

## 2 - @Component, @Service, @Repository
Na segunda parte, temos a criação das anotações @Component, @Service e @Repository. 
Na prática, o @Service e @Repository são um @Component, mantive esse padrão.

@Service
```
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void execute() {
        System.out.println(userRepository.findUser());
    }
}
```

@Repository - Aqui o @Repository está na implementação, que é o certo. 
Como o spring data cria a implementação em runtime, nos acostumamos colocar na interface, 
mas não é o correto quando temos uma implementação escrita
```
public interface UserRepository {
String findUser();
}
```
```
@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public String findUser() {
        return "Usuário encontrado no banco";
    }
}
```

## 3 - @RestControler, @RequestMapping, (GET, POST, PUT, DELETE)
Agora temos o nosso controller funcional com get, post, put e delete.

@Controller
```
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/find")
    public String findUser() {
        return userService.findUser();
    }
}
```

Como testar

Suba o Main e teste:


GET
```
curl -X GET http://localhost:8080/users
```

GET chamando service/repository
```
curl -X GET http://localhost:8080/users/find
```

POST
```
curl -X POST http://localhost:8080/users
```

PUT
```
curl -X PUT http://localhost:8080/users
```

PUT
```
curl -X DELETE http://localhost:8080/users
```

## 4 - @RequestBody, @PathVariable, @RequestParam
Agora sim, podemos criar o post via RequestBody, passaram Path e Params

@Controller
```
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
```

Como testar
Sessão 3.