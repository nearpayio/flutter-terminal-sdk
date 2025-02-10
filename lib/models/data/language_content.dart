class LanguageContent {
  final String arabic;
  final String english;

  LanguageContent({
    required this.arabic,
    required this.english,
  });

  factory LanguageContent.fromJson(Map<String, dynamic> json) {
    return LanguageContent(
      arabic: json['arabic'] as String,
      english: json['english'] as String,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'arabic': arabic,
      'english': english,
    };
  }
}
