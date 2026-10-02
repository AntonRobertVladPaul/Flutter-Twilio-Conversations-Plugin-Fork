package twilio.flutter.twilio_conversations.methods

import com.twilio.conversations.CallbackListener
import com.twilio.conversations.Conversation
import com.twilio.util.ErrorInfo
import com.twilio.conversations.Message
import com.twilio.conversations.StatusListener
import twilio.flutter.twilio_conversations.Api
import twilio.flutter.twilio_conversations.Mapper
import twilio.flutter.twilio_conversations.TwilioConversationsPlugin
import twilio.flutter.twilio_conversations.exceptions.ClientNotInitializedException
import twilio.flutter.twilio_conversations.exceptions.ConversionException
import twilio.flutter.twilio_conversations.exceptions.NotFoundException
import twilio.flutter.twilio_conversations.exceptions.TwilioException

class MessageMethods : Api.MessageApi {
    private val TAG = "MessageMethods"

    override fun getMediaContentTemporaryUrl(
        conversationSid: String,
        messageIndex: Long,
        result: Api.Result<String>
    ) {
        debug("getMediaContentTemporaryUrl => conversationSid: $conversationSid")
        val client = TwilioConversationsPlugin.client
            ?: return result.error(ClientNotInitializedException("Client is not initialized"))

        client.getConversation(conversationSid, object : CallbackListener<Conversation> {
            override fun onSuccess(conversation: Conversation) {
                conversation.getMessageByIndex(messageIndex, object : CallbackListener<Message> {
                    override fun onSuccess(message: Message) {
                        // Twilio 3.0 moved the temporary URL onto the media itself;
                        // the Dart API models one media per message, as 1.x did.
                        val media = message.attachedMedia.firstOrNull()
                            ?: return result.error(NotFoundException("Message has no media"))
                        media.getTemporaryContentUrl(object : CallbackListener<String> {
                            override fun onSuccess(url: String) {
                                debug("getMediaContentTemporaryUrl => onSuccess $url")
                                result.success(url)
                            }

                            override fun onError(errorInfo: ErrorInfo) {
                                debug("getMediaContentTemporaryUrl => onError: $errorInfo")
                                result.error(TwilioException(errorInfo.code, errorInfo.message))
                            }
                        })
                    }

                    override fun onError(errorInfo: ErrorInfo) {
                        debug("getMediaContentTemporaryUrl => onError: $errorInfo")
                        result.error(TwilioException(errorInfo.code, errorInfo.message))
                    }
                })
            }

            override fun onError(errorInfo: ErrorInfo) {
                debug("getMediaContentTemporaryUrl => onError: $errorInfo")
                result.error(TwilioException(errorInfo.code, errorInfo.message))
            }
        })
    }

    override fun getParticipant(
        conversationSid: String,
        messageIndex: Long,
        result: Api.Result<Api.ParticipantData>
    ) {
        debug("getParticipant => conversationSid: $conversationSid messageIndex: $messageIndex")
        val client = TwilioConversationsPlugin.client
            ?: return result.error(ClientNotInitializedException("Client is not initialized"))

        client.getConversation(conversationSid, object : CallbackListener<Conversation> {
            override fun onSuccess(conversation: Conversation) {
                conversation.getMessageByIndex(messageIndex, object : CallbackListener<Message> {
                    override fun onSuccess(message: Message) {
                        val participant = message.participant
                            ?: return result.error(NotFoundException("Participant not found for message: $messageIndex."))
                        debug("getParticipant => onSuccess")
                        return result.success(Mapper.participantToPigeon(participant))
                    }

                    override fun onError(errorInfo: ErrorInfo) {
                        debug("getParticipant => onError: $errorInfo")
                        result.error(TwilioException(errorInfo.code, errorInfo.message))
                    }
                })
            }

            override fun onError(errorInfo: ErrorInfo) {
                debug("getParticipant => onError: $errorInfo")
                result.error(TwilioException(errorInfo.code, errorInfo.message))
            }
        })
    }

    override fun setAttributes(
        conversationSid: String,
        messageIndex: Long,
        attributes: Api.AttributesData,
        result: Api.Result<Void>
    ) {
        debug("setAttributes => conversationSid: $conversationSid messageIndex: $messageIndex")
        val client = TwilioConversationsPlugin.client
            ?: return result.error(ClientNotInitializedException("Client is not initialized"))
        val messageAttributes = Mapper.pigeonToAttributes(attributes)
            ?: return result.error(ConversionException("Could not convert $attributes to valid Attributes"))

        client.getConversation(conversationSid, object : CallbackListener<Conversation> {
            override fun onSuccess(conversation: Conversation) {
                conversation.getMessageByIndex(messageIndex, object : CallbackListener<Message> {
                    override fun onSuccess(message: Message) {
                        message.setAttributes(messageAttributes, object : StatusListener {
                            override fun onSuccess() {
                                debug("setAttributes => onSuccess")
                                result.success(null)
                            }

                            override fun onError(errorInfo: ErrorInfo) {
                                debug("setAttributes => onError: $errorInfo")
                                result.error(TwilioException(errorInfo.code, errorInfo.message))
                            }
                        })
                    }

                    override fun onError(errorInfo: ErrorInfo) {
                        debug("setAttributes => onError: $errorInfo")
                        result.error(TwilioException(errorInfo.code, errorInfo.message))
                    }
                })
            }

            override fun onError(errorInfo: ErrorInfo) {
                debug("setAttributes => onError: $errorInfo")
                result.error(TwilioException(errorInfo.code, errorInfo.message))
            }
        })
    }

    override fun updateMessageBody(
        conversationSid: String,
        messageIndex: Long,
        messageBody: String,
        result: Api.Result<Void>
    ) {
        debug("updateMessageBody => conversationSid: $conversationSid messageIndex: $messageIndex")
        val client = TwilioConversationsPlugin.client
            ?: return result.error(ClientNotInitializedException("Client is not initialized"))

        client.getConversation(conversationSid, object : CallbackListener<Conversation> {
            override fun onSuccess(conversation: Conversation) {
                conversation.getMessageByIndex(messageIndex, object : CallbackListener<Message> {
                    override fun onSuccess(message: Message) {
                        message.updateBody(messageBody, object : StatusListener {
                            override fun onSuccess() {
                                debug("updateMessageBody => onSuccess")
                                result.success(null)
                            }

                            override fun onError(errorInfo: ErrorInfo) {
                                debug("updateMessageBody => onError: $errorInfo")
                                result.error(TwilioException(errorInfo.code, errorInfo.message))
                            }
                        })
                    }

                    override fun onError(errorInfo: ErrorInfo) {
                        debug("updateMessageBody => onError: $errorInfo")
                        result.error(TwilioException(errorInfo.code, errorInfo.message))
                    }
                })
            }

            override fun onError(errorInfo: ErrorInfo) {
                debug("updateMessageBody => onError: $errorInfo")
                result.error(TwilioException(errorInfo.code, errorInfo.message))
            }
        })
    }

    fun debug(message: String) {
        TwilioConversationsPlugin.debug("$TAG::$message")
    }
}
