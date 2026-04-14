# Winter Boot

Esse projeto tem o objetivo de criar um Mini Spring Boot. Vou recriar as anotações mais usadas para fins ditáticos.
Usando somente o java puro.

- [x] 1 - @Autowired
- [ ] 2 - @Component, @Service, @Repository
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