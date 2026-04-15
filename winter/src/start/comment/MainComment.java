package start.comment;

// Importa a classe concreta UserRepositoryImpl.
// Ela está no pacote start.impl.
// Essa será a implementação real do repositório que o sistema vai instanciar.
import start.UserService;
import start.impl.UserRepositoryImpl;

// Importa a interface Map.
// Vamos usar um Map para funcionar como um "container de objetos".
// Esse container vai guardar as instâncias criadas pelo sistema.
//
// A ideia é:
// chave   -> tipo da classe ou interface
// valor   -> objeto instanciado
//
// Exemplo:
// UserService.class -> instância de UserService
// UserRepository.class -> instância de UserRepositoryImpl
import java.util.Map;

// Importa HashMap.
// HashMap é uma implementação da interface Map.
// Ele será usado para guardar e recuperar as instâncias rapidamente.
import java.util.HashMap;

// Importa estaticamente o método injectDependencies da classe StartReflection.
// Isso permite chamar o método diretamente como:
//
// injectDependencies(container);
//
// em vez de escrever:
//
// StartReflection.injectDependencies(container);
import static start.StartReflection.injectDependencies;

// Importa estaticamente o método isComponent da classe StartReflection.
// Esse método provavelmente verifica se uma classe pode ser gerenciada pelo container,
// por exemplo, se ela está anotada com @Component, @Service ou @Repository.
import static start.StartReflection.isComponent;

// Declara a classe principal da aplicação.
// Essa classe vai inicializar o container, criar os objetos,
// injetar as dependências e executar o fluxo principal.
public class MainComment {

    // Método principal: ponto de entrada da aplicação Java.
    //
    // "throws Exception" foi usado para simplificar o código,
    // evitando vários try/catch neste exemplo didático.
    public static void main(String[] args) throws Exception {

        // Cria um Map que funcionará como um container de dependências.
        //
        // Pense nele como uma "caixa" onde vamos guardar os objetos prontos.
        //
        // A chave é o tipo da classe/interface.
        // O valor é a instância correspondente.
        //
        // Exemplo futuro:
        // UserService.class -> objeto UserService
        // UserRepository.class -> objeto UserRepositoryImpl
        Map<Class<?>, Object> container = new HashMap<>();

        // Cria um array de classes candidatas a serem gerenciadas pelo container.
        //
        // Aqui estamos listando manualmente as classes que queremos analisar.
        //
        // Esse tipo de abordagem é comum em exemplos iniciais,
        // antes de implementar um scanner automático de pacotes como o Spring faz.
        //
        // Nesse caso:
        // - UserService.class representa a camada de serviço
        // - UserRepositoryImpl.class representa a implementação do repositório
        Class<?>[] classes = {
                UserService.class,
                UserRepositoryImpl.class
        };

        // Percorre cada classe do array.
        //
        // O objetivo aqui é:
        // 1. verificar se a classe é um componente gerenciável
        // 2. criar uma instância dela
        // 3. registrar essa instância no container
        for (Class<?> clazz : classes) {

            // Verifica se a classe pode ser tratada como componente.
            //
            // Exemplo:
            // - se ela tiver @Component, retorna true
            // - se ela tiver @Service, talvez também retorne true
            // - se ela tiver @Repository, talvez também retorne true
            //
            // Isso simula a lógica de frameworks que só gerenciam classes anotadas.
            if (isComponent(clazz)) {

                // Cria uma nova instância da classe usando reflection.
                //
                // clazz.getDeclaredConstructor()
                // pega o construtor sem argumentos da classe.
                //
                // .newInstance()
                // chama esse construtor e cria o objeto.
                //
                // Exemplo:
                // se clazz for UserService.class,
                // isso equivale a:
                // new UserService()
                //
                // Reflection permite fazer isso dinamicamente,
                // sem escrever o tipo da classe diretamente no código.
                Object instance = clazz.getDeclaredConstructor().newInstance();

                // Registra a instância criada no container usando a própria classe concreta como chave.
                //
                // Exemplo:
                // UserService.class -> instância de UserService
                // UserRepositoryImpl.class -> instância de UserRepositoryImpl
                //
                // Isso permite depois recuperar a instância pelo tipo concreto.
                container.put(clazz, instance);

                // Percorre todas as interfaces implementadas pela classe atual.
                //
                // Exemplo:
                // se UserRepositoryImpl implementa UserRepository,
                // então clazz.getInterfaces() retornará UserRepository.class
                //
                // Isso é importante porque, no dia a dia,
                // muitas dependências são declaradas por interface,
                // e não pela classe concreta.
                for (Class<?> interfaceType : clazz.getInterfaces()) {

                    // Registra a mesma instância no container,
                    // mas agora usando a interface como chave.
                    //
                    // Exemplo:
                    // UserRepository.class -> instância de UserRepositoryImpl
                    //
                    // Isso permite que, quando alguém pedir:
                    // @Autowired private UserRepository userRepository;
                    //
                    // o container consiga encontrar a implementação correta.
                    container.put(interfaceType, instance);
                }
            }
        }

        // Depois que todas as instâncias foram criadas e registradas no container,
        // chamamos o método que injeta as dependências.
        //
        // Esse método provavelmente faz algo como:
        // - percorre os objetos do container
        // - procura campos anotados com @Autowired
        // - encontra a dependência correspondente no container
        // - injeta no campo usando reflection
        //
        // Exemplo:
        // em UserService:
        // @Autowired
        // private UserRepository userRepository;
        //
        // esse método vai achar a instância de UserRepositoryImpl
        // e colocá-la dentro do campo userRepository.
        injectDependencies(container);

        // Recupera do container a instância de UserService.
        //
        // Como o container guarda Object como valor,
        // precisamos fazer cast para UserService.
        //
        // Isso equivale a dizer:
        // "Eu sei que esse objeto recuperado é um UserService."
        UserService userService = (UserService) container.get(UserService.class);

        // Executa o método principal do serviço.
        //
        // Nesse ponto, espera-se que as dependências já tenham sido injetadas.
        //
        // Então, se UserService depende de UserRepository,
        // esse campo já deve estar preenchido.
        //
        // Na prática, isso mostra que o mini container funcionou.
        userService.execute();
    }
}