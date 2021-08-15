package dev.kord.core.behavior.interaction

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.InteractionResponseType
import dev.kord.common.entity.MessageFlag
import dev.kord.common.entity.MessageFlags
import dev.kord.common.entity.optional.Optional
import dev.kord.rest.builder.message.create.EphemeralInteractionResponseCreateBuilder
import dev.kord.rest.builder.message.create.PublicInteractionResponseCreateBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

interface ApplicationCommandInteractionBehavior : InteractionBehavior
