# Im API 说明

由于 Swagger 限制，不能够为 Im 相关 API 自动生成 Api 说明，所以将 API 说明移动到 GitHub。

## 动态群组

API：`/group/{courseId}`

向该 API 发送（Send）的消息会被转发到 `/g/{courseId}` 的订阅者（Subscribers）。需要注意的是，**发送消息时需要附带 Authorization 字段，该字段的值（Token）用于验证用户身份**

### Android 发送消息：

```java
JSONObject jsonObject = new JSONObject();
try {
    jsonObject.put("content", name.getText().toString());
} catch (JSONException e) {
    e.printStackTrace();
}
if (group_id == null || group_id.length() == 0) {
    group_id = groupId.getText().toString();
}

String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRlIjoxNTIyODUwNjY3LCJleHAiOjE1MjM3MTQ2NjcsInVzZXJuYW1lIjoiMSJ9.B6movwo-HUlx5kQkSG8kFwup0OLbO4U6lwi2vdtIbKU";
StompHeader authorizationHeader = new StompHeader("Authorization", token);
stompClient.send(new StompMessage(
        StompCommand.SEND,
        // 第一个 StompHeader 是必需的，第二个是我们手动添加的 Authorization 字段
        Arrays.asList(new StompHeader(StompHeader.DESTINATION, Const.group.replace("placeholder", group_id)), authorizationHeader),
        jsonObject.toString()
)).subscribe(new Subscriber<Void>() {
    @Override
    public void onSubscribe(Subscription s) {
        Log.i(Const.TAG, "订阅成功");
    }

    @Override
    public void onNext(Void aVoid) {

    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
        Log.e(Const.TAG, "发生错误：", t);
    }

    @Override
    public void onComplete() {
        Log.i(Const.TAG, "发送消息成功！");
    }
});
```

### 接收到的消息：

如果 Token 非法，则返回：

```json
{
    "message" : "Token 内容非法！",
    "status" : "FAILED"
}
```

如果 Token 合法，则返回：

```json
{
    "message" : {
        "id" : 32,
        "content" : "dasdsa",
        "date" : "2018-04-13 16:14:47",
        "messageType" : "GROUP",
        "courseId" : 21,
        "user" : {
            "nickname" : "1",
            "avatar" : "https://api.fc.xd.style/avatar",
            "role" : {
                "role" : "student"
            }
        }
    },
    "status" : "SUCCESS"
}
```
