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


// $("input:button").click(function () {
//     var userName = $("#userName").val();
//     var userPwd = $("#userPwd").val();
//     var url = "https://api.fc.xd.style/login";
//     var data = {
//         "username": userName,
//         "password": userPwd
//     };
//     $.ajax({
//         type: 'post',
//         url: url,
// //             ajax异步不刷新页面，这里如果是异步，则登录操作不会更新
//          async: false,
//         contentType: 'application/json',
//         data: JSON.stringify(data),
//         dataType: 'json',
//         success: function (response) {
//             if (response.status === "SUCCESS") {
//                 setCookie("userToken", response.token, 1);
//                 window.location.href = "./index.html";
//             }
//         }
//     });
// });

var currentID;
function RenderDomInfo() {
    var r = location.search;
    currentID=r.split("=")[1];
    var i = 0;
    var url = "";
    var pageIndex = new RegExp("index.html");
    var pageBaseInfo = new RegExp("baseInfo.html");
    var pagePreviewData = new RegExp("previewData.html");
    var pageElectronicData = new RegExp("electronicData.html");
    var pageCourseMessage = new RegExp("courseMessage.html");
    url = pageIndex.test(location.pathname) ? "https://api.fc.xd.style/course" : (pageBaseInfo.test(location.pathname) ?
        "https://api.fc.xd.style/course/"+currentID+"" : (pagePreviewData.test(location.pathname) ?
            ("https://api.fc.xd.style/course/"+currentID+"/data/preview") : (pageElectronicData.test(location.pathname) ?
                "https://api.fc.xd.style/course/"+currentID+"/data/edata" : "https://api.fc.xd.style/course/"+currentID+"/comment")));

    $.ajax({
        type: 'get',
        //AJAX异步一直保持监听，不能访问全局变量，所以要改为同步（jquery不建议这么做，会出现警告）。或者另外一个解决办法是写入到浏览器的sessionStorage里
        url: url,
        headers: {
            Authorization: getCookie("userToken")
        },
        success: function (response) {
            if (pageIndex.test(location.pathname)) {
                //动态添加课程
                for (i = 0; i < response.courses.length; i++) {
                    var tmp=i;
                    ++tmp;
                    $("#courseBox").append($("<li><a href='baseInfo.html?courseID="+tmp+"'"+" class='a_get'> <div class='course'><h4>" + response.courses[i].name + "</h4><time>2017-06-01</time></div></a></li>"));
                }
            }
            else if (pageBaseInfo.test(location.pathname)) {
                $("#courseName").val(response.name);
                $("#courseClass").val(response.major);
                $("#courseId").val(response.id);
            }
            else if (pagePreviewData.test(location.pathname)) {
                console.log(response);
                for (i = 0; i < response.data.length; i++) {
                    ($(".courseInfo").find("form")).prepend($("<div class='preview'>" + "<input type='text' class='courseContent' readonly='readonly' value='" + response.data[i].position + "'><button onclick='deletePreData(this)' data-message-id="+response.data[i].id+">删除</button></div>"));
                }
            }
            else if (pageElectronicData.test(location.pathname)) {
                for (i = 0; i < response.data.length; i++) {
                    ($(".courseInfo").find("form")).prepend($("<div class='preview'>" +
                        "<input type='text' class='courseContent' readonly='readonly' value='" + response.data[i].position + "'></div>"));
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
}



