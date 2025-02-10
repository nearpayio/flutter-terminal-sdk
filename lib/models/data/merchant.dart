import 'language_content.dart';

class Merchant {
  final String id;
  final LanguageContent name;
  final LanguageContent address;
  final String categoryCode;

  Merchant({
    required this.id,
    required this.name,
    required this.address,
    required this.categoryCode,
  });

  factory Merchant.fromJson(Map<String, dynamic> json) {
    return Merchant(
      id: json['id'] as String,
      name: LanguageContent.fromJson(json['name'] as Map<String, dynamic>),
      address: LanguageContent.fromJson(json['address'] as Map<String, dynamic>),
      categoryCode: json['categoryCode'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'name': name.toJson(),
      'address': address.toJson(),
      'categoryCode': categoryCode,
    };
  }
}
