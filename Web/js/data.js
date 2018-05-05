function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i].trim();
        if (c.indexOf(name) == 0)
            return c.substring(name.length, c.length);
    }
    return "";
}

function logAndReg() {
    var operate = location.search;
    var log = new RegExp("log");
    var reg = new RegExp("reg");
    if (log.test(operate)) {
        $("#submit").prepend("<a href='logAndReg.html?operate=reg'><span style='font-size: 10px;margin-right: 50px;color: blue'>没有账号，点击注册?</span></a>");
    } else if (reg.test(operate)) {
        $("h4").text("注册");
        $("input:button").val("注册");
        $("#submit").prepend("<a href='logAndReg.html?operate=log'><span style='font-size: 10px;margin-right: 50px;color: blue'>已有账号，点击登录?</span></a>");
    }

    $("input:button").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        var baseUrl = "https://api.fc.xd.style";
        var data;

        if (this.value === "登录") {
            data = {
                "username": username,
                "password": password
            };
            $.ajax({
                type: 'post',
                url: baseUrl + "/login",
                async: false,               // ajax异步不刷新页面，这里如果是异步，则登录操作不会更新
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: 'json',
                success: function (response) {
                    if (response.status === "SUCCESS") {
                        setCookie("userToken", response.token, 1);
                        window.location.href = "./index.html";
                    }
                }
            });
        } else if (this.value === "注册") {
            data = {
                "username": username,
                "password": password,
                "role": "teacher"
            };
            $.ajax({
                type: 'post',
                url: baseUrl + "/register",
                async: false,
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: 'json',
                success: function (response) {
                    if (response.status==="SUCCESS"){
                        alert("注册成功");
                        location.href="logAndReg.html?operate=log";
                    }
                }
            });
        }
    });
}

var currentID;

