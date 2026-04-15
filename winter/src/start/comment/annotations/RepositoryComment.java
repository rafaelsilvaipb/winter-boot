package start.comment.annotations;

// Importa o enum ElementType.
// Esse enum é usado junto com @Target para dizer
// em que tipo de elemento a anotação pode ser usada.
//
// Exemplos de ElementType:
// - TYPE   -> classes, interfaces, enums
// - FIELD  -> atributos
// - METHOD -> métodos
//
// Neste caso, vamos usar TYPE,
// porque @Repository deve ser colocada em cima de classes ou interfaces.
import start.annotations.Component;

import java.lang.annotation.ElementType;

// Importa a anotação Retention.
// Ela serve para dizer por quanto tempo a anotação deve existir.
//
// Em outras palavras:
// queremos definir se a anotação existe:
// - só no código-fonte
// - até o .class
// - ou também em tempo de execução
import java.lang.annotation.Retention;

// Importa o enum RetentionPolicy.
// Ele traz as opções possíveis para a retenção da anotação.
//
// As principais são:
// - SOURCE
// - CLASS
// - RUNTIME
//
// Como queremos que o framework leia essa anotação usando reflection,
// precisamos usar RUNTIME.
import java.lang.annotation.RetentionPolicy;

// Importa a anotação Target.
// Ela serve para restringir onde a anotação pode ser usada.
//
// Sem @Target, a anotação poderia ser usada em vários lugares
// sem muito controle.
//
// Com @Target, dizemos claramente:
// "essa anotação só pode ser usada em determinados tipos de elementos".
import java.lang.annotation.Target;

// Diz que a anotação @Repository deve continuar existindo
// em tempo de execução.
//
// Isso é essencial porque o seu mini container provavelmente vai fazer algo como:
//
// clazz.isAnnotationPresent(Repository.class)
//
// ou vai analisar as anotações da classe em runtime.
//
// Se não fosse RUNTIME, a anotação não estaria disponível
// quando o programa estivesse rodando.
@Retention(RetentionPolicy.RUNTIME)

// Diz que a anotação @Repository só pode ser usada em TYPE.
//
// TYPE significa:
// - classes
// - interfaces
// - enums
//
// Isso faz sentido porque @Repository deve marcar
// um tipo do sistema, como:
//
// @Repository
// public class UserRepositoryImpl {}
//
// ou, dependendo da arquitetura:
//
// @Repository
// public interface UserRepository {}
@Target(ElementType.TYPE)

// Aqui está o ponto mais interessante.
//
// Estamos anotando @Repository com @Component.
//
// Isso significa que @Repository é uma anotação "especializada"
// que carrega o significado de @Component.
//
// Em outras palavras:
//
// toda classe anotada com @Repository
// também deve ser tratada como um @Component.
//
// Esse é o mesmo conceito usado no Spring:
// @Service, @Repository e @Controller
// são variações mais semânticas de @Component.
//
// Ou seja, além de dizer:
// "isso é um componente gerenciado pelo container"
//
// ela também diz:
// "esse componente pertence à camada de repositório"
@Component

// Declara uma anotação personalizada chamada Repository.
//
// O @interface é a sintaxe usada em Java para criar anotações.
//
// Isso permite que você use essa anotação assim:
//
// @Repository
// public class UserRepositoryImpl implements UserRepository {
// }
//
// Essa anotação ajuda o container a identificar:
// - que a classe pode ser gerenciada
// - e que ela representa a camada de acesso a dados
public @interface RepositoryComment {
}