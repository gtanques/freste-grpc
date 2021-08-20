package com.orange

import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import kotlin.random.Random

@Singleton
class FreteGrpcServer : FretesServiceGrpc.FretesServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FreteGrpcServer::class.java)

    override fun calculaFrete(request: CalculaFreteRequest?, responseObserver: StreamObserver<CalculaFreteResponse>?) {

        logger.info("Calculando frete para request: $request")


        val response = CalculaFreteResponse.newBuilder()
            .setCep(request!!.cep)
            .setValor(Random.nextDouble(from = 0.0, until = 140.0))
            .build()

        logger.info("Resposta: $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}