import 'package:pigeon/pigeon.dart';

// Define the data structure for PersonInfo in Dart
class PersonInfo {
  late String id;
  late String displayName;

  PersonInfo({required this.id, required this.displayName});
}

@HostApi()
abstract class UnbluFlutterToVisitorHostApi {
  @async
  bool createVisitor();
  @async
  void deinitializeVisitor();
  @async
  bool isInitialized();
  PersonInfo? getPersonInfo();
  int getUnreadMessagesCount();
  bool isDeInitialized();
  @async
  bool openConversation(String conversationId);
  @async
  bool openConversationOverview();
  void setAccessToken(String token);
  void setCustomCookies(Map<String, String> customCookies);
}

@FlutterApi()
abstract class UnbluVisitorHostToFlutterApi {
  void showUnblu();
  void hideUnblu();
  bool handleBackButton(bool hasBackStack);
  void displayMessage(String message);
  void updateUnreadMessage(int updateUnreadMessages);
  void onAgentAvailable(bool isAvailable);
  void onUnbluError(String errorType, String message);
  void onVisitorInitChanged(bool initState);
}
