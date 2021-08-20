package com.orange

import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import kotlin.random.Random

@Singleton
class FreteGrpcServer : FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FreteGrpcServer::class.java)

    override fun calculaFrete(request: CalculaFreteRequest?, responseObserver: StreamObserver<CalculaFreteResponse>?) {

        val info = logger.info("Calculando frete para request: $request")

        val cep = request?.cep
        if (cep == null || cep.isBlank()) {
            val erro = Status.INVALID_ARGUMENT
                .withDescription("cep não deve ser nulo")
                .asRuntimeException()

            responseObserver?.onError(erro)
        }

        if (!cep!!.matches("[0-9]{5}-[0-9]{3}".toRegex())) {
            val erro = Status.INVALID_ARGUMENT
                .withDescription("cep inválido")
                .augmentDescription("Formato esperado: 00000-000")
                .asRuntimeException()

            responseObserver?.onError(erro)
        }

        // Simular verificação de segurança
        if (cep.endsWith("333")) {
            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.number)
                .setMessage("usuário não pode acessar esse recurso")
                .addDetails(Any.pack(ErroDetails.newBuilder()
                                        .setCode(401)
                                        .setMessage("token expirado")
                                        .build())).build()

            val e = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver?.onError(e)
        }

        var valor = 0.0
        try {
            valor = Random.nextDouble(from = 0.0, until = 140.0)
            if (valor > 100.0) {
                throw IllegalStateException("Erro inesperado ao executar lógica de negócio")
            }
        } catch (e: Exception) {
            responseObserver?.onError(
                Status.INTERNAL
                    .withDescription((e.message))
                    .withCause(e)
                    .asRuntimeException()
            )
        }

        val response = CalculaFreteResponse.newBuilder()
            .setCep(request!!.cep)
            .setValor(valor)
            .build()

        logger.info("Resposta: $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}