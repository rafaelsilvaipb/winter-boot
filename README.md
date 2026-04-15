# Winter Boot

Esse projeto tem o objetivo de criar um Mini Spring Boot. Vou recriar as anotações mais usadas para fins ditáticos.
Usando somente o java puro.

- [x] 1 - @Autowired
- [X] 2 - @Component, @Service, @Repository
- [ ] 3 - @RestControler, @RequestMapping, (GET, POST, PUT, DELETE)
- [ ] 4 - @WinterBootApplication, Scanner automático de classes
- [ ] 5 - Winter Data - Repository o mais pŕoximo do real


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