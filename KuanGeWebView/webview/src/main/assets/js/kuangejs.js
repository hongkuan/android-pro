var kuangejs = {};
kuangejs.os = {};
kuangejs.os.isIOS = /iOS|iPhone|iPad|iPod/i.test(navigator.userAgent);
kuangejs.os.isAndroid = !kuangejs.os.isIOS;
kuangejs.callbacks = {};

kuangejs.callback = function (callbackname, response) {
    var callbackobject = kuangejs.callbacks[callbackname];
    console.log("xxxx"+callbackname);
    if (callbackobject !== undefined){
       if(callbackobject.callback != undefined){
          console.log("xxxxxx"+response);
            var ret = callbackobject.callback(response);
           if(ret === false){
               return
           }
           delete kuangejs.callbacks[callbackname];
       }
    }
}

kuangejs.takeNativeAction = function(commandname, parameters){
    console.log("kuangejs takenativeaction")
    var request = {};
    request.name = commandname;
    request.param = parameters;
    if(window.kuangejs.os.isAndroid){
        console.log("android take native action" + JSON.stringify(request));
        window.kuangewebview.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.kuangewebview.postMessage(JSON.stringify(request))
    }
}

kuangejs.takeNativeActionWithCallback = function(commandname, parameters, callback){
    var callbackname = "nativetojs_callback_" +  (new Date()).getTime() + "_" + Math.floor(Math.random() * 10000);
        kuangejs.callbacks[callbackname] = {callback:callback};

    var request = {};
    request.name = commandname;
    request.param = parameters;
    request.param.callbackname = callbackname;
    if(window.kuangejs.os.isAndroid){
        console.log("android take native action" + JSON.stringify(request));
        window.kuangewebview.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.kuangewebview.postMessage(JSON.stringify(request))
    }
}

window.kuangejs = kuangejs;
