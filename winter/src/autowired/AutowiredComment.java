package autowired;

// Importa o ElementType.
// Ele é usado para dizer em que lugar a anotação pode ser aplicada.
// Exemplo: em campo, método, classe, parâmetro etc.
import java.lang.annotation.ElementType;

// Importa o Retention.
// Essa anotação é usada para configurar até quando a anotação vai existir.
import java.lang.annotation.Retention;

// Importa o RetentionPolicy.
// Ele define a política de retenção da anotação.
// Ou seja: se ela existe só no código-fonte, no .class, ou também em tempo de execução.
import java.lang.annotation.RetentionPolicy;

// Importa o Target.
// Essa anotação é usada para definir onde nossa anotação personalizada pode ser usada.
import java.lang.annotation.Target;

// Diz que a anotação @Autowired deve ficar disponível também em tempo de execução.
// Isso é importante porque, se quisermos usar Reflection para procurar campos anotados,
// o Java precisa conseguir "enxergar" essa anotação enquanto o programa está rodando.
//
// Se fosse SOURCE, ela existiria só no código e sumiria na compilação.
// Se fosse CLASS, ela iria para o .class, mas não estaria disponível em runtime.
// Como queremos algo parecido com o Spring, usamos RUNTIME.
@Retention(RetentionPolicy.RUNTIME)

// Diz que essa anotação só pode ser usada em CAMPOS.
// Exemplo:
//
// @Autowired
// private UserRepository repository;
//
// Aqui, "repository" é um campo/atributo da classe.
// Então ElementType.FIELD significa: só pode colocar @Autowired em atributos.
@Target(ElementType.FIELD)

// Declara uma anotação personalizada chamada Autowired.
// O uso de "@interface" é a sintaxe do Java para criar anotações.
//
// Isso não é uma interface comum.
// Apesar do nome parecer interface, "@interface" serve especificamente para anotações.
//
// Depois disso, poderemos usar:
//
// @Autowired
// private UserRepository repository;
public @interface AutowiredComment {

// O corpo está vazio porque essa anotação não recebe parâmetros.
// Existem anotações que recebem valores, por exemplo:
//
// public @interface MinhaAnotacao {
//     String value();
// }
//
// Mas aqui não precisamos disso.
// Só queremos marcar o campo com um "selo" dizendo:
// "esse campo deve ser injetado automaticamente".
}