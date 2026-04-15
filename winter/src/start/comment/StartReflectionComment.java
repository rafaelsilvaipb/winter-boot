package start.comment;

// Importa a anotação @Autowired.
// Essa anotação será usada para marcar os campos que devem receber
// uma dependência automaticamente.
//
// Exemplo:
// @Autowired
// private UserRepository userRepository;
//
// Quando o sistema encontrar um campo com essa anotação,
// ele vai tentar injetar o objeto correspondente.
import start.annotations.Autowired;

// Importa a anotação @Component.
// Essa anotação é a base para dizer:
// "essa classe pode ser gerenciada pelo container".
//
// Além dela, podem existir outras anotações, como @Service e @Repository,
// que também são tratadas como componentes.
import start.annotations.Component;

// Importa a classe Annotation.
// Vamos usar isso para inspecionar anotações em tempo de execução.
// Isso é útil no método isComponent, onde vamos olhar
// as anotações da classe e verificar se alguma delas carrega @Component.
import java.lang.annotation.Annotation;

// Importa a classe Field.
// Field representa um atributo/campo de uma classe em Reflection.
//
// Exemplo:
// se uma classe tiver:
// private UserRepository userRepository;
//
// esse atributo pode ser representado por um objeto Field.
import java.lang.reflect.Field;

// Importa a interface Map.
// Vamos usar Map<Class<?>, Object> como container.
//
// A ideia é:
// - a chave é o tipo (classe ou interface)
// - o valor é a instância criada
//
// Exemplo:
// UserService.class -> instância de UserService
// UserRepository.class -> instância de UserRepositoryImpl
import java.util.Map;

// Declara a classe StartReflection.
// Essa classe concentra lógicas baseadas em reflection.
//
// Aqui estão duas responsabilidades bem importantes:
//
// 1. descobrir se uma classe é um componente
// 2. injetar dependências em campos anotados com @Autowired
//
// Ou seja: essa classe é uma parte central do mini framework.
public class StartReflectionComment {

    // Método público e estático que injeta dependências nos objetos do container.
    //
    // "public" -> pode ser chamado de fora da classe
    // "static" -> não precisa criar instância de StartReflection para usar
    //
    // Ele recebe o container, que guarda os objetos já criados.
    //
    // A ideia desse método é:
    // - percorrer todos os objetos do container
    // - olhar os campos de cada objeto
    // - verificar se algum campo tem @Autowired
    // - encontrar a dependência correta
    // - colocar essa dependência dentro do campo
    public static void injectDependencies(Map<Class<?>, Object> container) throws Exception {

        // Percorre todos os objetos armazenados no container.
        //
        // container.values() devolve todas as instâncias.
        //
        // Exemplo:
        // uma instância de UserService
        // uma instância de UserRepositoryImpl
        for (Object object : container.values()) {

            // Pega a classe real do objeto atual.
            //
            // Exemplo:
            // se object for uma instância de UserService,
            // clazz será UserService.class
            Class<?> clazz = object.getClass();

            // Percorre todos os campos declarados na classe.
            //
            // getDeclaredFields() retorna os atributos da própria classe,
            // inclusive private.
            //
            // Exemplo:
            // se a classe tiver:
            // @Autowired
            // private UserRepository userRepository;
            //
            // esse campo aparecerá aqui.
            for (Field field : clazz.getDeclaredFields()) {

                // Verifica se o campo atual está anotado com @Autowired.
                //
                // Exemplo:
                // private UserRepository userRepository;
                //
                // se esse campo tiver @Autowired, o if entra.
                if (field.isAnnotationPresent(Autowired.class)) {

                    // Procura no container uma dependência compatível com o tipo do campo.
                    //
                    // field.getType() devolve o tipo do campo.
                    //
                    // Exemplo:
                    // se o campo for:
                    // private UserRepository userRepository;
                    //
                    // field.getType() será UserRepository.class
                    //
                    // O método findDependency vai tentar achar no container
                    // uma instância que possa ser usada ali.
                    Object dependency = findDependency(container, field.getType());

                    // Se não encontrou dependência, lança erro.
                    //
                    // Isso é importante porque um campo marcado com @Autowired
                    // está dizendo:
                    // "eu preciso receber essa dependência"
                    //
                    // Se ela não existir no container, a aplicação não consegue funcionar direito.
                    if (dependency == null) {

                        // Lança uma RuntimeException com uma mensagem explicando
                        // qual dependência não foi encontrada.
                        //
                        // field.getType().getName() pega o nome completo da classe/interface.
                        throw new RuntimeException(
                                "Dependência não encontrada para: " + field.getType().getName()
                        );
                    }

                    // Permite acesso ao campo mesmo que ele seja private.
                    //
                    // Normalmente, campos private não podem ser alterados diretamente via reflection.
                    // Com setAccessible(true), estamos dizendo:
                    // "quero ignorar essa restrição e mexer no campo mesmo assim"
                    field.setAccessible(true);

                    // Coloca a dependência encontrada dentro do campo do objeto.
                    //
                    // field.set(objeto, valor)
                    //
                    // Exemplo mental:
                    // se object for UserService
                    // e field for userRepository
                    // e dependency for UserRepositoryImpl
                    //
                    // isso é parecido com fazer:
                    // userService.userRepository = userRepositoryImpl;
                    field.set(object, dependency);
                }
            }
        }
    }

