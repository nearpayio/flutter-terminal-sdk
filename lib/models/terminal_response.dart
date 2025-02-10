// lib/models/terminal_response.dart

import 'package:flutter_terminal_sdk/models/data/reconciliation_list_response.dart';
import 'package:flutter_terminal_sdk/models/refund_callbacks.dart';
import 'package:flutter_terminal_sdk/models/transaction_details_response.dart';
import 'package:flutter_terminal_sdk/models/transactions_response.dart';
import '../flutter_terminal_sdk.dart';
import 'data/reconcile_response.dart';
import 'data/reconciliation_receipts_response.dart';
import 'purchase_callbacks.dart';

class TerminalModel {
  final String tid;
  final bool isReady;
  final String terminalUUID;
  final String uuid;

  TerminalModel(
      {required this.tid,
      required this.isReady,
      required this.terminalUUID,
      required this.uuid});

  factory TerminalModel.fromJson(Map<String, dynamic> json) {
    return TerminalModel(
        tid: json['tid'] as String,
        isReady: json['isReady'] as bool? ?? false,
        terminalUUID: json['terminalUUID'] as String,
        uuid: json['uuid'] as String);
  }

  Map<String, dynamic> toJson() => {
        'tid': tid,
        'isReady': isReady,
        'terminalUUID': terminalUUID,
        'uuid': uuid,
      };

  Future<String> getTerminal({
    required String transactionUuid,
    required int amount,
    required String scheme,
    required PurchaseCallbacks callbacks,
  }) async {
    final sdk = FlutterTerminalSdk();
    return await sdk.purchase(
      uuid: terminalUUID,
      amount: amount,
      scheme: scheme,
      callbacks: callbacks,
      transactionUuid: transactionUuid,
    );
  }
  Future<String> purchase({
    required String transactionUuid,
    required int amount,
    required String scheme,
    required PurchaseCallbacks callbacks,
  }) async {
    final sdk = FlutterTerminalSdk();
    return await sdk.purchase(
      uuid: terminalUUID,
      amount: amount,
      scheme: scheme,
      callbacks: callbacks,
      transactionUuid: transactionUuid,
    );
  }

  Future<String> refund({
    required String transactionUuid,
    required String refundUuid,
    required int amount,
    required String? scheme,
    required RefundCallbacks callbacks,
  }) async {
    final sdk = FlutterTerminalSdk();
    return await sdk.refund(
      terminalUuid: terminalUUID,
      amount: amount,
      scheme: scheme,
      callbacks: callbacks,
      transactionUuid: transactionUuid,
      refundUuid: refundUuid,
    );
  }

  Future<Receipts> getTransactionDetails({
    required String transactionUuid,
  }) async {
    final sdk = FlutterTerminalSdk();
    return await sdk.getTransactionDetails(
      terminalUuid: terminalUUID,
      transactionUuid: transactionUuid,
    );
  }

  Future<TransactionsResponse> getTransactionList({
    int? page,
    int? pageSize,
    bool? isReconciled,
    num? date,
    num? startDate,
    num? endDate,
    String? customerReferenceNumber,
  }) async {
    final sdk = FlutterTerminalSdk();
    return await sdk.getTransactionList(
      terminalUuid: terminalUUID,
      page: page,
      pageSize: pageSize,
      isReconciled: isReconciled,
      date: date,
      startDate: startDate,
      endDate: endDate,
      customerReferenceNumber: customerReferenceNumber,
    );
  }

  Future<ReconciliationResponse> getReconcileDetails({
    required String uuid,
  }) async {
    final sdk = FlutterTerminalSdk();
    return await sdk.getReconcileDetails(
      terminalUuid: terminalUUID,
      uuid: uuid,
    );
  }

  Future<ReconciliationListResponse> getReconcileList({
    int? page,
    int? pageSize,
    num? startDate,
    num? endDate,
  }) async {
    final sdk = FlutterTerminalSdk();
    return await sdk.getReconcileList(
      terminalUuid: terminalUUID,
      page: page,
      pageSize: pageSize,
      startDate: startDate,
      endDate: endDate,
    );
  }

  Future<ReconciliationReceiptsResponse> reconcile() async {
    final sdk = FlutterTerminalSdk();
    return await sdk.reconcile(terminalUUID: terminalUUID);
  }
}
