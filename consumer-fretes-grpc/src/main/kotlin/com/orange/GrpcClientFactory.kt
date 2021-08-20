package com.orange

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun fretesClientStub(@GrpcChannel(value = "fretes" ) channel: ManagedChannel): FretesServiceGrpc.FretesServiceBlockingStub? {

        /* val channel = ManagedChannelBuilder
            .forAddress("localhost", 50051)
            .usePlaintext()
            .maxRetryAttempts(10)
            .build() */

        return FretesServiceGrpc
            .newBlockingStub(channel)
    }
}