package dev.kord.core.behavior.interaction

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.MessageFlag
import dev.kord.common.entity.MessageFlags
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.core.Kord
import dev.kord.core.entity.interaction.CommandInteraction
import dev.kord.core.supplier.EntitySupplier
import dev.kord.core.supplier.EntitySupplyStrategy
import dev.kord.rest.builder.message.create.EphemeralInteractionResponseCreateBuilder
import dev.kord.rest.builder.message.create.PublicInteractionResponseCreateBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@OptIn(KordPreview::class)
interface CommandInteractionBehavior : InteractionBehavior {

    /**
     * Acknowledges an interaction ephemerally.
     *
     * @return [EphemeralInteractionResponseBehavior] Ephemeral acknowledgement of the interaction.
     */
    suspend fun acknowledgeEphemeral(): EphemeralInteractionResponseBehavior {
        val request =  InteractionResponseCreateRequest(
            type = InteractionResponseType.DeferredChannelMessageWithSource,
            data = Optional(
                InteractionApplicationCommandCallbackData(
                    flags = Optional(MessageFlags(MessageFlag.Ephemeral))
                )
            )
        )
        kord.rest.interaction.createInteractionResponse(id, token, request)
        return EphemeralInteractionResponseBehavior(applicationId, token, kord)
    }

    /**
     * Acknowledges an interaction.
     *
     * @return [PublicInteractionResponseBehavior] public acknowledgement of an interaction.
     */
    suspend fun acknowledgePublic(): PublicInteractionResponseBehavior {
        val request = InteractionResponseCreateRequest(
            type = InteractionResponseType.DeferredChannelMessageWithSource
        )
        kord.rest.interaction.createInteractionResponse(id, token, request)
        return PublicInteractionResponseBehavior(applicationId, token, kord)
    }

}


/**
 * Acknowledges an interaction and responds with [PublicInteractionResponseBehavior].
 *
 * @param builder [PublicInteractionResponseCreateBuilder] used to a create an public response.
 * @return [PublicInteractionResponseBehavior] public response to the interaction.
 */
@KordPreview
@OptIn(ExperimentalContracts::class)
suspend inline fun CommandInteractionBehavior.respondPublic(
    builder: PublicInteractionResponseCreateBuilder.() -> Unit
): PublicInteractionResponseBehavior {

    contract { callsInPlace(builder, InvocationKind.EXACTLY_ONCE) }

    val request = PublicInteractionResponseCreateBuilder().apply(builder).toRequest()
    kord.rest.interaction.createInteractionResponse(id, token, request)
    return PublicInteractionResponseBehavior(applicationId, token, kord)

}


/**
 * Acknowledges an interaction and responds with [EphemeralInteractionResponseBehavior] with ephemeral flag.
 *
 * @param builder [EphemeralInteractionResponseCreateBuilder] used to a create an ephemeral response.
 * @return [EphemeralInteractionResponseBehavior] ephemeral response to the interaction.
 */
@KordPreview
@OptIn(ExperimentalContracts::class)
suspend inline fun CommandInteractionBehavior.respondEphemeral(
    builder: EphemeralInteractionResponseCreateBuilder.() -> Unit
): EphemeralInteractionResponseBehavior {

    contract { callsInPlace(builder, InvocationKind.EXACTLY_ONCE) }
    val builder = EphemeralInteractionResponseCreateBuilder().apply(builder)
    val request = builder.toRequest()
    kord.rest.interaction.createInteractionResponse(id, token, request)
    return EphemeralInteractionResponseBehavior(applicationId, token, kord)

}


@KordPreview
fun CommandInteractionBehavior(
    id: Snowflake,
    channelId: Snowflake,
    token: String,
    applicationId: Snowflake,
    kord: Kord,
    strategy: EntitySupplyStrategy<*> = kord.resources.defaultStrategy
) = object : CommandInteractionBehavior {
    override val id: Snowflake
        get() = id

    override val token: String
        get() = token

    override val applicationId: Snowflake
        get() = applicationId

    override val kord: Kord
        get() = kord

    override val channelId: Snowflake
        get() = channelId


    override val supplier: EntitySupplier = strategy.supply(kord)

}