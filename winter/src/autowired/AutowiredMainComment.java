package autowired;

// Importa a classe HashMap.
// HashMap é uma estrutura de dados que guarda pares de chave e valor.
// Aqui vamos usar para guardar objetos no "container".
import java.util.HashMap;

// Importa a interface Map.
// Map representa a ideia de chave -> valor.
// No nosso caso:
// chave = tipo da classe
// valor = objeto daquela classe
import java.util.Map;

// Importa de forma estática o método injectDependencies da classe AutowiredReflection.
// Isso permite chamar o método diretamente como:
//
// injectDependencies(container);
//
// em vez de:
//
// AutowiredReflection.injectDependencies(container);
import static autowired.AutowiredReflection.injectDependencies;

public class AutowiredMainComment {

    public static void main(String[] args) throws Exception {

        // Cria um Map que vai funcionar como um "container" bem simples.
        //
        // Pense no container como um lugar onde guardamos os objetos da aplicação.
        // A ideia é:
        // - a chave será a classe
        // - o valor será a instância daquela classe
        //
        // Exemplo:
        // UserRepository.class -> objeto UserRepository
        // UserService.class    -> objeto UserService
        Map<Class<?>, Object> container = new HashMap<>();

        // Cria manualmente um objeto da classe UserRepository.
        // Esse objeto representa a dependência que será usada por outra classe.
        UserRepository userRepository = new UserRepository();

        // Cria manualmente um objeto da classe UserService.
        // Essa classe provavelmente tem um campo anotado com @Autowired,
        // algo como:
        //
        // @Autowired
        // private UserRepository userRepository;
        UserService userService = new UserService();

        // Coloca o objeto userRepository dentro do container.
        //
        // Chave: UserRepository.class
        // Valor: a instância criada acima
        //
        // Assim, depois podemos procurar no container:
        // "me dá o objeto do tipo UserRepository"
        container.put(UserRepository.class, userRepository);

        // Coloca o objeto userService dentro do container também.
        //
        // Agora o container tem pelo menos dois objetos registrados:
        // - UserRepository
        // - UserService
        container.put(UserService.class, userService);

        // Chama o método responsável por fazer a injeção das dependências.
        //
        // Esse método deve percorrer os objetos do container,
        // encontrar campos com @Autowired
        // e preencher esses campos automaticamente.
        //
        // Exemplo do que ele deve fazer por trás dos panos:
        // - encontrar o campo userRepository dentro de UserService
        // - ver que ele está anotado com @Autowired
        // - procurar no container um objeto do tipo UserRepository
        // - colocar esse objeto dentro do campo
        injectDependencies(container);

        // Chama o método execute() do userService.
        //
        // Nesse momento, esperamos que o campo userRepository dentro de userService
        // já tenha sido preenchido pela injeção.
        //
        // Se tudo deu certo, userService.execute() vai funcionar normalmente.
        // Se a injeção não tivesse acontecido, poderia dar NullPointerException.
        userService.execute();
    }
}
