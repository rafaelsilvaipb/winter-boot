package start.comment.annotations;// Importa a anotação Retention.
// Essa anotação é usada para dizer "até quando" a nossa anotação personalizada vai existir.
//
// Em outras palavras:
// quando criamos uma anotação como @Component,
// precisamos informar se ela só existe no código-fonte,
// se vai até o .class,
// ou se também vai estar disponível em tempo de execução.
import java.lang.annotation.Retention;

// Importa o enum RetentionPolicy.
// Esse enum traz as opções possíveis de retenção da anotação.
//
// As principais são:
// - SOURCE   -> existe só no código fonte
// - CLASS    -> vai para o .class, mas não fica disponível em runtime
// - RUNTIME  -> fica disponível em tempo de execução
//
// Como queremos ler @Component com reflection,
// precisamos usar RUNTIME.
import java.lang.annotation.RetentionPolicy;

// Aplica a anotação @Retention na nossa anotação personalizada.
//
// Isso significa:
// "A anotação @Component deve continuar existindo em tempo de execução."
//
// Esse detalhe é essencial para frameworks e mini frameworks.
// Sem isso, quando o programa rodar e tentar usar reflection para descobrir
// se uma classe tem @Component, ele não vai encontrar nada.
//
// Exemplo:
// se você fizer:
// clazz.isAnnotationPresent(Component.class)
//
// isso só funciona porque a anotação foi marcada com RUNTIME.
@Retention(RetentionPolicy.RUNTIME)

// Declara uma anotação personalizada chamada Component.
//
// O símbolo @interface não significa "interface normal" do Java.
// Ele significa:
// "estou criando uma anotação customizada".
//
// Ou seja, daqui em diante você poderá escrever algo como:
//
// @Component
// public class UserService {}
//
// Essa anotação pode ser usada para marcar classes que o seu container
// deve gerenciar.
//
// Em um mini Spring, ela serve para dizer:
// "essa classe pode virar um objeto dentro do container".
public @interface ComponentComment {
}