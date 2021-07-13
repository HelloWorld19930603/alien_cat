var clickd = "";
var status = true;
var baseUrl = "http://localhost:8086";
var url = "ws://localhost:8086/websocket";
var ws = "";
var message = {"id": "", "msg": "", "from": "", "to": ""};

function connection(username) {
    ws = new WebSocket(url);
    console.log("connection.......");
    ws.onmessage = function (e) {
        var json = eval('(' + e.data.toString() + ')');
        if (json.from) {
            showMessage(json.from + ":" + json.msg);
        }
        if (json.live) {
            showMessage2(json.live)
        }
    }
    ws.onclose = function () {
        status = false
    }
    ws.onerror = function (e) {
        status = false
        showMessage3("出问题了");
    }
    ws.onopen = function () {
        message.id = username;
        console.log(username)
        message.msg = "newUser";
        console.log(JSON.stringify(message));
        ws.send(JSON.stringify(message));
        message.msg = "";

    }
}


$(".start-conn-btn").click(function () {
    connection();
});
$(".send-btn").click(function () {//send message
    console.log(clickd)
    message.to = clickd;
    message.msg = $(".msg-context").val();
    $(".msg-context").val("");
    ws.send(JSON.stringify(message));
    showMessage("我:" + message.msg);
    message.msg = "";

});

function send() {//send message
    console.log(clickd)
    message.to = clickd;
    message.msg = $(".msg-context").val();
    $(".msg-context").val("");
    ws.send(JSON.stringify(message));
    showMessage("我:" + message.msg);
    message.msg = "";

}

function showMessage(msg) {
    console.log(msg)
    $(".show-message").append(msg + "<br/>");
    $('.show-message').scrollTop($('.show-message')[0].scrollHeight);

}

function showMessage3(msg) {
    alert(msg)
}

function showMessage2(a) {
    var strings = a.split(',');
    var aaa = "";
    for (var i = 0; i < strings.length - 1; i++) {
        var string1 = strings[i];
        aaa += "<button class='sb' id=" + string1 + " >" + string1 + "</button>";
    }

    $(".liveSpan").html(aaa)
    for (var i = 0; i < strings.length - 1; i++) {
        var lia = document.getElementsByClassName('sb')
        lia[i].onclick = function () {
            clickd = this.innerHTML
            console.log(this.innerHTML)
            $(".to-user").html(clickd)
        }
    }

}

function login() {
    var val = $(".username").val();
    username = val;
    $.ajax({
        type: "post",
        url: baseUrl + "/login",
        data: {name: $(".username").val(), password: $(".password").val()},
        dataType: "json",
        success: function (data) {
            console.log("success")
            sessionStorage.setItem("user", JSON.stringify(data))
            window.location.href = "./index.html"
        },
        error: function (data) {
            console.log("error")
            $('.msg_p').html("用户名或密码错误")
            console.log(data)
        }
    });
}
