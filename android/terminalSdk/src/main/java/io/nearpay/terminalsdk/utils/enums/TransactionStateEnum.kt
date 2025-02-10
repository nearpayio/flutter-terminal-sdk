package io.nearpay.softpos.core.utils.enums

enum class TransactionStateEnum(val id: String) {
    JOB_INITIALIZED("JobInitialized"),
    JOB_STARTED("JobStarted"),
    JOB_ENDED("JobEnded"),
    READER_WAITING("Reader.Waiting"),
    READER_READING("Reader.Reading"),
    READER_FINISHED("Reader.Finished"),
    READER_RETRY("Reader.Retry"),
    READER_ERROR("Reader.Error"),
    PIN_ENTERING("Pin.Entering"),
    ONLINE_PROCESSING("Online.Processing"),
    ONLINE_RESULT("Online.Result"),
    ONLINE_ERROR("Online.Error");

    companion object {
        fun of(id: String): TransactionStateEnum {
            return when (id) {
                "JobInitialized" -> JOB_INITIALIZED
                "JobStarted" -> JOB_STARTED
                "JobEnded" -> JOB_ENDED
                "Reader.Waiting" -> READER_WAITING
                "Reader.Reading" -> READER_READING
                "Reader.Finished" -> READER_FINISHED
                "Reader.Retry" -> READER_RETRY
                "Reader.Error" -> READER_ERROR
                "Pin.Entering" -> PIN_ENTERING
                "Online.Processing" -> ONLINE_PROCESSING
                "Online.Result" -> ONLINE_RESULT
                "Online.Error" -> ONLINE_ERROR
                else -> READER_WAITING
            }
        }
    }
}