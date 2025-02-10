// lib/flutter_terminal_sdk.dart

import 'dart:convert';
import 'package:flutter/services.dart';
import 'errors/errors.dart';
import 'models/data/reconcile_response.dart';
import 'models/data/reconciliation_list_response.dart';
import 'models/data/reconciliation_receipts_response.dart';
import 'models/nearpay_user_response.dart';
import 'models/refund_callbacks.dart';
import 'models/send_otp_response.dart';
import 'models/terminal_connection_response.dart';
import 'models/terminal_response.dart';
import 'models/purchase_callbacks.dart';
import 'models/transaction_details_response.dart';
import 'models/transaction_response.dart';
import 'models/transactions_response.dart';

/// Enumerations for environment/country
enum Environment { sandbox, production }

enum Country { sa, tr, usa }

class FlutterTerminalSdk {
  static final FlutterTerminalSdk _instance = FlutterTerminalSdk._internal();

  factory FlutterTerminalSdk() {
    return _instance;
  }

  FlutterTerminalSdk._internal()
      : _channel = const MethodChannel('nearpay_plugin') {
    _channel.setMethodCallHandler(_handleMethodCall);
  }

  final MethodChannel _channel;

  bool _initialized = false;

  bool get isInitialized => _initialized;

  final Map<String, PurchaseCallbacks> _activePurchaseCallbacks = {};
  final Map<String, RefundCallbacks> _activeRefundCallbacks = {};

  Future<Map<String, dynamic>> _callAndReturnMapResponse(
    String methodName,
    dynamic data,
  ) async {
    if (!_initialized) {
      throw NearpayException("Cannot call $methodName before initialization");
    }
    final tempResponse = await _channel.invokeMethod(methodName, data);
    return jsonDecode(jsonEncode(tempResponse));
  }

  /// Initialize the SDK
  Future<void> initialize({
    required Environment environment,
    required int googleCloudProjectNumber,
    required String huaweiSafetyDetectApiKey,
    required Country country,
  }) async {
    final environmentString = environment.name; // "sandbox" or "production"
    final countryString = country.name; // "sa", "tr", or "usa"

    try {
      await _channel.invokeMethod('initialize', {
        'environment': environmentString,
        'googleCloudProjectNumber': googleCloudProjectNumber,
        'huaweiSafetyDetectApiKey': huaweiSafetyDetectApiKey,
        'country': countryString,
      });
      _initialized = true;
    } catch (e) {
      _initialized = false;
      throw NearpayException("Failed to initialize Nearpay Terminal SDK: $e");
    }
  }

  /// Send Mobile OTP
  Future<OtpResponse> sendMobileOtp(String mobileNumber) async {
    final response = await _callAndReturnMapResponse(
      'sendMobileOtp',
      {"mobileNumber": mobileNumber},
    );

    if (response["status"] == "success") {
      final data = response["data"];
      return OtpResponse.fromJson(Map<String, dynamic>.from(data));
    } else {
      final errorMsg = response["message"] ?? "Failed to send mobile OTP";
      throw NearpayException(errorMsg);
    }
  }

  /// Send Email OTP
  Future<OtpResponse> sendEmailOtp(String email) async {
    final response = await _callAndReturnMapResponse(
      'sendEmailOtp',
      {"email": email},
    );

    if (response["status"] == "success") {
      final data = response["data"];
      return OtpResponse.fromJson(Map<String, dynamic>.from(data));
    } else {
      final errorMsg = response["message"] ?? "Failed to send email OTP";
      throw NearpayException(errorMsg);
    }
  }

  /// Verify Mobile OTP
  Future<NearpayUser> verifyMobileOtp({
    required String mobileNumber,
    required String code,
  }) async {
    final response = await _callAndReturnMapResponse(
      'verifyMobileOtp',
      {
        'mobileNumber': mobileNumber,
        'code': code,
        // 'deviceToken': deviceToken,
      },
    );

    if (response["status"] == "success") {
      final data = response["data"];
      return NearpayUser.fromJson(Map<String, dynamic>.from(data));
    } else {
      final errorMsg = response["message"] ?? "Failed to verify mobile OTP";
      throw NearpayException(errorMsg);
    }
  }

  /// Verify Email OTP
  Future<NearpayUser> verifyEmailOtp({
    required String email,
    required String code,
  }) async {
    final response = await _callAndReturnMapResponse(
      'verifyEmailOtp',
      {
        'email': email,
        'code': code,
      },
    );

    if (response["status"] == "success") {
      final data = response["data"];
      return NearpayUser.fromJson(Map<String, dynamic>.from(data));
    } else {
      final errorMsg = response["message"] ?? "Failed to verify email OTP";
      throw NearpayException(errorMsg);
    }
  }

