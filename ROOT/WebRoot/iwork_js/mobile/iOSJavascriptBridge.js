/**
 * 消息类型
 */
var CONST_BRIDGE_TYPE_ACTION = "CONST_BRIDGE_TYPE_ACTION";

/**
 * 消息类型
 */
var CONST_BRIDGE_TYPE_MESSAGE = "CONST_BRIDGE_TYPE_MESSAGE";

/**
 * 弹出一个提示窗口
 */
var CONST_BRIDGE_ACTION_SHOW_ALERT = "CONST_BRIDGE_ACTION_SHOW_ALERT";

/**
 * 返回上一个窗口
 */
var CONST_BRIDGE_ACTION_BACK = "CONST_BRIDGE_ACTION_BACK";

/**
 * 返回到列表
 */
var CONST_BRIDGE_ACTION_BACK_TO_LIST = "CONST_BRIDGE_ACTION_BACK_TO_LIST";

/**
 * 返回到HOME
 */
var CONST_BRIDGE_ACTION_BACK_HOME = "CONST_BRIDGE_ACTION_BACK_HOME";

/**
 * 返回到Login
 */
var CONST_BRIDGE_ACTION_BACK_TO_LOGIN = "CONST_BRIDGE_ACTION_BACK_TO_LOGIN";

/**
 * 下一个
 */
var CONST_BRIDGE_ACTION_FORWARD = "CONST_BRIDGE_ACTION_FORWARD";

var globalBridge = "";
document.addEventListener('WebViewJavascriptBridgeReady', function onBridgeReady(event) {
                          var bridge = event.bridge
                          bridge.init(function(message, responseCallback) {
                                      bridgeHandleReceivedMessage(message);
                                      if (responseCallback) {
                                      responseCallback("Right back atcha")
                                      }
                                      })
                          globalBridge = bridge;
                          }, false)

/**
 * 由javascript向native发送一个消息，内容为message
 */
function bridgeSendMessageToiOS(message){
    globalBridge.send(message)
};

/**
 * javascript接收到的native发送来的消息，内容为message
 */
function bridgeHandleReceivedMessage(message){
    //alert(message)
}

/**
 * 弹出一个提示窗口
 * message:提示内容
 */
function bridgeShowAlert(message){
    var JSONObject = {
    CONST_BRIDGE_TYPE_ACTION:CONST_BRIDGE_ACTION_SHOW_ALERT,
        CONST_BRIDGE_TYPE_MESSAGE:message};
    bridgeSendMessageToiOS(JSONObject);
}

/**
 * 返回到列表
 */
function bridgeBackToList(){
    var JSONObject = {
    CONST_BRIDGE_TYPE_ACTION:CONST_BRIDGE_ACTION_BACK_TO_LIST,
        CONST_BRIDGE_TYPE_MESSAGE:""};
    bridgeSendMessageToiOS(JSONObject);
}

/**
 * 返回到HOME
 */
function bridgeBackHome(){
    var JSONObject = {
    CONST_BRIDGE_TYPE_ACTION:CONST_BRIDGE_ACTION_BACK_HOME,
        CONST_BRIDGE_TYPE_MESSAGE:""};
    bridgeSendMessageToiOS(JSONObject);
}

/**
 * 返回到Login
 */
function bridgeBackToLogin(){
    var JSONObject = {
    CONST_BRIDGE_TYPE_ACTION:CONST_BRIDGE_ACTION_BACK_TO_LOGIN,
        CONST_BRIDGE_TYPE_MESSAGE:""};
    bridgeSendMessageToiOS(JSONObject);
}


/**
 * 返回到上一个窗口
 */
function bridgeBack(){
    bridgeBackWithObject("");
}

/**
 * 返回到上一个窗口
 * message:需要传递的参数
 */
function bridgeBackWithObject(message){
    var JSONObject = {
    CONST_BRIDGE_TYPE_ACTION:CONST_BRIDGE_ACTION_BACK,
        CONST_BRIDGE_TYPE_MESSAGE:message};
    bridgeSendMessageToiOS(JSONObject);
}

/**
 * 打开下一个窗口
 */
function bridgeForward(){
    bridgeForwardWithObject("","");
}

/**
 * 打开下一个窗口
 * message:需要传递的参数
 */
function bridgeForwardWithObject(title,url){
    var message = {
    TITLE:title,
        URL:url};
    var JSONObject = {
    CONST_BRIDGE_TYPE_ACTION:CONST_BRIDGE_ACTION_FORWARD,
        CONST_BRIDGE_TYPE_MESSAGE:message};
    bridgeSendMessageToiOS(JSONObject);
}