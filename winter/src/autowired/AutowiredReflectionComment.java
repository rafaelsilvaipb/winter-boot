package autowired;

// Importa a classe Field.
// Field representa um campo/atributo de uma classe em Java.
// Exemplo: se uma classe tem "private UserRepository userRepository;",
// esse atributo pode ser representado por um Field.
import java.lang.reflect.Field;

// Importa a interface Map.
// Vamos usar Map para receber o "container" que guarda os objetos da aplicação.
import java.util.Map;

public class AutowiredReflectionComment {

    // Declara um método estático chamado injectDependencies.
    //
    // "static" significa que podemos chamar esse método sem criar um objeto
    // da classe AutowiredReflection.
    //
    // Ele recebe um Map<Class<?>, Object> chamado container.
    // Esse Map funciona como um mini container de dependências.
    //
    // Exemplo de conteúdo do container:
    // UserRepository.class -> instância de UserRepository
    // UserService.class    -> instância de UserService
    //
    // "throws Exception" foi usado para simplificar o exemplo.
    // Assim não precisamos tratar várias exceções com try/catch aqui.
    static void injectDependencies(Map<Class<?>, Object> container) throws Exception {

        // Percorre todos os objetos armazenados no container.
        //
        // container.values() devolve só os valores do Map,
        // ou seja, as instâncias reais dos objetos.
        //
        // Exemplo:
        // - objeto UserRepository
        // - objeto UserService
        for (Object object : container.values()) {

            // Pega a classe real do objeto atual.
            //
            // Exemplo:
            // se object for um UserService, clazz será UserService.class
            // se object for um UserRepository, clazz será UserRepository.class
            Class<?> clazz = object.getClass();

            // Percorre todos os campos declarados nessa classe.
            //
            // getDeclaredFields() devolve os atributos da classe,
            // inclusive os privados.
            //
            // Exemplo em UserService:
            // private UserRepository userRepository;
            //
            // Esse atributo aparecerá aqui como um Field.
            for (Field field : clazz.getDeclaredFields()) {

                // Verifica se o campo atual está anotado com @Autowired.
                //
                // isAnnotationPresent(...) responde true ou false.
                //
                // Se o campo tiver:
                // @Autowired
                // private UserRepository userRepository;
                //
                // então essa condição será verdadeira.
                if (field.isAnnotationPresent(Autowired.class)) {

                    // Procura no container uma dependência com o mesmo tipo do campo.
                    //
                    // field.getType() devolve o tipo do campo.
                    //
                    // Exemplo:
                    // se o campo for "private UserRepository userRepository",
                    // field.getType() será UserRepository.class
                    //
                    // Então o código faz:
                    // container.get(UserRepository.class)
                    //
                    // e tenta achar a instância correspondente.
                    Object dependency = container.get(field.getType());

                    // Verifica se a dependência não foi encontrada no container.
                    //
                    // Se vier null, significa que existe um campo anotado com @Autowired,
                    // mas ninguém registrou no container um objeto daquele tipo.
                    if (dependency == null) {

                        // Lança um erro em tempo de execução dizendo que a dependência
                        // não foi encontrada.
                        //
                        // field.getType().getName() pega o nome completo da classe.
                        //
                        // Exemplo de mensagem:
                        // Dependência não encontrada para: autowired.UserRepository
                        throw new RuntimeException(
                                "Dependência não encontrada para: " + field.getType().getName()
                        );
                    }

                    // Permite acessar e modificar o campo mesmo que ele seja private.
                    //
                    // Normalmente, Java não deixa mexer diretamente em campos privados
                    // fora da classe.
                    //
                    // Com setAccessible(true), estamos dizendo:
                    // "quero ignorar temporariamente essa restrição".
                    //
                    // Reflection usa muito isso.
                    field.setAccessible(true);

                    // Injeta a dependência dentro do objeto.
                    //
                    // field.set(objeto, valor)
                    //
                    // Aqui:
                    // - object = objeto que vai receber a dependência
                    // - dependency = objeto que será colocado no campo
                    //
                    // Exemplo prático:
                    // se object for userService
                    // e dependency for userRepository
                    //
                    // esse comando faz algo parecido com:
                    // userService.userRepository = userRepository;
                    //
                    // Só que usando Reflection.
                    field.set(object, dependency);
                }
            }
        }
    }
}