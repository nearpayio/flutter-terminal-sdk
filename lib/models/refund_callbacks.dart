import 'package:flutter_terminal_sdk/models/transaction_response.dart';

import 'card_reader_callbacks.dart';

typedef VoidCallback = void Function();
typedef StringCallback = void Function(String message);
typedef TransactionResponseCallback = void Function(
    TransactionResponse response);

class RefundCallbacks {
  final CardReaderCallbacks? cardReaderCallbacks;
  final StringCallback? onSendTransactionFailure;
  final TransactionResponseCallback? onSendTransactionSuccess;

  RefundCallbacks({
    this.cardReaderCallbacks,
    this.onSendTransactionFailure,
    this.onSendTransactionSuccess,
  });
}
