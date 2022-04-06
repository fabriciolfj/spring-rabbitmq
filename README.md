# Complete RabbitMQ Guide With Spring Boot

## RabbitMQ
- Em poucas palavras, RabbitMQ é um software open source de mensageria. Fornece uma forma comunicação assíncrona de dados entre processos, aplicações ou servidores. É um dos brokers de mensagens mais utilizados e implementa o protocolo AMQP — Advanced Message Queueing Protocol.

### Como funciona
- Como toda comunicação, precisamos de um produtor, de uma mensagem e de um receptor. No meio do bolo fica o RabbitMQ, que seria o lugar onde fica a mensagem esperando pelo receptor.
- Um conceito importante para evitar dores de cabeça lá na frente é que não enviamos a mensagem para uma fila diretamente e sim para um exchange, que será o encarregado de encaminhar para a fila correta. E a mágica para entender o Rabbit está em entender como funciona esse encaminhamento, esse routing.
Antes de explicar melhor o que o exchange faz, precisamos esclarecer três conceitos:

  - Binding: É a ligação entre uma fila e um exchange
  - Binding key: É uma chave específica da ligação entre a fila e o exchange
  - Routing key: É uma chave enviada junto a mensagem que o exchange usa para decidir para que fila (ou filas) vai rotear uma mensagem.

#### Exchanges
- Como já comentei, as mensagens não são publicadas diretamente numa fila. O produtor (producer) envia a mensagem para o exchange, junto com o routing key (não é obrigatório) e o exchange encaminha para as filas em função da configuração.

#### Temos quatro tipos de exchange:
- Direct: O Direct Exchange encaminha as mensagens procurando por um binding key igual ao routing key fornecido. Vale salientar que se o exchange não consegue encaminhar a mensagem para uma fila, ela será descartada. As mensagens são encaminhadas em função do routing key.
- Topic: Topic Exchange funciona de uma forma parecida ao Direct, porém o binding key é um tipo de “expressão regular” aplicada sobre o routing key. Para ser mais fácil de entender, poderíamos fazer uma analogia com as tags do post. O routing key da mensagem seria a lista de tags separadas por pontos (por exemplo “kotlin.rabbitmq.tecnologia”). Os bindings poderiam filtrar por tipo, e cada fila só receberia aqueles posts que acharem interessantes, por exemplo “#.kotlin.#” ou “#.tecnologia.#”.
- Fanout: O Fanout Exchange encaminha para todas as filas com binding nele, desconsiderando routing key. Toda mensagem é encaminhada para as filas
- Headers: O Headers Exchange usa os valores do header da mensagem para fazer o encaminhamento, ignorando o routing key. É parecido ao Topic mas permite um controle mais fino. A mensagem pode dizer se os headers precisam ser iguais à binging key ou se só um header igual já é motivo suficiente para encaminhar. Sendo sincero, nunca vi usando este tipo de exchange.