  /// Verify Email OTP
  Future<TerminalModel> jwtLogin({
    required String jwt,
  }) async {
    final response = await _callAndReturnMapResponse(
      'jwtVerify',
      {
        'jwt': jwt,
      },
    );

    if (response["status"] == "success") {
      final data = response['data'] as Map<String, dynamic>;
      return TerminalModel.fromJson(data);
    } else {
      final errorMsg = response["message"] ?? "Failed to verify email OTP";
      throw NearpayException(errorMsg);
    }
  }

  /// get User
  Future<NearpayUser> getUser({
    required String uuid,
  }) async {
    final response = await _callAndReturnMapResponse(
      'getUser',
      {
        'uuid': uuid,
      },
    );

    if (response["status"] == "success") {
      final data = response["data"];
      return NearpayUser.fromJson(Map<String, dynamic>.from(data));
    } else {
      final errorMsg = response["message"] ?? "Failed to get user";
      throw NearpayException(errorMsg);
    }
  }

  /// logout
  Future<String> logout({
    required String uuid,
  }) async {
    final response = await _callAndReturnMapResponse(
      'logout',
      {
        'uuid': uuid,
      },
    );

    if (response["status"] == "success") {
      return response["message"];
    } else {
      final errorMsg = response["message"] ?? "Failed to logout";
      throw NearpayException(errorMsg);
    }
  }

  /// get terminal
  Future<TerminalModel> getTerminal({
    required String tid,
  }) async {
    final response = await _callAndReturnMapResponse(
      'getTerminal',
      {
        'tid': tid,
      },
    );

    if (response["status"] == "success") {
      final data = response['data'] as Map<String, dynamic>;
      return TerminalModel.fromJson(data);
    } else {
      final errorMsg = response["message"] ?? "Failed to get user";
      throw NearpayException(errorMsg);
    }
  }

  /// Purchase
  Future<String> purchase({
    required String transactionUuid,
    required String uuid,
    required int amount,
    required String? scheme,
    required PurchaseCallbacks callbacks,
  }) async {
    if (!_initialized) {
      throw NearpayException("SDK not initialized. Call initialize() first.");
    }
    _activePurchaseCallbacks[transactionUuid] = callbacks;

    try {
      final response = await _callAndReturnMapResponse(
        'purchase',
        {
          "uuid": uuid,
          "amount": amount,
          "scheme": scheme,
          "transactionUuid": transactionUuid,
        },
      );

      if (response["status"] == "success") {
        return transactionUuid;
      } else {
        _activePurchaseCallbacks.remove(transactionUuid);
        throw NearpayException(response["message"] ?? "Purchase failed");
      }
    } catch (e) {
      _activePurchaseCallbacks.remove(transactionUuid);
      rethrow;
    }
  }

  /// Refund
  Future<String> refund({
    required String terminalUuid,
    required String transactionUuid,
    required String refundUuid,
    required int amount,
    required String? scheme,
    required RefundCallbacks callbacks,
  }) async {
    if (!_initialized) {
      throw NearpayException("SDK not initialized. Call initialize() first.");
    }
    _activeRefundCallbacks[transactionUuid] = callbacks;

    try {
      final response = await _callAndReturnMapResponse(
        'refund',
        {
          "terminalUUID": terminalUuid,
          "transactionUuid": transactionUuid,
          "refundUuid": refundUuid,
          "amount": amount,
          "scheme": scheme,
        },
      );

      if (response["status"] == "success") {
        return refundUuid;
      } else {
        _activeRefundCallbacks.remove(transactionUuid);
        throw NearpayException(response["message"] ?? "Refund failed");
      }
    } catch (e) {
      _activeRefundCallbacks.remove(transactionUuid);
      rethrow;
    }
  }

  /// Get Transaction Details
  Future<Receipts> getTransactionDetails({
    required String terminalUuid,
    required String transactionUuid,
  }) async {
    if (!_initialized) {
      throw NearpayException("SDK not initialized. Call initialize() first.");
    }

    try {
      final response = await _callAndReturnMapResponse(
        'getTransactionDetails',
        {
          "terminalUUID": terminalUuid,
          "transactionUUID": transactionUuid,
        },
      );

      if (response["status"] == "success") {
        return  Receipts.fromJson(response['data']);

        // return TransactionResponse.fromJson(response['data']["receipts"][0]);
      } else {
        throw NearpayException(
            response["message"] ?? "Get Transaction Details Failed");
      }
    } catch (e) {
      rethrow;
    }
  }

