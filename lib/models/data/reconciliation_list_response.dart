import 'package:flutter_terminal_sdk/models/data/reconciliation_item.dart';

import '../transactions_response.dart';

class ReconciliationListResponse {
  final List<ReconciliationItem> data;
  final Pagination pagination;

  ReconciliationListResponse({
    required this.data,
    required this.pagination,
  });

  factory ReconciliationListResponse.fromJson(Map<String, dynamic> json) {
    return ReconciliationListResponse(
      data: (json['data'] as List<dynamic>)
          .map((e) => ReconciliationItem.fromJson(e as Map<String, dynamic>))
          .toList(),
      pagination:
          Pagination.fromJson(json['pagination'] as Map<String, dynamic>),
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'data': data.map((e) => e.toJson()).toList(),
      'pagination': pagination.toJson(),
    };
  }
}
