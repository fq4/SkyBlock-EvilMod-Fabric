/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.skyguygamer.sbmod.event;

import java.time.Instant;

import com.mojang.authlib.GameProfile;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Contains client-side events triggered when sending or receiving messages.
 */
public final class ClientMessageEvents {
    private ClientMessageEvents() {
    }

    /**
     * An event triggered when the client is about to send a chat message,
     * typically from a client GUI. Mods can use this to block the message.
     *
     * <p>If a listener returned {@code false}, the message will not be sent,
     * the remaining listeners will not be called (if any), and
     * {@link #CANCELED_SEND_CHAT_MESSAGE} will be triggered instead of {@link #SEND_CHAT_MESSAGE}.
     */
    public static final Event<AllowSendChatMessage> ALLOW_SEND_CHAT_MESSAGE = EventFactory.createArrayBacked(AllowSendChatMessage.class, listeners -> (message) -> {
        for (AllowSendChatMessage listener : listeners) {
            if (!listener.allowSendChatMessage(message)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event triggered when the client is about to send a command,
     * which is whenever the player executes a command
     * including client commands registered with {@code fabric-command-api}.
     * Mods can use this to block the message.
     * The command string does not include a slash at the beginning.
     *
     * <p>If a listener returned {@code false}, the command will not be sent,
     * the remaining listeners will not be called (if any), and
     * {@link #CANCELED_SEND_COMMAND_MESSAGE} will be triggered instead of {@link #SEND_COMMAND_MESSAGE}.
     */
    public static final Event<AllowSendCommandMessage> ALLOW_SEND_COMMAND_MESSAGE = EventFactory.createArrayBacked(AllowSendCommandMessage.class, listeners -> (command) -> {
        for (AllowSendCommandMessage listener : listeners) {
            if (!listener.allowSendCommandMessage(command)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event triggered when the client sends a chat message,
     * typically from a client GUI. Is not called when {@linkplain
     * #ALLOW_SEND_CHAT_MESSAGE chat messages are blocked}.
     */
    public static final Event<SendChatMessage> SEND_CHAT_MESSAGE = EventFactory.createArrayBacked(SendChatMessage.class, listeners -> (message) -> {
        for (SendChatMessage listener : listeners) {
            listener.onSendChatMessage(message);
        }
    });

    /**
     * An event triggered when the client sends a command,
     * which is whenever the player executes a command
     * including client commands registered with {@code fabric-command-api}.
     * Is not called when {@linkplain #ALLOW_SEND_COMMAND_MESSAGE command messages are blocked}.
     * The command string does not include a slash at the beginning.
     */
    public static final Event<SendCommandMessage> SEND_COMMAND_MESSAGE = EventFactory.createArrayBacked(SendCommandMessage.class, listeners -> (command) -> {
        for (SendCommandMessage listener : listeners) {
            listener.onSendCommandMessage(command);
        }
    });

    /**
     * An event triggered when sending a chat message is canceled with {@link #ALLOW_SEND_CHAT_MESSAGE}.
     */
    public static final Event<CanceledSendChatMessage> CANCELED_SEND_CHAT_MESSAGE = EventFactory.createArrayBacked(CanceledSendChatMessage.class, listeners -> (message) -> {
        for (CanceledSendChatMessage listener : listeners) {
            listener.onCanceledSendChatMessage(message);
        }
    });

    /**
     * An event triggered when sending a command is canceled with {@link #ALLOW_SEND_COMMAND_MESSAGE}.
     * The command string does not include a slash at the beginning.
     */
    public static final Event<CanceledSendCommandMessage> CANCELED_SEND_COMMAND_MESSAGE = EventFactory.createArrayBacked(CanceledSendCommandMessage.class, listeners -> (command) -> {
        for (CanceledSendCommandMessage listener : listeners) {
            listener.onCanceledSendCommandMessage(command);
        }
    });

    /**
     * An event triggered when the client receives a chat message,
     * which is any message sent by a player. Mods can use this to block the message.
     *
     * <p>If a listener returned {@code false}, the message will not be displayed,
     * the remaining listeners will not be called (if any), and
     * {@link #CANCELED_RECEIVE_CHAT_MESSAGE} will be triggered instead of {@link #RECEIVE_CHAT_MESSAGE}.
     */
    public static final Event<AllowReceiveChatMessage> ALLOW_RECEIVE_CHAT_MESSAGE = EventFactory.createArrayBacked(AllowReceiveChatMessage.class, listeners -> (message, signedMessage, sender, params, receptionTimestamp) -> {
        for (AllowReceiveChatMessage listener : listeners) {
            if (!listener.allowReceiveChatMessage(message, signedMessage, sender, params, receptionTimestamp)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event triggered when the client receives a game message,
     * which is any message sent by the server.
     * Mods can use this to block the message or toggle overlay.
     *
     * <p>If a listener returned {@code false}, the message will not be displayed,
     * the remaining listeners will not be called (if any), and
     * {@link #CANCELED_RECEIVE_GAME_MESSAGE} will be triggered instead of {@link #RECEIVE_GAME_MESSAGE}.
     *
     * <p>Overlay is whether the message will be displayed in the action bar.
     * To toggle overlay, return false and call
     * {@link net.minecraft.client.network.ClientPlayerEntity#sendMessage(Text, boolean) ClientPlayerEntity.sendMessage(message, overlay)}.
     */
    public static final Event<AllowReceiveGameMessage> ALLOW_RECEIVE_GAME_MESSAGE = EventFactory.createArrayBacked(AllowReceiveGameMessage.class, listeners -> (message, overlay) -> {
        for (AllowReceiveGameMessage listener : listeners) {
            if (!listener.allowReceiveGameMessage(message, overlay)) {
                return false;
            }
        }

        return true;
    });

    /**
     * An event triggered when the client receives a chat message,
     * which is any message sent by a player. Is not called when
     * {@linkplain #ALLOW_RECEIVE_CHAT_MESSAGE chat messages are blocked}.
     */
    public static final Event<ReceiveChatMessage> RECEIVE_CHAT_MESSAGE = EventFactory.createArrayBacked(ReceiveChatMessage.class, listeners -> (message, signedMessage, sender, params, receptionTimestamp) -> {
        for (ReceiveChatMessage listener : listeners) {
            listener.onReceiveChatMessage(message, signedMessage, sender, params, receptionTimestamp);
        }
    });

    /**
     * An event triggered when the client receives a chat message,
     * which is any message sent by the server. Is not called when
     * {@linkplain #ALLOW_RECEIVE_CHAT_MESSAGE chat messages are blocked}.
     *
     * <p>Overlay is whether the message will be displayed in the action bar.
     * Use {@link #ALLOW_RECEIVE_GAME_MESSAGE to toggle overlay}.
     */
    public static final Event<ReceiveGameMessage> RECEIVE_GAME_MESSAGE = EventFactory.createArrayBacked(ReceiveGameMessage.class, listeners -> (message, overlay) -> {
        for (ReceiveGameMessage listener : listeners) {
            listener.onReceiveGameMessage(message, overlay);
        }
    });

    /**
     * An event triggered when receiving a chat message is canceled with {@link #ALLOW_RECEIVE_CHAT_MESSAGE}.
     */
    public static final Event<CanceledReceiveChatMessage> CANCELED_RECEIVE_CHAT_MESSAGE = EventFactory.createArrayBacked(CanceledReceiveChatMessage.class, listeners -> (message, signedMessage, sender, params, receptionTimestamp) -> {
        for (CanceledReceiveChatMessage listener : listeners) {
            listener.onCanceledReceiveChatMessage(message, signedMessage, sender, params, receptionTimestamp);
        }
    });

    /**
     * An event triggered when receiving a game message is canceled with {@link #ALLOW_RECEIVE_GAME_MESSAGE}.
     *
     * <p>Overlay is whether the message would have been displayed in the action bar.
     */
    public static final Event<CanceledReceiveGameMessage> CANCELED_RECEIVE_GAME_MESSAGE = EventFactory.createArrayBacked(CanceledReceiveGameMessage.class, listeners -> (message, overlay) -> {
        for (CanceledReceiveGameMessage listener : listeners) {
            listener.onCanceledReceiveGameMessage(message, overlay);
        }
    });

    @FunctionalInterface
    public interface AllowSendChatMessage {
        /**
         * Called when the client is about to send a chat message,
         * typically from a client GUI. Returning {@code false}
         * prevents the message from being sent, and
         * {@link #CANCELED_SEND_CHAT_MESSAGE} will be triggered instead of {@link #SEND_CHAT_MESSAGE}.
         *
         * @param message the message that will be sent to the server
         * @return {@code true} if the message should be sent, otherwise {@code false}
         */
        boolean allowSendChatMessage(String message);
    }

    @FunctionalInterface
    public interface AllowSendCommandMessage {
        /**
         * Called when the client is about to send a command,
         * which is whenever the player executes a command
         * including client commands registered with {@code fabric-command-api}.
         * Returning {@code false} prevents the command from being sent, and
         * {@link #CANCELED_SEND_COMMAND_MESSAGE} will be triggered instead of {@link #SEND_COMMAND_MESSAGE}.
         * The command string does not include a slash at the beginning.
         *
         * @param command the command that will be sent to the server, without a slash at the beginning.
         * @return {@code true} if the command should be sent, otherwise {@code false}
         */
        boolean allowSendCommandMessage(String command);
    }

    @FunctionalInterface
    public interface SendChatMessage {
        /**
         * Called when the client sends a chat message,
         * typically from a client GUI. Is not called when {@linkplain
         * #ALLOW_SEND_CHAT_MESSAGE chat messages are blocked}.
         *
         * @param message the message that is being sent to the server
         */
        void onSendChatMessage(String message);
    }

    @FunctionalInterface
    public interface SendCommandMessage {
        /**
         * Called when the client sends a command,
         * which is whenever the player executes a command
         * including client commands registered with {@code fabric-command-api}.
         * Is not called when {@linkplain #ALLOW_SEND_COMMAND_MESSAGE command messages are blocked}.
         * The command string does not include a slash at the beginning.
         *
         * @param command the command that is being sent to the server, without a slash at the beginning.
         */
        void onSendCommandMessage(String command);
    }

    @FunctionalInterface
    public interface CanceledSendChatMessage {
        /**
         * Called when sending a chat message is canceled with {@link #ALLOW_SEND_CHAT_MESSAGE}.
         *
         * @param message the message that is canceled from being sent to the server
         */
        void onCanceledSendChatMessage(String message);
    }

    @FunctionalInterface
    public interface CanceledSendCommandMessage {
        /**
         * Called when sending a command is canceled with {@link #ALLOW_SEND_COMMAND_MESSAGE}.
         * The command string does not include a slash at the beginning.
         *
         * @param command the command that is being sent to the server, without a slash at the beginning.
         */
        void onCanceledSendCommandMessage(String command);
    }

    @FunctionalInterface
    public interface AllowReceiveChatMessage {
        /**
         * Called when the client receives a chat message,
         * which is any message sent by a player.
         * Returning {@code false} prevents the message from being displayed, and
         * {@link #CANCELED_RECEIVE_CHAT_MESSAGE} will be triggered instead of {@link #RECEIVE_CHAT_MESSAGE}.
         *
         * @param message            the message received from the server
         * @param signedMessage      the signed message received from the server (nullable)
         * @param sender             the sender of the message (nullable)
         * @param params             the parameters of the message
         * @param receptionTimestamp the timestamp when the message was received
         * @return {@code true} if the message should be displayed, otherwise {@code false}
         */
        boolean allowReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp);
    }

    @FunctionalInterface
    public interface AllowReceiveGameMessage {
        /**
         * Called when the client receives a game message,
         * which is any message sent by the server. Returning {@code false}
         * prevents the message from being displayed, and
         * {@link #CANCELED_RECEIVE_GAME_MESSAGE} will be triggered instead of {@link #RECEIVE_GAME_MESSAGE}.
         *
         * <p>Overlay is whether the message will be displayed in the action bar.
         * To toggle overlay, return false and call
         * {@link net.minecraft.client.network.ClientPlayerEntity#sendMessage(Text, boolean) ClientPlayerEntity.sendMessage(message, overlay)}.
         *
         * @param message the message received from the server
         * @param overlay whether the message will be displayed in the action bar
         * @return {@code true} if the message should be displayed, otherwise {@code false}
         */
        boolean allowReceiveGameMessage(Text message, boolean overlay);
    }

    @FunctionalInterface
    public interface ReceiveChatMessage {
        /**
         * Called when the client receives a chat message,
         * which is any message sent by a player. Is not called when
         * {@linkplain #ALLOW_RECEIVE_CHAT_MESSAGE chat messages are blocked}.
         *
         * @param message            the message received from the server
         * @param signedMessage      the signed message received from the server (nullable)
         * @param sender             the sender of the message (nullable)
         * @param params             the parameters of the message
         * @param receptionTimestamp the timestamp when the message was received
         */
        void onReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp);
    }

    @FunctionalInterface
    public interface ReceiveGameMessage {
        /**
         * Called when the client receives a game message,
         * which is any message sent by the server. Is not called when
         * {@linkplain #ALLOW_RECEIVE_GAME_MESSAGE game messages are blocked}.
         *
         * <p>Overlay is whether the message will be displayed in the action bar.
         * Use {@link #ALLOW_RECEIVE_GAME_MESSAGE} to toggle overlay.
         *
         * @param message the message received from the server
         * @param overlay whether the message will be displayed in the action bar
         */
        void onReceiveGameMessage(Text message, boolean overlay);
    }

    @FunctionalInterface
    public interface CanceledReceiveChatMessage {
        /**
         * Called when receiving a chat message is canceled with {@link #ALLOW_RECEIVE_CHAT_MESSAGE}.
         *
         * @param message            the message received from the server
         * @param signedMessage      the signed message received from the server (nullable)
         * @param sender             the sender of the message (nullable)
         * @param params             the parameters of the message
         * @param receptionTimestamp the timestamp when the message was received
         */
        void onCanceledReceiveChatMessage(Text message, @Nullable SignedMessage signedMessage, @Nullable GameProfile sender, MessageType.Parameters params, Instant receptionTimestamp);
    }

    @FunctionalInterface
    public interface CanceledReceiveGameMessage {
        /**
         * Called when receiving a game message is canceled with {@link #ALLOW_RECEIVE_GAME_MESSAGE}.
         *
         * @param message the message received from the server
         * @param overlay whether the message would have been displayed in the action bar
         */
        void onCanceledReceiveGameMessage(Text message, boolean overlay);
    }
}