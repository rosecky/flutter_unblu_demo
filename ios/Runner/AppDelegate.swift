import UIKit
import Flutter
import UnbluCoreSDK
import UnbluFirebaseNotificationModule
import UnbluMobileCoBrowsingModule
//import UnbluCallModule
import FirebaseMessaging



@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    let serverUrl = "http://192.168.1.159:7777"
    let apiKey = "MZsy5sFESYqU7MawXZgR_w"
    
    var callModule: UnbluCallModuleApi?
    var mobileCoBrowsingModule: UnbluMobileCoBrowsingModuleApi?
    var unbluVisitor: UnbluVisitorClient?
    var unbluAgent: UnbluAgentClient?
    var coordinator: FirebaseDelegate?
    var userNotificationCenter : NotificationCenterDelegate?
    
    var visitorClientDelegate : VisitorClientDelegate?
    var callModuleDelegate : CallModuleDelegate?
    var coBrowsingDelegate : CoBrowsingDelegate?
    
    
    
  override func application( _ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? ) -> Bool {
      
      if  createVisitorClient() {
          coordinator?.application(application, didFinishLaunchingWithOptions: launchOptions)
      }
      
      
    GeneratedPluginRegistrant.register(with: self)
      
      weak var registrar = self.registrar(forPlugin: "Unblu")
      let unbluVisitorFactory = UnbluVisitorViewFactory(messenger: registrar!.messenger())
      _ = UnbluVisitorViewFactory(messenger: registrar!.messenger())
      registrar!.register(unbluVisitorFactory,withId: "UnbluVisitorView")
      
      return super.application(application, didFinishLaunchingWithOptions: launchOptions)
      
  }
    
    override func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
          Messaging.messaging().apnsToken = deviceToken;
      }
    
    //Called when received a background remote notification.
    override func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any]) {
          
          do {
              // the notification will be decrypted and delivered as a local notification
              try UnbluNotificationApi.instance.handleRemoteNotification(userInfo: userInfo,withCompletionHandler: {_ in
                  // if it is endCall or readMessage notifications (silent)
              })
          } catch {
              // if this not an unblu notification , call default implementation
              coordinator?.on_application(application, didReceiveRemoteNotification: userInfo)
          }
      }

      //Called when received a background remote notification.
    override func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
          
          do {
              // Important! if the notification version is encrypted use this method instead 'handleRemoteNotification(userInfo:  [AnyHashable: Any])'
              //the notification will be decrypted and delivered as a local notification
              try UnbluNotificationApi.instance.handleRemoteNotification(userInfo: userInfo,withCompletionHandler: {_ in
                  // if it is endCall or readMessage notifications (silent)
              })
          } catch {
              // if this not an unblu notification , call default implementation
              coordinator?.on_application(application, didReceiveRemoteNotification: userInfo, fetchCompletionHandler: completionHandler)
          }
      }
}



extension AppDelegate {
    
    
    func getUnbluVisitorView() -> UIView {
        return unbluVisitor!.view
    }
    
    func createVisitorClient() -> Bool {
        // Set Icon for CallKit UI
        UnbluClientConfiguration.callKitProviderIconResourceName = "AppIcon"
        
        //1. Register modules
        var config = createUnbluConfig()
        config.unbluPushNotificationVersion = .Encrypted
        
        callModule = UnbluCallModuleProvider.create()
        try! config.register(module: callModule!)
        callModuleDelegate = CallModuleDelegate()
        callModule?.delegate = callModuleDelegate
        
        let mobileCoBrowsingModuleConfig = UnbluMobileCoBrowsingModuleConfiguration(enableCapturingPerformanceLogging: true)
        mobileCoBrowsingModule = UnbluMobileCoBrowsingModuleProvider.create(config: mobileCoBrowsingModuleConfig)
        try! config.register(module: mobileCoBrowsingModule!)
        coBrowsingDelegate = CoBrowsingDelegate()
        mobileCoBrowsingModule?.delegate = coBrowsingDelegate
        
        //2 Set NotificationCenter delegate
        userNotificationCenter = NotificationCenterDelegate()
        UNUserNotificationCenter.current().delegate = userNotificationCenter
        
        //3. Create client , register for PushKit notifications
        unbluVisitor = Unblu.createVisitorClient(withConfiguration: config)
        unbluVisitor?.logLevel = .verbose
        unbluVisitor?.enableDebugOutput = true
        visitorClientDelegate = VisitorClientDelegate()
        unbluVisitor?.visitorDelegate = visitorClientDelegate
        
        //4. Init Firebase , register for Push notifications
        coordinator = FirebaseDelegate()
        return true
        
    }
    
    func createUnbluConfig() -> UnbluClientConfiguration {
        var configuration = UnbluClientConfiguration(unbluBaseUrl: serverUrl,
                                                     apiKey:  apiKey,
                                                     preferencesStorage: UserDefaultsPreferencesStorage(),
                                                     fileDownloadHandler: UnbluDefaultFileDownloadHandler(),
                                                     externalLinkHandler: UnbluDefaultExternalLinkHandler())
        return configuration
    }
    
}



extension AppDelegate {
    
    func unbluVisitorStart()    {
            self.unbluVisitor!.start { result in
                switch result {
                case .success():
                    print("ok")
                case .failure(let error):
                    print(error)
                }
            }
    }
    
    func unbluVisitorStop() async throws {
            self.unbluVisitor!.stop { result in
                switch result {
                case .success():
                    print("ok")
                case .failure(let error):
                    print(error)
                }
            }
    }
    
}
