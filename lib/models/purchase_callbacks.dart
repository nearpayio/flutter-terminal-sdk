

import 'package:flutter_terminal_sdk/models/transaction_response.dart';

typedef VoidCallback = void Function();
typedef StringCallback = void Function(String message);
typedef TransactionResponseCallback = void Function(TransactionResponse response);

class PurchaseCallbacks {
  final VoidCallback? onReadingStarted;
  final VoidCallback? onReaderWaiting;
  final VoidCallback? onReaderReading;
  final VoidCallback? onReaderRetry;
  final VoidCallback? onPinEntering;
  final VoidCallback? onReaderFinished;
  final StringCallback? onReaderError;
  final VoidCallback? onCardReadSuccess;
  final StringCallback? onCardReadFailure;
  final VoidCallback? onSendTransactionSuccess;
  final StringCallback? onSendTransactionFailure;
  final TransactionResponseCallback? onSendTransactionSuccessData;

  PurchaseCallbacks({
    this.onReadingStarted,
    this.onReaderWaiting,
    this.onReaderReading,
    this.onReaderRetry,
    this.onPinEntering,
    this.onReaderFinished,
    this.onReaderError,
    this.onCardReadSuccess,
    this.onCardReadFailure,
    this.onSendTransactionSuccess,
    this.onSendTransactionFailure,
    this.onSendTransactionSuccessData,
  });
}