function baseJS() {
    (function setSizeLayout() {
        //得到浏览器窗口内部的尺寸
        var getBodyHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
        var getBodyWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
        document.getElementById("pageHeight").style.height = getBodyHeight + "px";
        document.getElementById("pageHeight").style.width = getBodyWidth * 0.70 + "px";
    })();



    (function RenderDomInfo() {
        var r = location.search;
        currentID = r.split("=")[1];
        var i = 0;
        var url = "";
        var pageIndex = new RegExp("index.html");
        var pageBaseInfo = new RegExp("baseInfo.html");
        var pagePreviewData = new RegExp("previewData.html");
        var pageElectronicData = new RegExp("electronicData.html");
        var pageCourseMessage = new RegExp("courseMessage.html");
        url = pageIndex.test(location.pathname) ? "https://api.fc.xd.style/course" : (pageBaseInfo.test(location.pathname) ?
            "https://api.fc.xd.style/course/" + currentID + "" : (pagePreviewData.test(location.pathname) ?
                ("https://api.fc.xd.style/course/" + currentID + "/data/preview") : (pageElectronicData.test(location.pathname) ?
                    "https://api.fc.xd.style/course/" + currentID + "/data/edata" : "https://api.fc.xd.style/course/" + currentID + "/comment")));

        $.ajax({
            type: 'get',
            //AJAX异步一直保持监听，不能访问全局变量，所以要改为同步（jquery不建议这么做，会出现警告）。或者另外一个解决办法是写入到浏览器的sessionStorage里
            url: url,
            async:false,
            headers: {
                Authorization: getCookie("userToken")
            },
            success: function (response) {
                if (pageIndex.test(location.pathname)) {
                    //动态添加课程
                    for (i = 0; i < response.courses.length; i++) {
                        var tmp = i;
                        ++tmp;
                        $("#courseBox").append($("<li><a href='baseInfo.html?courseID=" + tmp + "'" + " class='a_get'> <div class='course'><h4>" + response.courses[i].name + "</h4><time></time></div></a></li>"));
                    }
                }
                else if (pageBaseInfo.test(location.pathname)) {
                    $("#courseName").val(response.name);
                    $("#courseClass").val(response.major);
                    $("#courseId").val(response.id);
                }
                else if (pagePreviewData.test(location.pathname)) {
                    for (i = 0; i < response.data.length; i++) {
                        ($(".courseInfo").find("form")).prepend($("<div class='preview'>" + "<input type='text' class='courseContent' readonly='readonly' value='" + response.data[i].position + "'><button type='button' onclick='deletePreData(this)' data-message-id=" + response.data[i].id + ">删除</button></div>"));
                    }
                }
                else if (pageElectronicData.test(location.pathname)) {
                    for (i = 0; i < response.data.length; i++) {
                        ($(".courseInfo").find("form")).prepend($("<div class='preview'>" +
                            "<input type='text' class='courseContent' readonly='readonly' value='" + response.data[i].position + "'><button type='button' onclick='deleteEleData(this)' data-message-id=" + response.data[i].id + ">删除</button></div>"));
                    }
                }
                else if (pageCourseMessage.test(location.pathname)) {
                    //js实现翻页功能
                    var totalPageNum = Math.ceil(response.comments.length / 4);//（留言数除以每页显示的留言向上取整）
                    var everyPageNum = 3;//页面显示的页数
                    var ul = document.createElement("ul");
                    var pageTurning = document.getElementById("pageTurning");
                    pageTurning.appendChild(ul);
                    var pageTurningUl = pageTurning.getElementsByTagName("ul")[0];
                    addLiSpan("首页");
                    addLiSpan("上一页");
                    for (var i = 1; i < everyPageNum + 1; i++) {
                        addLiSpan(i);
                    }
                    addLiSpan("下一页");
                    addLiSpan("尾页");

                    function addLiSpan(spanContent) {
                        var li = document.createElement("li");
                        var span = document.createElement("span");
                        span.innerHTML = (spanContent);
                        li.appendChild(span);
                        pageTurningUl.appendChild(li);
                        if (spanContent === 1) {
                            li.style.backgroundColor = "#777777";
                        }
                    }

                    var lis = document.getElementById("pageTurning").getElementsByTagName("ul")[0].getElementsByTagName("li");
                    var indexHead = 2;//这是第一个li的索引
                    var valueHead = 1;//这是第一页的值
                    var indexFooter = everyPageNum + 1;//这是最后一个li的索引
                    var valueFooter = totalPageNum;//这是最后一页的值
                    var currentIndex = 2;//这是当前的li索引

                    for (i = 0; i < 4; i++) {
                        $(".courseMessage h4").after($("<div class='messageUser'><p><b>" + response.comments[i].user.role.role + ":</b>" + response.comments[i].content + "</p><time>" + response.comments[i].date + "</time></div>"));
                    }
                    verticalCenter();
                    for (var j = 0; j < lis.length; j++) {
                        if (j === 0) {
                            //js闭包使点击事件里面的函数可以访问到外部j的当前值(没有闭包j的值就是循环后的最后一个值)
                            (function (j) {//j为形参
                                lis[j].onclick = function () {
                                    deleteBgColor();
                                    currentIndex = 2;
                                    lis[2].style.backgroundColor = "#777777";
                                    var temp = valueHead;
                                    for (var k = indexHead; k < indexHead + everyPageNum; k++) {
                                        lis[k].getElementsByTagName("span")[0].innerHTML = (temp++);
                                    }
                                    var currentPage = lis[currentIndex].getElementsByTagName("span")[0].innerHTML;
                                    $(".messageUser").remove();
                                    for (i = (currentPage - 1) * 4; i < currentPage * 4; i++) {
                                        $(".courseMessage h4").after($("<div class='messageUser'><p><b>" + response.comments[i].user.role.role + ":</b>" + response.comments[i].content + "</p><time>" + response.comments[i].date + "</time></div>"));
                                    }
                                    verticalCenter();
                                }
                            }(j))//j实参
                        } else if (j === 1) {
                            (function (j) {//j为形参
                                lis[j].onclick = function () {
                                    if (currentIndex === indexHead) {
                                        if (lis[currentIndex].getElementsByTagName("span")[0].innerHTML === valueHead) {


                                        } else if (lis[currentIndex].getElementsByTagName("span")[0].innerHTML > valueHead) {
                                            for (var k = indexHead; k < indexHead + everyPageNum; k++) {
                                                --lis[k].getElementsByTagName("span")[0].innerHTML;
                                            }
                                        }
                                    } else {
                                        deleteBgColor();
                                        currentIndex--;
                                        lis[currentIndex].style.backgroundColor = "#777777";
                                    }
                                    var currentPage = lis[currentIndex].getElementsByTagName("span")[0].innerHTML;
                                    $(".messageUser").remove();
                                    for (i = (currentPage - 1) * 4; i < currentPage * 4; i++) {
                                        $(".courseMessage h4").after($("<div class='messageUser'><p><b>" + response.comments[i].user.role.role + ":</b>" + response.comments[i].content + "</p><time>" + response.comments[i].date + "</time></div>"));
                                    }
                                    verticalCenter();
                                }
                            }(j));//j实参
                        } else if (j > 1 && j < lis.length - 2) {
                            (function (j) {//j为形参
                                lis[j].onclick = function () {
                                    deleteBgColor();
                                    currentIndex = j;
                                    lis[currentIndex].style.backgroundColor = "#777777";
                                    var currentPage = lis[currentIndex].getElementsByTagName("span")[0].innerHTML;
                                    $(".messageUser").remove();
                                    for (i = (currentPage - 1) * 4; i < currentPage * 4; i++) {
                                        $(".courseMessage h4").after($("<div class='messageUser'><p><b>" + response.comments[i].user.role.role + ":</b>" + response.comments[i].content + "</p><time>" + response.comments[i].date + "</time></div>"));
                                    }
                                    verticalCenter();
                                }
                            }(j));//j实参
                        } else if (j === 5) {
                            (function (j) {//j为形参
                                lis[j].onclick = function () {
                                    if (currentIndex === indexFooter) {
                                        if (lis[currentIndex].getElementsByTagName("span")[0].innerHTML === valueFooter) {

                                        } else if (lis[currentIndex].getElementsByTagName("span")[0].innerHTML < valueFooter) {
                                            for (var k = indexHead; k < indexHead + everyPageNum; k++) {
                                                ++lis[k].getElementsByTagName("span")[0].innerHTML;
                                            }
                                        }
                                    } else {
                                        deleteBgColor();
                                        currentIndex++;
                                        lis[currentIndex].style.backgroundColor = "#777777";
                                    }
                                    var currentPage = lis[currentIndex].getElementsByTagName("span")[0].innerHTML;
                                    $(".messageUser").remove();
                                    for (i = (currentPage - 1) * 4; i < response.comments.length; i++) {
                                        $(".courseMessage h4").after($("<div class='messageUser'><p><b>" + response.comments[i].user.role.role + ":</b>" + response.comments[i].content + "</p><time>" + response.comments[i].date + "</time></div>"));
                                    }
                                }
                            }(j));//j实参

                        } else if (j === 6) {
                            (function (j) {//j为形参
                                lis[j].onclick = function () {
                                    deleteBgColor();
                                    lis[4].style.backgroundColor = "#777777";
                                    var temp = valueFooter;
                                    for (var k = indexFooter; k >= indexHead; k--) {
                                        lis[k].getElementsByTagName("span")[0].innerHTML = (temp--);
                                    }
                                    currentIndex = indexFooter;
                                    var currentPage = lis[currentIndex].getElementsByTagName("span")[0].innerHTML;
                                    $(".messageUser").remove();
                                    for (i = (currentPage - 1) * 4; i < currentPage * 4; i++) {
                                        $(".courseMessage h4").after($("<div class='messageUser'><p><b>" + response.comments[i].user.role.role + ":</b>" + response.comments[i].content + "</p><time>" + response.comments[i].date + "</time></div>"));
                                    }
                                    verticalCenter();
                                };
                            }(j));//j实参
                        }
                    }

                    function deleteBgColor() {
                        for (var k = 0; k < lis.length; k++) {
                            lis[k].style.backgroundColor = "";
                        }
                    }
                }

            }
        });

        function verticalCenter() {
//    翻页中的span元素垂直居中li中(文字垂直居中)
            var getLiTag = document.getElementById("pageTurning").getElementsByTagName("li");
            var getSpanTag = document.getElementById("pageTurning").getElementsByTagName("span");
            for (var i = 0; i < getSpanTag.length; i++) {
                getSpanTag[i].style.lineHeight = getLiTag[0].offsetHeight + "px";
            }
        }
    })();

    (function landscapingInterface() {
        //  给登录注册按钮添加旋转功能,css3的变换和过渡实现
        var logReglis = document.getElementById("addOnMouseOver").getElementsByTagName("li");
        for (var i = 0; i < logReglis.length; i++) {
            logReglis[i].onmouseover = function () {
                this.style.cssText = "transform: rotate(-10deg);transition: transform 1s;" +
                    "-moz-transition: transform 1s;-webkit-transition: transform 1s;-o-transition: transform 1s;";
            };
            logReglis[i].onmouseout = function () {
                this.style.cssText = "transform: rotate(0);transition: transform 1s;" +
                    "-moz-transition: transform 1s;-webkit-transition: transform 1s;-o-transition: transform 1s;";
            };
        }

        // 给课程盒子添加动画效果
        try {
            var courseBoxLis = document.getElementById("courseBox").getElementsByTagName("li");
            for (var j = 0; j < courseBoxLis.length; j++) {
                courseBoxLis[j].onmouseover = function () {
                    this.getElementsByTagName("a")[0].style.cssText = "animation:courseBgAddColor 0.1s;  -moz-animation:courseBgAddColor 0.1s;  " +
                        "-webkit-animation:courseBgAddColor 0.1s;  -o-animation:courseBgAddColor 0.1s;background-color: pink;width: 77%;height: 72%";
                };
                courseBoxLis[j].onmouseout = function () {
                    this.getElementsByTagName("a")[0].style.cssText = "animation:courseBgRemoveColor 0.1s;  -moz-animation:courseBgRemoveColor 0.1s;  " +
                        "-webkit-animation:courseBgRemoveColor 0.1s;  -o-animation:courseBgRemoveColor 0.1s";
                }
            }
        } catch (err) {
            console.log("为了使代码继续运行");
        }
    })();

}


