package dev.kord.voice.udp

import dev.kord.common.annotation.KordVoice
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first

@KordVoice
public interface VoiceUdpSocket {
    public val incoming: SharedFlow<Datagram>

    public suspend fun discoverIp(address: NetworkAddress, ssrc: Int): NetworkAddress

    public suspend fun send(packet: Datagram)

    public suspend fun stop()

    public companion object {
        private object None : VoiceUdpSocket {
            override val incoming: SharedFlow<Datagram> = MutableSharedFlow()

            override suspend fun discoverIp(address: NetworkAddress, ssrc: Int): NetworkAddress {
                return address
            }

            override suspend fun send(packet: Datagram) {}

            override suspend fun stop() {}
        }

        public fun none(): VoiceUdpSocket = None
    }
}

@KordVoice
public suspend fun VoiceUdpSocket.receiveFrom(address: NetworkAddress): Datagram =
    incoming.filter { it.address == address }.first()