  /// Get Transaction List
  Future<TransactionsResponse> getTransactionList({
    required String terminalUuid,
    int? page,
    int? pageSize,
    bool? isReconciled,
    num? date,
    num? startDate,
    num? endDate,
    String? customerReferenceNumber,
  }) async {
    if (!_initialized) {
      throw NearpayException("SDK not initialized. Call initialize() first.");
    }
    final result = await _callAndReturnMapResponse(
      'getTransactionList',
      {
        "terminalUUID": terminalUuid,
        "page": page,
        "pageSize": pageSize,
        "isReconciled": isReconciled,
        "date": date,
        "startDate": startDate,
        "endDate": endDate,
        "customerReferenceNumber": customerReferenceNumber,
      },
    );
    final status = result['status'];
    if (status == 'success') {
      final data = result['data'] as Map<String, dynamic>;
      return TransactionsResponse.fromJson(data);
    } else {
      final message = result['message'] ?? 'Unknown error';
      throw NearpayException('Failed to retrieve Transactions: $message');
    }
  }

  /// Get Transaction Details
  Future<ReconciliationResponse> getReconcileDetails({
    required String terminalUuid,
    required String uuid,
  }) async {
    if (!_initialized) {
      throw NearpayException("SDK not initialized. Call initialize() first.");
    }

    try {
      final response = await _callAndReturnMapResponse(
        'getReconcileDetails',
        {
          "terminalUUID": terminalUuid,
          "uuid": uuid,
        },
      );

      if (response["status"] == "success") {
        final data = response['data'] as Map<String, dynamic>;
        return ReconciliationResponse.fromJson(data);
      } else {
        throw NearpayException(
            response["message"] ?? "Get Reconcile Details Failed");
      }
    } catch (e) {
      rethrow;
    }
  }

  /// Get Transaction List
  Future<ReconciliationListResponse> getReconcileList({
    required String terminalUuid,
    int? page,
    int? pageSize,
    num? startDate,
    num? endDate,
  }) async {
    if (!_initialized) {
      throw NearpayException("SDK not initialized. Call initialize() first.");
    }
    final result = await _callAndReturnMapResponse(
      'getReconcileList',
      {
        "terminalUUID": terminalUuid,
        "page": page,
        "pageSize": pageSize,
        "startDate": startDate,
        "endDate": endDate,
      },
    );
    final status = result['status'];
    if (status == 'success') {
      final data = result['data'] as Map<String, dynamic>;
      return ReconciliationListResponse.fromJson(data);
    } else {
      final message = result['message'] ?? 'Unknown error';
      throw NearpayException('Failed to retrieve Reconcile: $message');
    }
  }

  /// getTerminalList
  Future<List<TerminalConnectionModel>> getTerminalList(String uuid) async {
    final result = await _callAndReturnMapResponse(
      'getTerminalList',
      {'uuid': uuid},
    );
    // TODO: Add page & pageSize
    final status = result['status'];
    if (status == 'success') {
      final data = result['data'] as Map<String, dynamic>;
      final terminals = data['terminals'] as List<dynamic>;
      return terminals.map((terminalJson) {
        return TerminalConnectionModel.fromJson(
          terminalJson as Map<String, dynamic>,
        );
      }).toList();
    } else {
      final message = result['message'] ?? 'Unknown error';
      throw NearpayException('Failed to retrieve terminals: $message');
    }
  }

  /// connectTerminal
  Future<TerminalModel> connectTerminal({
    required String tid,
    required String userUUID,
    required String terminalUUID,
  }) async {
    final response = await _callAndReturnMapResponse(
      'connectTerminal',
      {
        'tid': tid,
        'userUUID': userUUID,
        'terminalUUID': terminalUUID,
      },
    );

    final status = response['status'];
    if (status == 'success') {
      final data = response['data'] as Map<String, dynamic>;
      return TerminalModel.fromJson(data);
    } else {
      final message = response['message'] ?? 'Failed to connect to terminal';
      throw NearpayException(message);
    }
  }

  /// connectTerminal
  Future<ReconciliationReceiptsResponse> reconcile({
    required String terminalUUID,
  }) async {
    final response = await _callAndReturnMapResponse(
      'reconcile',
      {
        'terminalUUID': terminalUUID,
      },
    );

    final status = response['status'];
    if (status == 'success') {
      final data = response['data'] as Map<String, dynamic>;
      return ReconciliationReceiptsResponse.fromJson(data);
    } else {
      final message = response['message'] ?? 'Failed to connect to terminal';
      throw NearpayException(message);
    }
  }

