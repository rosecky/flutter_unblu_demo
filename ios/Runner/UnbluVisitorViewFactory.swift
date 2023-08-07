//
//  UnbluVisitorViewFactory.swift
//  Runner
//
//  Created by Denis Mikaya on 07.08.23.
//

import Flutter
import UIKit


class UnbluVisitorViewFactory: NSObject, FlutterPlatformViewFactory {
    private var messenger: FlutterBinaryMessenger

    init(messenger: FlutterBinaryMessenger) {
        self.messenger = messenger
        super.init()
    }

    func create(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?) -> FlutterPlatformView {
        return UnbluVisitorNativeView(
            frame: frame,
            viewIdentifier: viewId,
            arguments: args,
            binaryMessenger: messenger)
    }
}

class UnbluVisitorNativeView: NSObject, FlutterPlatformView {
    private var _view: UIView

    init(frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?, binaryMessenger messenger: FlutterBinaryMessenger?) {
        _view = UIView()
        super.init()
        createNativeView(view: _view)
    }

    func view() -> UIView {
        return _view
    }

    func createNativeView(view _view: UIView){
        
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        
        let unbluView = appDelegate.getUnbluVisitorView()
        unbluView.translatesAutoresizingMaskIntoConstraints = false
        _view.addSubview(unbluView)
        NSLayoutConstraint.activate([
            unbluView.topAnchor.constraint(equalTo: _view.topAnchor),
            unbluView.bottomAnchor.constraint(equalTo: _view.bottomAnchor),
            unbluView.trailingAnchor.constraint(equalTo: _view.trailingAnchor),
            unbluView.leadingAnchor.constraint(equalTo: _view.leadingAnchor)
        ])
        
        appDelegate.unbluVisitorStart()
        
    }
}
