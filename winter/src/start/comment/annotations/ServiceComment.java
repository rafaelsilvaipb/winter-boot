package start.comment.annotations;

// Importa o enum ElementType.
// Esse enum é usado junto com @Target para definir
// em que tipo de elemento uma anotação pode ser usada.
//
// Alguns exemplos:
// - TYPE   -> classes, interfaces, enums
// - FIELD  -> atributos
// - METHOD -> métodos
//
// Neste caso, vamos usar TYPE,
// porque @Service deve ser colocada em cima de uma classe
// ou outro tipo, e não em um atributo ou método.
import start.annotations.Component;

import java.lang.annotation.ElementType;

// Importa a anotação Retention.
// Ela serve para definir por quanto tempo uma anotação existe.
//
// Em outras palavras:
// queremos dizer se a anotação existe:
// - só no código-fonte
// - até o .class
// - ou também enquanto o programa está rodando
import java.lang.annotation.Retention;

// Importa o enum RetentionPolicy.
// Esse enum traz as opções de retenção da anotação.
//
// As principais são:
// - SOURCE  -> só existe no código-fonte
// - CLASS   -> vai para o .class, mas não fica disponível em runtime
// - RUNTIME -> fica disponível em tempo de execução
//
// Como queremos que o nosso mini framework enxergue @Service usando reflection,
// precisamos usar RUNTIME.
import java.lang.annotation.RetentionPolicy;

// Importa a anotação Target.
// Ela serve para restringir onde a anotação pode ser usada.
//
// Sem @Target, a anotação poderia ser usada em vários lugares,
// até em locais que não fariam sentido.
//
// Com @Target, deixamos claro:
// "essa anotação só pode ser usada em determinados tipos de elemento".
import java.lang.annotation.Target;

// Diz que a anotação @Service deve continuar existindo
// em tempo de execução.
//
// Isso é essencial para que o programa consiga usar reflection
// para descobrir se uma classe está anotada com @Service.
//
// Exemplo:
// clazz.isAnnotationPresent(Service.class)
//
// Se não fosse RUNTIME, o programa rodando não conseguiria enxergar essa anotação.
@Retention(RetentionPolicy.RUNTIME)

// Diz que @Service só pode ser usada em TYPE.
//
// TYPE significa:
// - classes
// - interfaces
// - enums
//
// Isso faz sentido, porque @Service normalmente é usada
// para marcar classes de serviço, por exemplo:
//
// @Service
// public class UserService {}
@Target(ElementType.TYPE)

// Aqui está o ponto mais importante desse código.
//
// Estamos anotando @Service com @Component.
//
// Isso significa que @Service é uma anotação mais específica,
// mas que também carrega o significado de @Component.
//
// Em outras palavras:
// toda classe anotada com @Service
// também deve ser tratada como um componente.
//
// Esse é o mesmo conceito usado no Spring:
// @Service, @Repository e @Controller
// são especializações semânticas de @Component.
//
// Então @Service diz duas coisas ao mesmo tempo:
// 1. essa classe é um componente gerenciado pelo container
// 2. essa classe representa a camada de serviço
@Component

// Declara uma anotação personalizada chamada Service.
//
// O @interface é a sintaxe usada em Java para criar anotações customizadas.
//
// Isso permite escrever algo assim:
//
// @Service
// public class UserService {
// }
//
// Essa anotação serve para marcar classes que fazem parte
// da camada de serviço da aplicação,
// e que também devem ser gerenciadas pelo container.
public @interface ServiceComment {
}