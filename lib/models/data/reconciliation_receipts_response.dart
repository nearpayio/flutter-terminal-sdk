import 'package:flutter_terminal_sdk/models/data/scheme.dart';

import 'details.dart';
import 'label_value.dart';
import 'language_content.dart';
import 'merchant.dart';

class ReconciliationReceiptsResponse {
  final ReconciliationReceipt receipt;

  ReconciliationReceiptsResponse({
    required this.receipt,
  });

  factory ReconciliationReceiptsResponse.fromJson(Map<String, dynamic> json) {
    return ReconciliationReceiptsResponse(
      receipt: ReconciliationReceipt.fromJson(
          json['receipt'] as Map<String, dynamic>),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'receipt': receipt.toJson(),
    };
  }
}

class ReconciliationReceipt {
  final String id;
  final String date;
  final String time;
  final String startDate;
  final String startTime;
  final String endDate;
  final String endTime;
  final Merchant merchant;
  final String cardAcceptorTerminalId;
  final String posSoftwareVersionNumber;
  final String cardSchemeSponsorId;
  final LabelValue<bool> isBalanced;
  final Details details;
  final List<Scheme> schemes;
  final LanguageContent currency;
  final String systemTraceAuditNumber;
  final String merchantId;
  final String deviceId;
  final String clientId;
  final Terminal terminal;
  final Reconciliation reconciliation;
  final String? userId;
  final String createdAt;
  final String updatedAt;
  final String qrCode;

  ReconciliationReceipt({
    required this.id,
    required this.date,
    required this.time,
    required this.startDate,
    required this.startTime,
    required this.endDate,
    required this.endTime,
    required this.merchant,
    required this.cardAcceptorTerminalId,
    required this.posSoftwareVersionNumber,
    required this.cardSchemeSponsorId,
    required this.isBalanced,
    required this.details,
    required this.schemes,
    required this.currency,
    required this.systemTraceAuditNumber,
    required this.merchantId,
    required this.deviceId,
    required this.clientId,
    required this.terminal,
    required this.reconciliation,
    this.userId,
    required this.createdAt,
    required this.updatedAt,
    required this.qrCode,
  });

  factory ReconciliationReceipt.fromJson(Map<String, dynamic> json) {
    return ReconciliationReceipt(
      id: json['id'] as String,
      date: json['date'] as String,
      time: json['time'] as String,
      startDate: json['start_date'] as String,
      startTime: json['start_time'] as String,
      endDate: json['end_date'] as String,
      endTime: json['end_time'] as String,
      merchant: Merchant.fromJson(json['merchant'] as Map<String, dynamic>),
      cardAcceptorTerminalId: json['card_acceptor_terminal_id'] as String,
      posSoftwareVersionNumber: json['pos_software_version_number'] as String,
      cardSchemeSponsorId: json['card_scheme_sponsor_id'] as String,
      isBalanced: LabelValue<bool>.fromJson(
          json['is_balanced'] as Map<String, dynamic>),
      details: Details.fromJson(json['details'] as Map<String, dynamic>),
      schemes: (json['schemes'] as List<dynamic>)
          .map((e) => Scheme.fromJson(e as Map<String, dynamic>))
          .toList(),
      currency:
          LanguageContent.fromJson(json['currency'] as Map<String, dynamic>),
      systemTraceAuditNumber: json['system_trace_audit_number'] as String,
      merchantId: json['merchant_id'] as String,
      deviceId: json['device_id'] as String,
      clientId: json['client_id'] as String,
      terminal: Terminal.fromJson(json['terminal'] as Map<String, dynamic>),
      reconciliation: Reconciliation.fromJson(
          json['reconciliation'] as Map<String, dynamic>),
      userId: json['user_id'] as String?,
      createdAt: json['created_at'] as String,
      updatedAt: json['updated_at'] as String,
      qrCode: json['qr_code'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'date': date,
      'time': time,
      'start_date': startDate,
      'start_time': startTime,
      'end_date': endDate,
      'end_time': endTime,
      'merchant': merchant.toJson(),
      'card_acceptor_terminal_id': cardAcceptorTerminalId,
      'pos_software_version_number': posSoftwareVersionNumber,
      'card_scheme_sponsor_id': cardSchemeSponsorId,
      'is_balanced': isBalanced.toJson(),
      'details': details.toJson(),
      'schemes': schemes.map((e) => e.toJson()).toList(),
      'currency': currency.toJson(),
      'system_trace_audit_number': systemTraceAuditNumber,
      'merchant_id': merchantId,
      'device_id': deviceId,
      'client_id': clientId,
      'terminal': terminal.toJson(),
      'reconciliation': reconciliation.toJson(),
      'user_id': userId,
      'created_at': createdAt,
      'updated_at': updatedAt,
      'qr_code': qrCode,
    };
  }
}

class Terminal {
  final String id;

  Terminal({
    required this.id,
  });

  factory Terminal.fromJson(Map<String, dynamic> json) {
    return Terminal(
      id: json['id'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
    };
  }
}

class Reconciliation {
  final String id;

  Reconciliation({
    required this.id,
  });

  factory Reconciliation.fromJson(Map<String, dynamic> json) {
    return Reconciliation(
      id: json['id'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
    };
  }
}