    // Método privado e estático que tenta encontrar uma dependência compatível no container.
    //
    // "private" porque ele é um detalhe interno dessa classe.
    // "static" porque é um método utilitário.
    //
    // Ele recebe:
    // - o container
    // - o tipo do campo que precisa ser preenchido
    //
    // Exemplo:
    // se um campo for do tipo UserRepository,
    // o fieldType será UserRepository.class
    private static Object findDependency(Map<Class<?>, Object> container, Class<?> fieldType) {

        // Primeiro tenta buscar diretamente no container pela chave exata.
        //
        // Exemplo:
        // se fieldType for UserService.class
        // e o container tiver essa chave, ele encontra imediatamente.
        Object directDependency = container.get(fieldType);

        // Se encontrou diretamente, já devolve.
        //
        // Isso é o caminho mais simples e rápido.
        if (directDependency != null) {
            return directDependency;
        }

        // Se não achou diretamente, percorre todas as entradas do container.
        //
        // Isso é útil quando o campo é uma interface,
        // mas o container guarda uma implementação concreta.
        //
        // Exemplo:
        // campo: UserRepository
        // implementação registrada: UserRepositoryImpl
        for (Map.Entry<Class<?>, Object> entry : container.entrySet()) {

            // Pega a classe que foi registrada como chave no container.
            //
            // Exemplo:
            // UserRepositoryImpl.class
            // UserRepository.class
            // UserService.class
            Class<?> registeredClass = entry.getKey();

            // Verifica se o tipo do campo é compatível com a classe registrada.
            //
            // isAssignableFrom responde algo como:
            // "um objeto de registeredClass pode ser usado onde fieldType é esperado?"
            //
            // Exemplo:
            // fieldType = UserRepository.class
            // registeredClass = UserRepositoryImpl.class
            //
            // Resultado: true
            //
            // Porque UserRepositoryImpl implementa UserRepository.
            if (fieldType.isAssignableFrom(registeredClass)) {

                // Se for compatível, devolve a instância correspondente.
                return entry.getValue();
            }
        }

        // Se não encontrou nenhuma dependência compatível, devolve null.
        //
        // O método que chamou isso decidirá o que fazer.
        // No caso do injectDependencies, ele lança exceção.
        return null;
    }

    // Método público e estático que verifica se uma classe deve ser tratada como componente.
    //
    // A ideia aqui é permitir que o container reconheça:
    // - classes com @Component diretamente
    // - classes com anotações que carregam @Component,
    //   como @Service ou @Repository
    //
    // Isso deixa o comportamento mais parecido com o Spring.
    public static boolean isComponent(Class<?> clazz) {

        // Primeiro verifica se a própria classe tem @Component diretamente.
        //
        // Exemplo:
        // @Component
        // public class MyClass {}
        //
        // Se tiver, retorna true imediatamente.
        if (clazz.isAnnotationPresent(Component.class)) {
            return true;
        }

        // Se não tem @Component diretamente,
        // percorre todas as anotações presentes na classe.
        //
        // Exemplo:
        // se a classe tiver @Service,
        // esse loop vai encontrar essa anotação.
        for (Annotation annotation : clazz.getAnnotations()) {

            // Pega o tipo da anotação atual.
            //
            // Exemplo:
            // se annotation for @Service,
            // annotationType será Service.class
            Class<? extends Annotation> annotationType = annotation.annotationType();

            // Verifica se o tipo da anotação atual tem @Component.
            //
            // Exemplo:
            // se @Service estiver definido assim:
            //
            // @Component
            // public @interface Service {}
            //
            // então annotationType.isAnnotationPresent(Component.class)
            // será true.
            //
            // Isso é o conceito de meta-anotação:
            // uma anotação anotada com outra anotação.
            if (annotationType.isAnnotationPresent(Component.class)) {

                // Se encontrou alguma anotação que carrega @Component,
                // a classe também deve ser tratada como componente.
                return true;
            }
        }

        // Se não encontrou nem @Component direto
        // nem uma anotação que carregue @Component,
        // então essa classe não é um componente.
        return false;
    }
}