function submit() {
    var fd = new FormData();
    var file = $("#file")[0].files[0];
    fd.append("file", file);
    var previewPage = new RegExp("previewData.html");
    var elecPage = new RegExp("electronicData.html");
    var url = "";
    if (previewPage.test(location.pathname)) {
        url = "https://api.fc.xd.style/course/" + currentID + "/data/preview";
    } else if (elecPage.test(location.pathname)) {
        url = "https://api.fc.xd.style/course/" + currentID + "/data/edata";
    }

    $.ajax({
        type: 'POST',
        url: url,
        data: fd,
        cache: false,
        processData: false,
        contentType: false,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("authorization", getCookie("userToken"))
        },
        xhr: function () {
            var xhr = $.ajaxSettings.xhr();
            if (xhr.upload) {
                xhr.upload.addEventListener("progress", handleProgress, false);
                return xhr;
            }
        }
    }).done(function (res) {
        alert("上传成功!");
    }).fail(function (res) {
        alert("上传失败!");
    });
}

function handleProgress(e) {
    var percentComplete = Math.round((e.loaded) * 100 / e.total);
    $("#percent").html(percentComplete + '%');
    $("#progressNumber").css("width", "" + percentComplete + "px");
}


function deletePreData(ele) {
    var previewId = $(ele).attr("data-message-id");
    var url = "https://api.fc.xd.style/course/" + currentID + "/data/preview/" + previewId;
    var currentUrl = location.href;
    $.ajax({
        type: "DELETE",
        url: url,
        async: false,
        headers: {
            Authorization: getCookie("userToken")
        },
        success: function (result) {
            location.reload();
        }
    });

}

