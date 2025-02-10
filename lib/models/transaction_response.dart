// lib/models/transaction_response.dart

import 'dart:convert';

class TransactionResponse {
  final String id;
  final String actionCode;
  final String type;
  final Status status;
  final String amount;
  final String currency;
  final String? createdAt;
  final String? completedAt;
  final String? canceledAt;
  final String? cancelReason;
  final String? referenceId;
  final String? orderId;
  final String? approvalCode;
  final bool? pinRequired;
  final Terminal? terminal;
  final int? attempts;
  final Map<String, dynamic>? user;
  final Map<String, dynamic>? device;
  final Map<String, dynamic>? app;
  final Card card;
  final List<Event> events;

  TransactionResponse({
    required this.id,
    required this.actionCode,
    required this.type,
    required this.status,
    required this.amount,
    required this.currency,
    this.createdAt,
    this.completedAt,
    this.canceledAt,
    this.cancelReason,
    this.referenceId,
    this.orderId,
    this.approvalCode,
    this.pinRequired,
    this.terminal,
    this.attempts,
    this.user,
    this.device,
    this.app,
    required this.card,
    required this.events,
  });

  factory TransactionResponse.fromJson(Map<String, dynamic> json) {
    return TransactionResponse(
      id: json['id'] as String,
      actionCode: json['action_code'] as String,
      type: json['type'] as String,
      status: StatusExtension.fromString(json['status'] as String),
      amount: json['amount'] as String,
      currency: json['currency'] as String,
      createdAt: json['created_at'] as String?,
      completedAt: json['completed_at'] as String?,
      canceledAt: json['canceled_at'] as String?,
      cancelReason: json['cancel_reason'] as String?,
      referenceId: json['reference_id'] as String?,
      orderId: json['order_id'] as String?,
      approvalCode: json['approval_code'] as String?,
      pinRequired: json['pin_required'] as bool?,
      terminal: json['terminal'] != null
          ? Terminal.fromJson(json['terminal'] as Map<String, dynamic>)
          : null,
      attempts: json['attempts'] as int?,
      user: json['user'] as Map<String, dynamic>?,
      device: json['device'] as Map<String, dynamic>?,
      app: json['app'] as Map<String, dynamic>?,
      card: Card.fromJson(json['card'] as Map<String, dynamic>),
      events: (json['events'] as List<dynamic>)
          .map((e) => Event.fromJson(e as Map<String, dynamic>))
          .toList(),
    );
  }

  Map<String, dynamic> toJson() => {
    'id': id,
    'action_code': actionCode,
    'type': type,
    'status': status.toShortString(),
    'amount': amount,
    'currency': currency,
    'created_at': createdAt,
    'completed_at': completedAt,
    'canceled_at': canceledAt,
    'cancel_reason': cancelReason,
    'reference_id': referenceId,
    'order_id': orderId,
    'approval_code': approvalCode,
    'pin_required': pinRequired,
    'terminal': terminal?.toJson(),
    'attempts': attempts,
    'user': user,
    'device': device,
    'app': app,
    'card': card.toJson(),
    'events': events.map((e) => e.toJson()).toList(),
  };
}

enum Status { approved, declined }

extension StatusExtension on Status {
  static Status fromString(String status) {
    switch (status.toLowerCase()) {
      case 'approved':
        return Status.approved;
      case 'declined':
        return Status.declined;
      default:
        throw ArgumentError('Unknown status: $status');
    }
  }

  String toShortString() {
    return toString().split('.').last.toLowerCase();
  }
}

class Terminal {
  final String terminalId;
  final String bankId;
  final String merchantId;
  final String vendorId;
  final String merchantCategoryCode;

  Terminal({
    required this.terminalId,
    required this.bankId,
    required this.merchantId,
    required this.vendorId,
    required this.merchantCategoryCode,
  });

  factory Terminal.fromJson(Map<String, dynamic> json) {
    return Terminal(
      terminalId: json['terminal_id'] as String,
      bankId: json['bank_id'] as String,
      merchantId: json['merchant_id'] as String,
      vendorId: json['vendor_id'] as String,
      merchantCategoryCode: json['merchant_category_code'] as String,
    );
  }

  Map<String, dynamic> toJson() => {
    'terminal_id': terminalId,
    'bank_id': bankId,
    'merchant_id': merchantId,
    'vendor_id': vendorId,
    'merchant_category_code': merchantCategoryCode,
  };
}

class Card {
  final String pan;
  final String exp;

  Card({
    required this.pan,
    required this.exp,
  });

  factory Card.fromJson(Map<String, dynamic> json) {
    return Card(
      pan: json['pan'] as String,
      exp: json['exp'] as String,
    );
  }

  Map<String, dynamic> toJson() => {
    'pan': pan,
    'exp': exp,
  };
}

class Event {
  final String rrn;
  final String stan;
  final String type;
  final Status status;
  final Receipt receipt;

  Event({
    required this.rrn,
    required this.stan,
    required this.type,
    required this.status,
    required this.receipt,
  });

  factory Event.fromJson(Map<String, dynamic> json) {
    return Event(
      rrn: json['rrn'] as String,
      stan: json['stan'] as String,
      type: json['type'] as String,
      status: StatusExtension.fromString(json['status'] as String),
      receipt: Receipt.fromJson(json['receipt'] as Map<String, dynamic>),
    );
  }

  Map<String, dynamic> toJson() => {
    'rrn': rrn,
    'stan': stan,
    'type': type,
    'status': status.toShortString(),
    'receipt': receipt.toJson(),
  };
}

class Receipt {
  final String type;
  final Map<String, dynamic> data;

  Receipt({
    required this.type,
    required this.data,
  });

  factory Receipt.fromJson(Map<String, dynamic> json) {
    return Receipt(
      type: json['type'] as String,
      data: json['data'] as Map<String, dynamic>,
    );
  }

  Map<String, dynamic> toJson() => {
    'type': type,
    'data': data,
  };
}
