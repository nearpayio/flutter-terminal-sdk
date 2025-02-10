package io.nearpay.terminalsdk.data.dto


enum class NiceErrors(
    val messageError: MessageError
) {

    PARSING_ERROR_MSG_FAILED(
        MessageError(
            code_text = "parsing_error_msg_failed",
            code = 5000,
            error = LocalizationField("حدث خطأ غير متوقع.", "Unexpected Error"),
            solution = LocalizationField(
                "لمزيد من المعلومات، يمكنك التواصل مع فريق الدعم الخاص بمزود الخدمة",
                "Contact your technical provider"
            ),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    PARSING_RESPONSE_FAILED(
        MessageError(
            code_text = "parsing_response_failed",
            code = 5001,
            error = LocalizationField("حدث خطأ غير متوقع..", "Unexpected Error"),
            solution = LocalizationField(
                "لمزيد من المعلومات، يمكنك التواصل مع فريق الدعم الخاص بمزود الخدمة",
                "Contact your technical provider"
            ),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    NETWORK_ERROR(
        MessageError(
            code_text = "network_error",
            code = 5002,
            error = LocalizationField("حدث خطأ في الشبكة", "A network error has occurred"),
            solution = LocalizationField(
                "تحقق من الاتصال بشبكة Wi-Fi أو 4G/5G ",
                "Check your Wi-Fi or 4G/5G connection"
            ),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "الجهاز",
                english = "Device",
                color = "#000000"
            )
        )
    ),

    INVALID_MOBILE(
        MessageError(
            code_text = "invalid_mobile",
            code = 5003,
            error = LocalizationField("رقم الهاتف غير صحيح", "Invalid mobile number"),
            solution = LocalizationField(
                "الرجاء التأكد من إدخال رقم هاتف صحيح (على سبيل المثال 5XXXXXXXX)",
                "Please make sure you entered a valid mobile number (for example 5XXXXXXXX)"
            ),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    AUTHENTICATION_ERROR(
        MessageError(
            code_text = "authentication_error",
            code = 5004,
            error = LocalizationField("حدث خطأ في تسجيل الدخول", "Error in Authentication Credentials"),
            solution = LocalizationField(
                "لمزيد من المعلومات، يمكنك التواصل مع فريق الدعم الخاص بمزود الخدمة",
                "Contact your technical provider"
            ),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),


    UNEXPECTED_ERROR(
        MessageError(
            code_text = "unexpected_error",
            code = 5003,
            error = LocalizationField("حدث خطأ غير متوقع", "Unexpected Error"),
            solution = LocalizationField(
                "لمزيد من المعلومات، يمكنك التواصل مع فريق الدعم الخاص بمزود التقنية",
                "Contact your technical provider"
            ),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    IRREVERSIBLE_TRANSACTION(
        MessageError(
            code_text = "irreversible_transaction",
            code = 5004,
            error = LocalizationField("هذه المعاملة لا يمكن عكسها", "This transaction cannot be reversed"),
            solution = LocalizationField(
                "يمكن عكس هذه المعاملة خلال 60 ثانية فقط",
                "Transactions can only be reversed within 60 seconds of the transaction."
            ),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    SPIN_INITIALIZING_FAILED(
        MessageError(
            code_text = "spin_initializing_failed",
            code = 5005,
            error = LocalizationField("spin_initializing_failed", "spin_initializing_failed"),
            solution = LocalizationField("spin_initializing_failed", "spin_initializing_failed"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    INCORRECT_ADMIN_PIN(
        MessageError(
            code_text = "incorrect_admin_pin",
            code = 5006,
            error = LocalizationField("لقد قمت بإدخال رمز مرور المشرف بشكل غير صحيح", "Admin PIN entered incorrectly"),
            solution = LocalizationField("الرجاء إعادة إدخال رمز المرور", "Re-enter the Admin PIN"),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    UNEXPECTED_LOGIN_ERROR(
        MessageError(
            code_text = "unexpected_login_error",
            code = 5007,
            error = LocalizationField("حدث خطأ في تسجيل الدخول", "Unexpected Login Error"),
            solution = LocalizationField("الرجاء تسجيل الدخول مره اخرى", "Try to login again"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    UNMATCH_USER_PIN(
        MessageError(
            code_text = "unmatch_user_pin",
            code = 5008,
            error = LocalizationField("لا تتطابق كلمتا المرور اللتان تم إدخالهما", "User PIN does not match"),
            solution = LocalizationField("يُرجى إعادة المحاولة", "Re-enter the User PIN"),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    INCORRECT_USER_PIN(
        MessageError(
            code_text = "incorrect_user_pin",
            code = 5009,
            error = LocalizationField("تم إدخال رمز PIN الخاص بالمستخدم بشكل غير صحيح", "User PIN entered incorrectly"),
            solution = LocalizationField("الرجاء إعادة إدخال رمز الـPIN", "Re-enter the User PIN"),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    TERMINAL_NOT_AVAILABLE(
        MessageError(
            code_text = "terminal_not_available",
            code = 5010,
            error = LocalizationField("نقطة البيع متصلة بجهاز آخر", "The terminal is already connected to another device"),
            solution = LocalizationField("الرجاء التواصل مع المشرف المتجر لفصل نقطة البيع", "Contact your admin to disconnect the terminal"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    CRASH(
        MessageError(
            code_text = "crash",
            code = 5011,
            error = LocalizationField("خطأ غير متوقع", "Unexpected Error"),
            solution = LocalizationField("يرجى إعادة تشغيل التطبيق أو الاتصال بالمزود الفني الخاص بك", "Please reload the application or contact your technical provider"),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    TRANSACTION_CANCELED(
        MessageError(
            code_text = "transaction_canceled",
            code = 5012,
            error = LocalizationField("تم إلغاء العملية", "Transaction Cancelled"),
            solution = LocalizationField("تم إلغاء العملية ، يرجى المحاولة مرة اخرى", "your transaction was canceled ، please try again"),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    TRANSACTION_PROCESSING_ERROR(
        MessageError(
            code_text = "transaction_processing_error",
            code = 5013,
            error = LocalizationField(
                "خطأ في معالجة معاملة الدفع",
                "payment transaction processing error"
            ),
            solution = LocalizationField(
                "الرجاء معاودة المحاولة في وقت لاحق",
                "Please try again later"
            ),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    USE_LOAD_CONFIG(
        MessageError(
            code_text = "use_existing_config",
            code = 5019,
            error = LocalizationField("فشل تحميل أحدث الإعدادات","Failed to load the latest configuration"),
            solution = LocalizationField("لمزيد من المعلومات، يمكنك التواصل مع فريق الدعم الخاص بمزود التقنية. للمتابعة بالإعدادات الحالية إضغط زر الأستمرار.","Contact your technical provider. To proceed with existing configuration press continue"),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    TERMINAL_DOES_NOT_EXIST(
        MessageError(
            code_text = "unknown_terminal",
            code = 5020,
            error = LocalizationField("لا وجود لنقطة البيع المحددة", "This terminal does not exist"),
            solution = LocalizationField("الرجاء التأكد من إدخال رقم نقطة بيع صحيح", "Please make sure you entered the correct terminal ID"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    NEARBY_DEVICES_PERMISSION_DENIED(
        MessageError(
            code_text = "nearby_devices_permission",
            code = 5021,
            error = LocalizationField("التواصل مع الأجهزة المجاورة غير مسموح", "Connecting to nearby devices has been denied"),
            solution = LocalizationField("الرجاء السماح بالإذن للتواصل مع الأجهزة المجاورة من إعدادات النظام", "Please enable nearby devices permission from system settings"),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    COROUTINE_CANCEL_ERROR(
        MessageError(
            code_text = "timeout_error",
            code = 5022,
            error = LocalizationField("نفذ الوقت", "Timeout"),
            solution = LocalizationField(
                "لمزيد من المعلومات، يمكنك التواصل مع فريق الدعم الخاص بمزود التقنية",
                "Contact your technical provider"
            ),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),


    CAMERA_PERMISSION_DENIED(
        MessageError(
            code_text = "camera_permission",
            code = 5023,
            error = LocalizationField("تم رفض استخدام الكاميرا", "Camera permission has been denied"),
            solution = LocalizationField("الرجاء السماح بالإذن لاستخدام الكاميرا من إعدادات النظام", "Please enable camera permission from system settings"),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "المستخدم",
                english = "User",
                color = "#000000"
            )
        )
    ),

    KERNEL_OFFLINE_APPROVED(
        MessageError(
            code_text = "kernel_offline_approved",
            code = 6000,
            error = LocalizationField("تمت الموافقة على الدفع غير الرسمي", "payment was offline approved"),
            solution = LocalizationField("تمت الموافقة على الدفع غير الرسمي", "payment was offline approved"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    KERNEL_OFFLINE_DECLINE(
        MessageError(
            code_text = "kernel_offline_decline",
            code = 6001,
            error = LocalizationField("تم رفض الدفع غير الرسمي", "payment was offline DECLINED"),
            solution = LocalizationField("تم رفض الدفع غير الرسمي", "payment was offline DECLINED"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    KERNEL_END_APPLICATION(
        MessageError(
            code_text = "kernel_end_application",
            code = 6002,
            error = LocalizationField("توقف التطبيق عن العمل", "Application has been end "),
            solution = LocalizationField("جرب مرة اخرى", "try again now"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    KERNEL_TRY_ANOTHER_INTERFACE(
        MessageError(
            code_text = "kernel_try_another_interface",
            code = 6003,
            error = LocalizationField(
                "لا يمكن إتمام الدفع",
                "Payment could not be completed"
            ),
            solution = LocalizationField(
                "جرب بطاقة اخرى",
                "Try another Interface"
            ),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    KERNEL_TRY_AGAIN(
        MessageError(
            code_text = "kernel_try_again",
            code = 6004,
            error = LocalizationField(
                "لم نتمكن من قراءة  بطاقتك",
                "We couldn't ready your card"
            ),
            solution = LocalizationField(
                "يرجى مراجعة هاتفك لإكمال العملية",
                "please see your phone to complete the process "
            ),
            level = MessageErrorLevel.LOW,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    KERNEL_SELECT_NEXT_OUTCOME(
        MessageError(
            code_text = "kernel_select_next_outcome",
            code = 6005,
            error = LocalizationField(
                "kernel_select_next_outcome",
                "kernel_select_next_outcome"
            ),
            solution = LocalizationField(
                "kernel_select_next_outcome",
                "kernel_select_next_outcome"
            ),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    KERNEL_NOT_APPLICABLE(
        MessageError(
            code_text = "kernel_not_applicable",
            code = 6006,
            error = LocalizationField("kernel_not_applicable", "kernel_not_applicable"),
            solution = LocalizationField("kernel_not_applicable", "kernel_not_applicable"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    KERNEL_NULL_OUTCOME(
        MessageError(
            code_text = "kernel_null_outcome",
            code = 6007,
            error = LocalizationField("kernel_null_outcome", "kernel_null_outcome"),
            solution = LocalizationField("kernel_null_outcome", "kernel_null_outcome"),
            level = MessageErrorLevel.MEDIUM,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),
    WEBSOCKET_ROOM_NOT_FOUND(
        MessageError(
            code_text = "room_not_found",
            code = 5039,
            error = LocalizationField("خطأ في الاتصال", "Connection not found"),
            solution = LocalizationField("اعد الاتصال لاستكمال استلام المدفوعات", "reconnect to continue receiving payments"),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),
    WEBSOCKET_TIMEOUT(
        MessageError(
            code_text = "websocket_timeout",
            code = 5040,
            error = LocalizationField("انتهت مهلة الاتصال", "Websocket timeout"),
            solution = LocalizationField("أعد المحاولة", "Try again"),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),
    TERMINAL_NOT_READY(
        MessageError(
            code_text = "terminal_not_ready",
            code = 5041,
            error = LocalizationField("نقطة البيع غير جاهزة", "Terminal not ready"),
            solution = LocalizationField("تأكد من أن نقطة البيع في الصفحة الرئيسية", "Make sure the terminal is in home screen"),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),

    REFUND_AMOUNT_EXCEEDS(
        MessageError(
            code_text = "transaction_amount_more_than_refundable_amount",
            code = 4019,
            error = LocalizationField("مبلغ العملية اكثر من المبلغ المتبقي للإسترداد", "Transaction amount more than refundable amount"),
            solution = LocalizationField("الرجاء التأكد من المبلغ المسترد", "Please check refundable amount"),
            level = MessageErrorLevel.HIGH,
            source = MessageErrorSource(
                arabic = "NearPay",
                english = "NearPay",
                color = "#000000"
            )
        )
    ),
}