function deleteEleData(ele) {
    var EleId = $(ele).attr("data-message-id");
    var url = "https://api.fc.xd.style/course/" + currentID + "/data/edata/" + EleId;
    $.ajax({
        type: "DELETE",
        url: url,
        async: false,
        headers: {
            Authorization: getCookie("userToken")
        },
        success: function (result) {
            location.reload();
        }
    });
}


function addMenuNavUrl() {
    $(".menuNav ul li:nth-child(1) a").attr("href", "baseInfo.html?courseID=" + currentID);
    $(".menuNav ul li:nth-child(2) a").attr("href", "previewData.html?courseID=" + currentID);
    $(".menuNav ul li:nth-child(3) a").attr("href", "electronicData.html?courseID=" + currentID);
    $(".menuNav ul li:nth-child(4) a").attr("href", "courseMessage.html?courseID=" + currentID);
    $(".menuNav ul li:nth-child(5) a").attr("href", "exercise.html?courseID=" + currentID);
}

function submitExercise() {
    var url="https://api.fc.xd.style/course/"+currentID+"/quiz";
    var exerciseContent=$("#exerciseContent").val();
    var exerciseAnswer=$("#exerciseAnswer").val();
    var data = {
        "content": exerciseContent,
        "answer": exerciseAnswer
    };
    $.ajax({
        type: 'post',
        contentType: 'application/json',
        data: JSON.stringify(data),
        //AJAX异步一直保持监听，不能访问全局变量，所以要改为同步（jquery不建议这么做，会出现警告）。或者另外一个解决办法是写入到浏览器的sessionStorage里
        url: url,
        headers: {
            Authorization: getCookie("userToken")
        },
        success:function (response) {
            alert("添加习题成功");
        }
    })
}