//
//  VisitorClientDelegate.swift
//  Runner
//
//  Created by Denis Mikaya on 07.08.23.
//

import Foundation
import UnbluCoreSDK


class VisitorClientDelegate: UnbluVisitorClientDelegate {
    func unblu(didUpdatePersonActivityInfo personActivity: PersonActivityInfo) {
    }
    
    func unbluDidInitialize() {
    }
    
    func unbluDidDeinitialize() {
    }
    
    func unblu(didUpdateAgentAvailability isAvailable: Bool) {
    }
    
    func unblu(didUpdatePersonInfo personInfo: PersonInfo) {
    }
    
    func unblu(didUpdateUnreadMessages count: Int) {
    }
    
    func unblu(didChangeOpenConversation openConversation: UnbluConversation?) {
    }
    
    func unblu(didRequestHideUi reason: UnbluUiHideRequestReason, conversationId: String?) {
    }
    
    func unblu(didToggleCallUi isOpen: Bool) {
    }

    func unblu(didRequestShowUi withReason: UnbluUiRequestReason, requestedByUser: Bool) {
    }
    
    func unblu(didReceiveError type: UnbluClientErrorType, description: String) {
    }
}