  Future<void> _handleMethodCall(MethodCall call) async {
    if (call.method == 'purchaseEvent') {
      final args = Map<String, dynamic>.from(call.arguments);
      final transactionUuid = args['transactionUuid'] as String;
      final eventType = args['type'] as String;
      final message = args['message'] as String?;
      final data = args['data'] as Map<String, dynamic>?;

      final callbacks = _activePurchaseCallbacks[transactionUuid];
      if (callbacks == null) {
        return;
      }

      switch (eventType) {
        case 'readingStarted':
          callbacks.onReadingStarted?.call();
          break;
        case 'readerWaiting':
          callbacks.onReaderWaiting?.call();
          break;
        case 'readerReading':
          callbacks.onReaderReading?.call();
          break;
        case 'readerRetry':
          callbacks.onReaderRetry?.call();
          break;
        case 'pinEntering':
          callbacks.onPinEntering?.call();
          break;
        case 'readerFinished':
          callbacks.onReaderFinished?.call();
          break;
        case 'readerError':
          if (message != null) {
            callbacks.onReaderError?.call(message);
          }
          break;
        case 'cardReadSuccess':
          callbacks.onCardReadSuccess?.call();
          break;
        case 'cardReadFailure':
          if (message != null) {
            callbacks.onCardReadFailure?.call(message);
          }
          break;
        case 'sendTransactionSuccess':
          callbacks.onSendTransactionSuccess?.call();
          break;
        case 'sendTransactionFailure':
          if (message != null) {
            callbacks.onSendTransactionFailure?.call(message);
          }
          break;
        case 'sendTransactionSuccessData':
          if (data != null) {
            final transactionResponse = TransactionResponse.fromJson(data);
            callbacks.onSendTransactionSuccessData?.call(transactionResponse);
          }
          break;
        default:
          break;
      }

      if (eventType == 'sendTransactionSuccessData' ||
          eventType == 'sendTransactionFailure') {
        _activePurchaseCallbacks.remove(transactionUuid);
      }
    } else if (call.method == 'refundEvent') {
      final args = Map<String, dynamic>.from(call.arguments);
      final transactionUuid = args['transactionUuid'] as String;
      final eventType = args['type'] as String;
      final message = args['message'] as String?;
      final data = args['data'] as Map<String, dynamic>?;

      final callbacks = _activeRefundCallbacks[transactionUuid];
      if (callbacks == null) {
        return;
      }

      switch (eventType) {
        case 'readingStarted':
          callbacks.cardReaderCallbacks?.onReadingStarted?.call();
          break;
        case 'readerWaiting':
          callbacks.cardReaderCallbacks?.onReaderWaiting?.call();
          break;
        case 'readerReading':
          callbacks.cardReaderCallbacks?.onReaderReading?.call();
          break;
        case 'readerRetry':
          callbacks.cardReaderCallbacks?.onReaderRetry?.call();
          break;
        case 'pinEntering':
          callbacks.cardReaderCallbacks?.onPinEntering?.call();
          break;
        case 'readerFinished':
          callbacks.cardReaderCallbacks?.onReaderFinished?.call();
          break;
        case 'readerError':
          if (message != null) {
            callbacks.cardReaderCallbacks?.onReaderError?.call(message);
          }
          break;
        case 'cardReadSuccess':
          callbacks.cardReaderCallbacks?.onCardReadSuccess?.call();
          break;
        case 'cardReadFailure':
          if (message != null) {
            callbacks.cardReaderCallbacks?.onCardReadFailure?.call(message);
          }
          break;
        case 'sendTransactionFailure':
          if (message != null) {
            callbacks.onSendTransactionFailure?.call(message);
          }
          break;
        case 'sendTransactionSuccess':
          if (data != null) {
            final transactionResponse = TransactionResponse.fromJson(data);
            callbacks.onSendTransactionSuccess?.call(transactionResponse);
          }
          break;
        default:
          break;
      }

      if (eventType == 'sendTransactionSuccess' ||
          eventType == 'sendTransactionFailure') {
        _activeRefundCallbacks.remove(transactionUuid);
      }
    }
  }

  void dispose() {
    _activePurchaseCallbacks.clear();
    _activeRefundCallbacks.clear();
  }
}
