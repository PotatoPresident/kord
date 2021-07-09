package dev.kord.core.cache.data

import dev.kord.cache.api.data.description
import dev.kord.common.entity.*
import dev.kord.common.entity.optional.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelData(
    val id: Snowflake,
    val type: ChannelType,
    val guildId: OptionalSnowflake = OptionalSnowflake.Missing,
    val position: OptionalInt = OptionalInt.Missing,
    val permissionOverwrites: Optional<List<Overwrite>> = Optional.Missing(),
    val name: Optional<String> = Optional.Missing(),
    val topic: Optional<String?> = Optional.Missing(),
    val nsfw: OptionalBoolean = OptionalBoolean.Missing,
    val lastMessageId: OptionalSnowflake? = OptionalSnowflake.Missing,
    val bitrate: OptionalInt = OptionalInt.Missing,
    val userLimit: OptionalInt = OptionalInt.Missing,
    val rateLimitPerUser: OptionalInt = OptionalInt.Missing,
    val recipients: Optional<List<Snowflake>> = Optional.Missing(),
    val icon: Optional<String?> = Optional.Missing(),
    val ownerId: OptionalSnowflake = OptionalSnowflake.Missing,
    val applicationId: OptionalSnowflake = OptionalSnowflake.Missing,
    val parentId: OptionalSnowflake? = OptionalSnowflake.Missing,
    val lastPinTimestamp: Optional<String?> = Optional.Missing(),
    val permissions: Optional<Permissions> = Optional.Missing(),
    @SerialName("thread_metadata")
    val threadsMetadata: Optional<ThreadMetadata> = Optional.Missing()
) {


    companion object {
        val description = description(ChannelData::id)

        fun from(entity: DiscordChannel) = with(entity) {
            ChannelData(
                id,
                type,
                guildId,
                position,
                permissionOverwrites,
                name,
                topic,
                nsfw,
                lastMessageId,
                bitrate,
                userLimit,
                rateLimitPerUser,
                recipients.mapList { it.id },
                icon,
                ownerId,
                applicationId,
                parentId,
                lastPinTimestamp,
                permissions,
                threadMetadata.map { ThreadMetadata.from(it) }
            )
        }
    }

}

@Serializable
data class ThreadMetadata(
    val archived: Boolean,
    @SerialName("archiver_id")
    val archiverId: OptionalSnowflake = OptionalSnowflake.Missing,
    @SerialName("archive_timestamp")
    val archiveTimestamp: String,
    val autoArchiveDuration: Int,
    val locked: OptionalBoolean = OptionalBoolean.Missing
) {
    companion object {
        fun from(threadMetadata: DiscordThreadMetadata): ThreadMetadata = with(threadMetadata) {
            ThreadMetadata(archived, archiverId, archiveTimestamp, autoArchiveDuration, locked)
        }
    }
}


fun DiscordChannel.toData() = ChannelData.from(this)